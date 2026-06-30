package com.sqmusicplus.v3.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.download.DownloadStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname SupplementCleanupTask
 * @Description 兜底定时任务：清理卡在 supplement 状态的下载记录。
 *              当 retryWithOtherPlugin 将父级设为 supplement 后，
 *              若所有子记录均已终结（全部成功或全部失败），
 *              本任务将父级更新为正确的最终状态。
 * @Version 1.0.0
 * @Date 2025/7/15
 * @Created by SQ
 */
@Slf4j
@Component
public class SupplementCleanupTask {

    @Autowired
    private DownloadInfoService downloadInfoService;

    @PostConstruct
    public void init() {
        log.info("SupplementCleanupTask 兜底清理定时任务已注册, cron=0 */5 * * * ? (每5分钟执行)");
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void execute() {
        try {
            log.info("=============开始清理卡在 supplement 状态的下载记录===============");
            cleanupSupplementRecords();
        } catch (Throwable t) {
            log.error("=============supplement 清理任务执行失败(严重异常)===============", t);
        }
    }

    /**
     * 查询所有 supplement 状态的根记录，检查其子记录状态并做最终标记
     */
    private void cleanupSupplementRecords() {
        // 1. 查询所有状态为 supplement 的根记录
        LambdaQueryWrapper<DownloadInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DownloadInfo::getDownloadStatus, DownloadStatus.supplement.getValue());
        List<DownloadInfo> supplementRoots = downloadInfoService.list(queryWrapper);

        if (supplementRoots == null || supplementRoots.isEmpty()) {
            log.debug("无卡在 supplement 状态的记录，跳过清理");
            return;
        }

        log.info("发现 {} 条卡在 supplement 状态的根记录，开始检查子记录状态", supplementRoots.size());

        int toSuccessCount = 0;
        int toErrorCount = 0;
        int timeoutErrorCount = 0;

        for (DownloadInfo root : supplementRoots) {
            try {
                // 2. 查询该根记录的所有子记录
                LambdaQueryWrapper<DownloadInfo> childQuery = new LambdaQueryWrapper<>();
                childQuery.eq(DownloadInfo::getParentDownloadId, root.getId());
                List<DownloadInfo> children = downloadInfoService.list(childQuery);

                if (children == null || children.isEmpty()) {
                    // 3a. 无子记录：兜底超时保护
                    if (root.getDownloadUpdateTime() != null &&
                        System.currentTimeMillis() - root.getDownloadUpdateTime().getTime() > 30 * 60 * 1000) {
                        // 超过30分钟无子记录，标记为错误
                        DownloadInfo update = new DownloadInfo();
                        update.setId(root.getId());
                        update.setDownloadStatus(DownloadStatus.error.getValue());
                        update.setDownloadMsg("补充下载超时，无可用插件");
                        downloadInfoService.updateById(update);
                        timeoutErrorCount++;
                        log.warn("supplement 记录 {} ({} - {}) 超时无子记录，已标记为 error",
                            root.getId(), root.getDownloadMusicname(), root.getDownloadArtistname());
                    } else {
                        log.debug("supplement 记录 {} 尚无子记录，等待下次检查", root.getId());
                    }
                } else {
                    // 3b. 有子记录：判断是否存在成功的子记录
                    boolean hasSuccess = children.stream()
                        .anyMatch(c -> DownloadStatus.success.getValue().equals(c.getDownloadStatus()));
                    boolean allTerminal = children.stream()
                        .allMatch(c -> isTerminalStatus(c.getDownloadStatus()));

                    if (hasSuccess) {
                        // 至少一个子记录成功 → supplement_success
                        DownloadInfo update = new DownloadInfo();
                        update.setId(root.getId());
                        update.setDownloadStatus(DownloadStatus.supplement_success.getValue());
                        update.setDownloadMsg("补充下载成功");
                        downloadInfoService.updateById(update);
                        toSuccessCount++;
                        log.info("supplement 记录 {} ({} - {}) 存在成功子记录，已标记为 supplement_success",
                            root.getId(), root.getDownloadMusicname(), root.getDownloadArtistname());
                    } else if (allTerminal) {
                        // 子记录全部终结且无成功 → error
                        DownloadInfo update = new DownloadInfo();
                        update.setId(root.getId());
                        update.setDownloadStatus(DownloadStatus.error.getValue());
                        update.setDownloadMsg("所有重试插件均下载失败");
                        downloadInfoService.updateById(update);
                        toErrorCount++;
                        log.warn("supplement 记录 {} ({} - {}) 所有子记录均失败，已标记为 error",
                            root.getId(), root.getDownloadMusicname(), root.getDownloadArtistname());
                    } else {
                        // 仍有子记录在处理中，跳过
                        log.debug("supplement 记录 {} 仍有子记录在处理中，跳过本次检查", root.getId());
                    }
                }
            } catch (Exception e) {
                log.error("处理 supplement 记录 {} 时发生异常", root.getId(), e);
            }
        }

        log.info("=============supplement 清理完成: 转为 supplement_success={} 条, 转为 error(子记录全失败)={} 条, 转为 error(超时无子记录)={} 条===============",
            toSuccessCount, toErrorCount, timeoutErrorCount);
    }

    /**
     * 判断下载状态是否为终结状态（不再变化）
     */
    private boolean isTerminalStatus(String status) {
        return DownloadStatus.success.getValue().equals(status)
            || DownloadStatus.error.getValue().equals(status);
    }
}
