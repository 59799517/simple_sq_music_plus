import { inject, reactive } from 'vue';
import playListStore from "../stores/playList";


/**
 * 同步播放列表数据到注入的 playerData 对象
 */
export function syncPlayerData() {
    // 注入 playerData 对象
    const playerData = inject("playerData");

    if (playerData && playListStore.playList.length > 0) {
        const currentIndex = playListStore.playIndex;
        const currentTrack = playListStore.playList[currentIndex];

        if (currentTrack) {
            // 更新 playerData 对象的所有属性
            Object.assign(playerData, {
                albumName: currentTrack.albumName || "",
                albumid: currentTrack.albumid || "",
                artistName: currentTrack.artistName || [],
                artistids: currentTrack.artistids || [],
                brTypes: currentTrack.brTypes || [],
                duration: currentTrack.duration || "",
                id: currentTrack.id || "",
                lyric: currentTrack.lyric || "",
                lyricId: currentTrack.lyricId || "",
                name: currentTrack.name || "",
                pic: currentTrack.pic || "",
                plugName: currentTrack.plugName || [],
                playIndex: currentIndex,
            });
        }
    }
}

/**
 * 更新播放列表中特定索引的播放数据
 * @param {Object} playListStore - 播放列表 store 实例
 * @param {number} index - 要设置为当前播放的索引
 */
export function updateCurrentPlayData(index) {
    // 注入 playerData 对象
    const playerData = inject("playerData");

    // 检查索引是否存在
    if (playListStore.playList[index]) {
        // 更新 store 中的播放索引和相关信息
        playListStore.playIndex = index;
        const nowPlay = playListStore.playList[index];

        // 更新 store 状态
        Object.assign(playListStore, {
            albumName: nowPlay.albumName || "",
            albumid: nowPlay.albumid || "",
            artistName: nowPlay.artistName || [],
            artistids: nowPlay.artistids || [],
            brTypes: nowPlay.brTypes || [],
            duration: nowPlay.duration || "",
            id: nowPlay.id || "",
            lyric: nowPlay.lyric || "",
            lyricId: nowPlay.lyricId || "",
            name: nowPlay.name || "",
            pic: nowPlay.pic || "",
            plugName: nowPlay.plugName || [],
        });

        // 如果有 playerData 对象，则同步数据
        if (playerData) {
            Object.assign(playerData, {
                albumName: nowPlay.albumName || "",
                albumid: nowPlay.albumid || "",
                artistName: nowPlay.artistName || [],
                artistids: nowPlay.artistids || [],
                brTypes: nowPlay.brTypes || [],
                duration: nowPlay.duration || "",
                id: nowPlay.id || "",
                lyric: nowPlay.lyric || "",
                lyricId: nowPlay.lyricId || "",
                name: nowPlay.name || "",
                pic: nowPlay.pic || "",
                plugName: nowPlay.plugName || [],
                playIndex: index,
            });
        }

        return true;
    }

    return false;
}

/**
 * 获取当前播放数据
 * @returns {Object} 当前播放数据对象
 */
export function getCurrentPlayerData() {
    const playerData = inject("playerData");
    return playerData || {};
}
