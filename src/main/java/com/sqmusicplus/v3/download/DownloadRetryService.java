package com.sqmusicplus.v3.download;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sqmusicplus.v3.base.entity.DownloadInfo;
import com.sqmusicplus.v3.base.enums.PlugBrType;
import com.sqmusicplus.v3.base.enums.SetConfigEnum;
import com.sqmusicplus.v3.base.service.DownloadInfoService;
import com.sqmusicplus.v3.config.SqConfigCache;
import com.sqmusicplus.v3.plug.base.hander.SearchHanderAbstract;
import com.sqmusicplus.v3.plug.entity.PlugSearchMusicResult;
import com.sqmusicplus.v3.plug.entity.PlugSearchResult;
import com.sqmusicplus.v3.plug.entity.SearchKeyData;
import com.sqmusicplus.v3.utils.MusicUtils;
import static com.sqmusicplus.v3.utils.StringUtils.toSimplified;
import com.sqmusicplus.v3.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @Classname DownloadRetryService
 * @Description 下载失败后使用其他插件重试下载服务
 * @Version 1.0.0
 * @Date 2025/7/15
 * @Created by SQ
 */
@Slf4j
@Service
public class DownloadRetryService {

    @Autowired
    private DownloadInfoService downloadInfoService;

    @Autowired(required = false)
    private List<SearchHanderAbstract> searchHanderAbstractList;

    /**
     * 当下载失败时，尝试使用其他插件重新下载
     *
     * @param failedRecord 下载失败记录
     * @return true 表示已创建新的重试下载记录，false 表示无需重试 
     */
    public boolean retryWithOtherPlugin(DownloadInfo failedRecord) {
        if(failedRecord.getParentDownloadId()!=null){
            log.debug("子任务，不重试");
            return false;
        }


        // 1. 检查开关
        String switchValue = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FAILED_USE_OTHER_PLUGIN);
        if (!"true".equalsIgnoreCase(switchValue)) {
            log.debug("下载失败重试开关未开启，不执行重试");
            return false;
        }

        // 检查是否有可用的插件处理器
        if (searchHanderAbstractList == null || searchHanderAbstractList.isEmpty()) {
            log.warn("没有可用的搜索处理器，无法重试");
            return false;
        }

        // 2. 读取插件顺序配置
        String orderStr = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FAILED_USE_OTHER_PLUGIN_ORDER);
        if (StringUtils.isBlank(orderStr)) {
            log.debug("下载失败重试插件顺序未配置");
            return false;
        }
        String[] pluginOrder = orderStr.split(",");

        // 3. 读取匹配模式
        String matchMode = SqConfigCache.getSqConfigValue(SetConfigEnum.SYSTEM_DOWNLOAD_FAILED_USE_OTHER_PLUGIN_MATCH_MODE);
        if (StringUtils.isBlank(matchMode)) {
            matchMode = "name_artist_fuzzy";
        }

        // 4. 查询该歌曲的所有历史下载记录，收集已尝试过的插件
        String musicName = failedRecord.getDownloadMusicname();
        String artistName = failedRecord.getDownloadArtistname();
        if (StringUtils.isBlank(musicName)) {
            log.warn("下载失败记录歌曲名称为空，无法重试, id={}", failedRecord.getId());
            return false;
        }

        Set<String> triedPlugins = getTriedPlugins(failedRecord);

        // 原失败的插件也加入已尝试集合（确保不再用原插件重试）
        if (failedRecord.getDownloadPlugName() != null) {
            triedPlugins.add(failedRecord.getDownloadPlugName());
        }

        // 根记录ID：所有重试记录都指向这个根
        Integer rootId = failedRecord.getParentDownloadId() != null ? failedRecord.getParentDownloadId() : failedRecord.getId();

        // 5. 将父级状态改为补充下载(supplement)状态，标记正在尝试其他插件
        try {
            DownloadInfo parentRecord = new DownloadInfo();
            parentRecord.setId(rootId);
            parentRecord.setDownloadStatus(DownloadStatus.supplement.getValue());
            downloadInfoService.updateById(parentRecord);
            log.info("父级下载记录 {} 状态已改为补充下载(supplement)", rootId);
        } catch (Exception e) {
            log.error("修改父级下载记录状态为supplement失败, id={}", rootId, e);
        }

        // 6. 遍历插件顺序，找到第一个未尝试过的插件
        for (String plugName : pluginOrder) {
            plugName = plugName.trim();
            if (StringUtils.isBlank(plugName)) {
                continue;
            }
            if (triedPlugins.contains(plugName)) {
                log.debug("插件 {} 已尝试过，跳过", plugName);
                continue;
            }

            // 获取该插件的处理器
            SearchHanderAbstract handler;
            try {
                handler = MusicUtils.getPlugHander(plugName, searchHanderAbstractList);
            } catch (Exception e) {
                log.warn("获取插件处理器失败: {}", plugName, e);
                triedPlugins.add(plugName);
                continue;
            }

            // 6. 在新插件中搜索歌曲
            try {
                PlugSearchMusicResult matched = searchSongInPlugin(handler, musicName, artistName,
                        failedRecord.getDownloadAlbumname(), matchMode);
                if (matched == null) {
                    log.info("在插件 {} 中未匹配到歌曲: {} - {}", plugName, artistName, musicName);
                    // 插入一条错误记录，标记该插件未找到歌曲
                    DownloadInfo noMatchRecord = new DownloadInfo()
                            .setDownloadMusicname(musicName)
                            .setDownloadArtistname(artistName)
                            .setDownloadAlbumname(failedRecord.getDownloadAlbumname())
                            .setDownloadPlugName(plugName)
                            .setDownloadStatus(DownloadStatus.error.getValue())
                            .setDownloadMsg("未找到对应歌曲")
                            .setParentDownloadId(rootId)
                            .setDownloadTime(new java.util.Date())
                            .setDownloadMusicId("0")
                            .setDownloadUpdateTime(new java.util.Date());
                    try {
                        downloadInfoService.save(noMatchRecord);
                    } catch (Exception addEx) {
                        log.error("保存未找到歌曲记录失败", addEx);
                    }
                    triedPlugins.add(plugName);
                    continue;
                }

                // 7. 确定音质：尝试匹配原音质，否则取最大音质

                PlugBrType targetBr = resolveBrType(failedRecord.getDownloadBrType(), matched.getBrTypes());
                if (targetBr == null) {
                    log.warn("插件 {} 中无可用的音质类型", plugName);
                    triedPlugins.add(plugName);
                    continue;
                }

                // 8. 创建新的下载记录
                DownloadInfo newRecord = handler.musicToDownloadInfo(matched, targetBr, false);
                newRecord.setDownloadStatus(DownloadStatus.waiting.getValue());
                // 设置父级下载ID（指向第一次失败的原始记录），所有重试都指向同一个根
                newRecord.setParentDownloadId(rootId);
                // 设置错误信息，标明是重试产生的记录
                newRecord.setDownloadMsg("由插件 " + failedRecord.getDownloadPlugName() + " 下载失败后自动重试");

                boolean saved = downloadInfoService.add(newRecord);
                if (saved) {
                    log.info("下载失败重试成功: {} - {}, 原插件={}, 新插件={}, 新记录id={}",
                            artistName, musicName, failedRecord.getDownloadPlugName(), plugName, newRecord.getId());
                    // 找到匹配歌曲并创建子记录成功，立即将父级改为 supplement_success
                    try {
                        DownloadInfo parentRecord = new DownloadInfo();
                        parentRecord.setId(rootId);
                        parentRecord.setDownloadStatus(DownloadStatus.supplement_success.getValue());
                        downloadInfoService.updateById(parentRecord);
                        log.info("父级下载记录 {} 状态已改为补充成功(supplement_success)", rootId);
                    } catch (Exception e) {
                        log.error("修改父级下载记录状态为supplement_success失败, id={}", rootId, e);
                    }
                    return true;
                } else {
                    log.error("下载失败重试保存记录失败: {} - {}, 新插件={}", artistName, musicName, plugName);
                    triedPlugins.add(plugName);
                }
            } catch (Exception e) {
                log.error("在插件 {} 中搜索/创建下载记录失败: {} - {}", plugName, artistName, musicName, e);
                triedPlugins.add(plugName);
            }
        }

        log.info("下载失败重试结束，所有可用插件均已尝试: {} - {}", artistName, musicName);
        return false;
    }

    /**
     * 查询该歌曲的历史下载记录，收集已尝试过的插件名称
     * 所有重试记录都指向同一个根 parentDownloadId，一次查询即可全部找到
     */
    private Set<String> getTriedPlugins(DownloadInfo failedRecord) {
        Set<String> tried = new HashSet<>();
        try {
            // 找到根记录ID（所有重试都指向这个根）
            Integer rootId = failedRecord.getParentDownloadId() != null ? failedRecord.getParentDownloadId() : failedRecord.getId();

            // 一次查询：根记录 + 所有指向根的记录
            LambdaQueryWrapper<DownloadInfo> query = new LambdaQueryWrapper<>();
            query.eq(DownloadInfo::getId, rootId)
                 .or(w -> w.eq(DownloadInfo::getParentDownloadId, rootId));
            List<DownloadInfo> historyList = downloadInfoService.list(query);

            // 如果查不到任何记录（老数据没有parentDownloadId），降级为名称+歌手查询
            if (historyList.isEmpty()) {
                LambdaQueryWrapper<DownloadInfo> fallbackQuery = new LambdaQueryWrapper<>();
                fallbackQuery.eq(DownloadInfo::getDownloadMusicname, failedRecord.getDownloadMusicname());
                if (StringUtils.isNotBlank(failedRecord.getDownloadArtistname())) {
                    fallbackQuery.eq(DownloadInfo::getDownloadArtistname, failedRecord.getDownloadArtistname());
                }
                historyList = downloadInfoService.list(fallbackQuery);
            }

            for (DownloadInfo info : historyList) {
                if (StringUtils.isNotBlank(info.getDownloadPlugName())) {
                    tried.add(info.getDownloadPlugName());
                }
            }
        } catch (Exception e) {
            log.error("查询下载历史失败", e);
        }
        return tried;
    }

    /**
     * 在新插件中搜索歌曲，并根据匹配模式选取最佳结果
     */
    private PlugSearchMusicResult searchSongInPlugin(SearchHanderAbstract handler,
                                                      String musicName, String artistName,
                                                      String albumName, String matchMode) {
        // 使用歌曲名称作为搜索关键词
        SearchKeyData searchKeyData = new SearchKeyData();
        searchKeyData.setSearchkey(musicName+" "+artistName+" "+albumName);
        searchKeyData.setPlugName(handler.getPlugName());
        searchKeyData.setPageIndex(1);
        searchKeyData.setPageSize(20);

        PlugSearchResult<PlugSearchMusicResult> searchResult = handler.querySongByName(searchKeyData);
        if (searchResult == null || searchResult.getRecords() == null || searchResult.getRecords().isEmpty()) {
            return null;
        }

        List<PlugSearchMusicResult> records = searchResult.getRecords();


//         根据匹配模式选择最佳结果
        switch (matchMode) {
            case "strict":
                return matchStrict(records, musicName, artistName, albumName);
            case "name_artist_alubm_like":
                return matchNameArtistAlbumLike(records, musicName, artistName, albumName);
            case "name_fuzzy":
                return matchNameFuzzy(records, musicName);
            case "name_artist_fuzzy":
            default:
                return matchNameArtistFuzzy(records, musicName, artistName);
        }
    }

    /**
     * 严格匹配：名称+歌手+专辑完全一致（忽略大小写和空格）
     */
    private PlugSearchMusicResult matchStrict(List<PlugSearchMusicResult> records,
                                               String musicName, String artistName, String albumName) {
        for (PlugSearchMusicResult r : records) {
            if (equalsIgnoreCaseAndSpace(r.getName(), musicName)
                    && equalsIgnoreCaseAndSpace(joinArtistNames(r), artistName)
                    && equalsIgnoreCaseAndSpace(r.getAlbumName(), albumName)) {
                return r;
            }
        }
        return null;
    }

    /**
     * 名称+歌手+专辑包含匹配
     */
    private PlugSearchMusicResult matchNameArtistAlbumLike(List<PlugSearchMusicResult> records,
                                                            String musicName, String artistName, String albumName) {
        for (PlugSearchMusicResult r : records) {
            if (containsIgnoreCase(stripParentheses(r.getName()), stripParentheses(musicName))
                    && containsIgnoreCase(stripParentheses(joinArtistNames(r)), stripParentheses(artistName))
                    && containsIgnoreCase(stripParentheses(r.getAlbumName()), stripParentheses(albumName))) {
                return r;
            }
        }
        return null;
    }

    /**
     * 移除字符串中括号内的内容（包括括号本身），并去除多余空格
     */
    private String stripParentheses(String str) {
        if (str == null) return null;
        return str.replaceAll("\\([^)]*\\)", "").trim();
    }

    /**
     * 名称+歌手包含匹配（推荐默认值）
     */
    private PlugSearchMusicResult matchNameArtistFuzzy(List<PlugSearchMusicResult> records,
                                                        String musicName, String artistName) {
        for (PlugSearchMusicResult r : records) {
            if (containsIgnoreCase(r.getName(), musicName)
                    && containsIgnoreCase(joinArtistNames(r), artistName)) {
                return r;
            }
        }
        return null;
    }

    /**
     * 仅名称包含匹配（最宽松）
     */
    private PlugSearchMusicResult matchNameFuzzy(List<PlugSearchMusicResult> records, String musicName) {
        for (PlugSearchMusicResult r : records) {
            if (containsIgnoreCase(r.getName(), musicName)) {
                return r;
            }
        }
        return null;
    }

    /**
     * 解析音质：优先匹配原音质类型，否则取最大音质
     */
    private PlugBrType resolveBrType( String originalBrTypeId, List<PlugBrType> availableTypes) {
        if (availableTypes == null || availableTypes.isEmpty()) {
            return null;
        }

        // 尝试匹配原音质的格式和码率
        if (StringUtils.isNotBlank(originalBrTypeId)) {
            PlugBrType original = PlugBrType.findById(originalBrTypeId);
            if (original != null) {
                // 从 availableTypes 中找相同格式（type）且码率最大的
                PlugBrType sameTypeMatch = availableTypes.stream()
                    .filter(t -> t.getType().equals(original.getType()))
                    .max(Comparator.comparing(PlugBrType::getBit))
                    .orElse(null);
                if (sameTypeMatch != null) {
                    return sameTypeMatch;
                }
            }
        }

        // 从 availableTypes 中取最大音质
        return availableTypes.stream()
            .max(Comparator.comparing(PlugBrType::getBit))
            .orElse(null);
    }

    /**
     * 将 PlugSearchMusicResult 的 artistName 列表合并为字符串
     */
    private String joinArtistNames(PlugSearchMusicResult r) {
        if (r.getArtistName() == null || r.getArtistName().isEmpty()) {
            return "";
        }
        return String.join("&", r.getArtistName());
    }

    /**
     * 忽略大小写和空格后比较两个字符串是否相等
     */
    private boolean equalsIgnoreCaseAndSpace(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return toSimplified(a).replaceAll("\\s+", "").equalsIgnoreCase(toSimplified(b).replaceAll("\\s+", ""));
    }

    /**
     * 忽略大小写判断 str 是否包含 keyword
     */
    private boolean containsIgnoreCase(String str, String keyword) {
        if (str == null || keyword == null) return false;
        return toSimplified(str).toLowerCase(Locale.ROOT)
                .contains(toSimplified(keyword).toLowerCase(Locale.ROOT));
    }
}
