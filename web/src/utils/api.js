import request from "./request.js";
import qs from "qs";

 // const baseUrl = 'http://127.0.0.1:8099'
const baseUrl = ''


const configUrl = "/api/config"
const musicUrl = "/api/music"
const downloadUrl = "/api/download"
const taskUrl = "/api/task"
const parserUrl = "/api/parser"
const plugUrl = "/api/plug"
const monitorUrl = "/api/monitor"
const expandUrl = "/api/expand/ali"


/**
 * 登录
 * @param username 用户名
 * @param password 密码
 * @returns {*}
 */
export function login(username,password) {
    return request({
        url: baseUrl +configUrl+ "/login" ,
        method: "post",
        data:{"username":username,"password":password,"device":"web"}
    });
}


/**
 * 登出 退出
 * @returns {*}
 */

export function logout(device) {
    return request({
        url: baseUrl +configUrl+ "/logout" ,
        method: "post",
        data:{"device":device}
    });
}


/**
 * 检查是否登录或者token是否超时
 * @returns {*}
 */
export function isLogin() {
    return request({
        url: baseUrl +configUrl + "/isLogin" ,
        method: "post"
    });
}

/**
 * 获取全部设置
 * @returns {*}
 */
export function getAllSet() {
    return request({
        url: baseUrl +configUrl+ "/getConfigList" ,
        method: "get"
    });
}

/**
 * 修改设置
 * @param configKey 设置唯一标识
 * @param configValue 需要修改的值
 * @returns {*}
 */
export function updateConfig(configKey,configValue) {
    return request({
        url: baseUrl  +configUrl+"/updateConfig" ,
        method: "post",
        data:{"configKey":configKey,"configValue":configValue}
    });
}


/**
 * 获取搜索条件（插件类型）
 * @returns {*}
 */
export function getAllOption() {
    return request({
        url: baseUrl +configUrl+"/getOption",
        method: "get"
    });
}


/**
 * 搜索提示
 * @param plugName 插件类型  kw，qq，qqvip等
 * @param keyword 关键字
 */
export function searchTips(plugName,keyword) {
    return request({
        url: baseUrl +musicUrl+"/searchTips",
        method: "get",
        params:{"plugName":plugName,"keyword":keyword}
    });
}

/**
 * 获取歌曲播放链接
 */
export function getMusicUrl(data,brType) {
    data["brType"] = brType;
    return request({
        url: baseUrl +musicUrl+ "/getDownloadUrl",
        method: "post",
        data:data
    });
}
/**
 * 获取歌曲播放链接
 */
export function getLyric(id,plugName) {
    return request({
        url: baseUrl +musicUrl+ "/getLyric",
        method: "post",
        data: {"id":id,"plugName":plugName}
    });
}

/**
 * 根据歌手ID查询歌手信息和专辑信息
 */
export function getArtistInfo(id,plugName) {
    return request({
        url: baseUrl +musicUrl+ "/artistAlbumById",
        method: "get",
        params:{"id":id,"plugName":plugName},
    });
}
/**
 * 获取专辑信息
 */
export function getAlbumInfo(id,plugName) {
    return request({
        url: baseUrl +musicUrl+ "/albumInfoById",
        method: "get",
        params:{"id":id,"plugName":plugName},
    });
}




/**
 * 搜索歌曲专辑等整体搜索
 * @param plugName 插件类型  kw，qq，mg等
 * @param searType 详见判断
 * @param keyword 关键字
 * @param pageSize 页码最多不超30否则有问题
 * @param pageIndex 当前页
 * @returns {*}
 */
export function musicSearch(plugName,searType = "music",keyword,pageSize=20,pageIndex=1) {
    if (searType === "music"){
        return request({
            url: baseUrl + musicUrl+"/searchSong",
            method: "get",
            params:{"plugName":plugName,"keyword":keyword,"pageSize":pageSize,"pageIndex":pageIndex}

        });
    }else if (searType === "album"){
        return request({
            url: baseUrl + musicUrl+"/searchAlbum",
            method: "get",
            params:{"plugName":plugName,"keyword":keyword,"pageSize":pageSize,"pageIndex":pageIndex}

        });
    }else if (searType === "artist"){
        return request({
            url: baseUrl + musicUrl+"/searchArtist",
            method: "get",
            params:{"plugName":plugName,"keyword":keyword,"pageSize":pageSize,"pageIndex":pageIndex}

        });

    }else if (searType === "artistAllSong"){
        return request({
            url: baseUrl + musicUrl+"/searchArtist",
            method: "get",
            params:{"plugName":plugName,"keyword":keyword,"pageSize":pageSize,"pageIndex":pageIndex}

        });

    }
}

/**
 * 下载单曲
 * @param 下载的数据
 * @returns {*}
 */
export function musicDownload(data,brType) {
    data["brType"] = brType;
    return request({
        url: baseUrl +downloadUrl+ "/downloadSong",
        method: "post",
        data:data
    });
}

/**
 * 根据专辑ID下载专辑内所有歌曲
 */
export function musicDownloadAlbum(data) {
    return request({
        url: baseUrl +downloadUrl+ "/downloadAlbum",
        method: "post",
        data:data
    });
}

/**
 * 根据歌手ID下载全部专辑内的歌曲
 */
export function musicDownloadArtist(data) {
    return request({
        url: baseUrl +downloadUrl+ "/downloadArtistAlbum",
        method: "post",
        data:data
    });
}














/**
 * 单个删除
 * @param id
 * @returns {*}
 */
export  function delDownloadInfo(id) {
    return request({
        url: baseUrl +taskUrl+ "/del",
        method: "post",
        data:{"id":id}
    });
}

/**
 * 重新下载
 * @param id
 * @returns {*}
 */
export  function refreshStatus(id) {
    return request({
        url: baseUrl +taskUrl+ "/refreshTask",
        method: "post",
        data:{"id":id}
    });
}



/**
 * 获取服务器下载信息
 * @param downloadMusicname
 * @param downloadArtistname
 * @param downloadAlbumname
 * @param downloadType
 * @param audioBook
 * @param status
 * @param downloadTimeStart
 * @param downloadTimeEnd
 * @param pageSize
 * @param pageIndex
 * @returns {*}
 */
export function postDownloadInfo(downloadMusicname,downloadArtistname,downloadAlbumname,downloadType,status,downloadTimeStart,downloadTimeEnd,pageSize=20,pageIndex=1) {
    return request({
        url: baseUrl + taskUrl +"/list",
        method: "post",
        data: {"downloadMusicname":downloadMusicname,
            "downloadArtistname":downloadArtistname,
            "downloadAlbumname":downloadAlbumname,
            "downloadPlugName":downloadType,
            "downloadStatus":status,
            "downloadTimeStart":downloadTimeStart,
            "downloadTimeEnd":downloadTimeEnd,
            "pageSize":pageSize,
            "pageIndex":pageIndex
            }
    });
}





/**
 * 删除错误任务
 * @returns {*}
 */
export function delErrorTask() {
    return request({
        url: baseUrl + taskUrl+ "/delErrorTask",
        method: "get"
    });
}

/**
 * 删除成功任务
 * @returns {*}
 */
export function delSuccessTask() {
    return request({
        url: baseUrl + taskUrl+ "/delSuccessTask",
        method: "get"
    });
}

/**
 * 删除待下任务
 * @returns {*}
 */
export function delWaitingTask() {
    return request({
        url: baseUrl + taskUrl+"/delWaitingTask",
        method: "get"
    });
}

/**
 * 错误任务重新下载
 * @returns {*}
 */
export function againTask() {
    return request({
        url: baseUrl + taskUrl+"/againTask",
        method: "get"
    });
}

/**
 * 卡任务的时候用（刷新正在运行的任务）
 * @returns {*}
 */
export function refreshTask() {
    return request({
        url: baseUrl + taskUrl+"/refreshTask",
        method: "get"
    });
}

/**
 * 文本解析下载
 * @param text
 * @returns {*}
 */
export function downloadParserText(text) {
    return request({
        url: baseUrl + downloadUrl+"/downloadParserText",
        method: "post",
        data: {"text":text}
    });
}


/**
 *
 * @param url
 * @param isAudioBook
 * @param bookName
 * @param artist
 * @returns {*}
 */
export function parserUrlAndDownload(url,isAudioBook,bookName,artist) {
    return request({
        url: baseUrl +downloadUrl+ "/downloadParserUrl",
        method: "post",
        data: {"url":url,"isAudioBook":isAudioBook,"bookName":bookName,"artist":artist}
    });


}


/**
 * 获取后端版本号
 * @returns {*}
 */
export function getversion() {
    return request({
        url: baseUrl +configUrl+ "/version",
        method: "get"
    });

}




/**
 * 酷狗刷新token
 */
export function kgRefreshToken() {
    return request({
        url: baseUrl + plugUrl+"/kg/refreshToken",
        method: "get"
    });
}
/**
 * 酷狗签到
 */
export function kgSign() {
    return request({
        url: baseUrl +plugUrl+ "/kg/signIn",
        method: "get"
    });
}

/**
 * 获取酷狗Qr码
 */
export function getKgQrCode() {
    return request({
        url: baseUrl + plugUrl+"/kg/getQrImage",
        method: "get"
    });
}

/**
 * 获取酷狗二维码扫码状态
 */
export function getKgQrCodeStatus() {
    return request({
        url: baseUrl + plugUrl+"/kg/checkQrCodeStatus",
        method: "get"
    });
}

/**
 * 获取酷狗微信二维码
 */
export function getKgWxQrCode() {
    return request({
        url: baseUrl + plugUrl+"/kg/getWxQrImage",
        method: "get"
    });
}

/**
 * 获取酷狗微信二维码扫码状态
 */
export function getKgWxQrCodeStatus() {
    return request({
        url: baseUrl + plugUrl+"/kg/checkWxQrCodeStatus",
        method: "get"
    });
}

/**
 * 获取QQVIP微信二维码
 * @returns {*}
 */
export function getQQVipWechatQrCode() {
    return request({
        url: baseUrl + plugUrl+"/qqvip/getWechatQrImage",
        method: "get"
    });
}
/**
 * 获取QQVIP QQ登录二维码
 */
export function getQQVipQrCode() {
    return request({
        url: baseUrl + plugUrl+"/qqvip/getQrImage",
        method: "get"
    });
}

/**
 * 获得QQvip QQ二维码扫码状态
 */
export function getQQVipQrCodeStatus() {
    return request({
        url: baseUrl + plugUrl+"/qqvip/checkQrCodeStatus",
        method: "get"
    });
}

/**
 * 僧侯东刷新qqvip token
 */
export function refreshQQvipCookie() {
    return request({
        url: baseUrl + plugUrl+"/qqvip/refreshQQvipCookie",
        method: "get"
    });
}
/**
 * 上传json文件
 */
export function getUploadJsonFileUrl() {
    return baseUrl + configUrl+"/importSongList"
}

/**
 * 获取网速情况
 */
export function getNetSpeed() {
    return request({
        url: baseUrl + configUrl+"/getCurrentNetwork",
        method: "get"
    });
}

/**
 * 获取监听列表
 */
export function getMonitor() {
return request({
    url: baseUrl + monitorUrl+"/list",
    method: "get"
})
}

/**
 *新增监听
 */
export function addMonitor(data) {
    return request({
        url: baseUrl + monitorUrl+"/add",
        method: "post",
        data: data
    })
}

/**
 * 删除监听
 */
export function delListen(id) {
    return request({
        url: baseUrl + monitorUrl+"/delete",
        method: "post",
        headers: {
            'Content-Type': 'application/json'
        },
        data: {"id":id}
    })
}

/**
 * 获取歌单信息
 */
export function parserUrlInfo(url) {
    return request({
        url: baseUrl + parserUrl+"/parserUrlInfo",
        method: "post",
        headers: {
            'Content-Type': 'application/json'
        },
        data: {"url":url}
    })

}

/**
 * 获取阿里云盘授权码 URL
 * @returns {*}
 */
export function getAliyunAuthUrl() {
    return request({
        url: baseUrl + expandUrl + "/getAuthorizationCode",
        method: "post"
    });
}

/**
 * 获取确认授权码
 * @param code 授权码
 * @returns {*}
 */
export function saveAliyunAuthCode(code,code_verifier) {
    return request({
        url: baseUrl + expandUrl + "/getConfirmCode",
        method: "post",
        data: {"code": code,"code_verifier":code_verifier}
    });
}

/**
 * 检查 access_token 是否有效
 * @returns {*}
 */
export function checkAccessToken() {
    return request({
        url: baseUrl + expandUrl + "/checkAccessToken",
        method: "get"
    });
}

/**
 * 获取并设置用户信息
 * @returns {*}
 */
export function getAndSetUserInfo() {
    return request({
        url: baseUrl + expandUrl + "/getAndSetUserInfo",
        method: "get"
    });
}

/**
 * 检查文件夹路径
 * @param path 路径
 * @returns {*}
 */
export function checkFolder(path) {
    return request({
        url: baseUrl + expandUrl + "/checkFolder",
        method: "post",
        data: {"path": path}
    });
}

/**
 * 获取默认保存路径
 * @returns {*}
 */
export function getDefaultSavePath() {
    return request({
        url: baseUrl + expandUrl + "/getDefaultSavePath",
        method: "get"
    });
}

/**
 * 自动创建文件夹
 * @param path 路径
 * @returns {*}
 */
export function autoCreateFolder(path) {
    return request({
        url: baseUrl + expandUrl + "/autoCreateFolder",
        method: "post",
        data: {"path": path}
    });
}

/**
 * 全部歌曲扫描上传全量
 */
export function syncOnce() {
    return request({
        url: baseUrl + expandUrl + "/syncOnce",
        method: "get"
    });
}

/**
 * 增量上传
 */
export function incrementalSync() {
    return request({
        url: baseUrl + expandUrl + "/incrementalSync",
        method: "get"
    });
}

/**
 * 查询所有已上传的文件列表
 */
export function queryAllUploadFile() {
    return request({
        url: baseUrl + expandUrl + "/queryAllUploadFile",
        method: "get"
    });
}

/**
 * 查询所有已上传的文件列表树状展示
 */
export function queryAllUploadFileTree() {
    return request({
        url: baseUrl + expandUrl + "/queryAllUploadFileTree",
        method: "get"
    });
}
