<script lang="js" setup>
import {ref, onMounted, onUnmounted, computed, watch, nextTick} from 'vue'
import {NImage, NText, NSpace, NButton, NIcon, NSlider} from 'naive-ui'
import {usePlayListStore} from '../stores/playList'
import '../assets/icons/iconfont.css';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close', 'seek'])

// 使用播放列表存储
const store = usePlayListStore()

const lyricsContainerRef = ref(null)

// 监听键盘事件
const handleKeydown = (event) => {
  // 检查是否按下了ESC键 (keyCode 27)
  if (event.keyCode === 27 || event.key === 'Escape') {
    emit('close')
  }
}

// 组件挂载时添加键盘事件监听器
onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
  lyricsList.value = parseLyrics(store.lyric)
  volumePercentage.value = store.volume * 100
})

// 组件卸载时移除键盘事件监听器
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
  // 不需要做任何清理工作，因为音频元素在Home.vue中管理
})

// 解析歌词
const parseLyrics = (lyricStr) => {
  if (!lyricStr) return []

  const lines = lyricStr.split('\n')
  const lyrics = []

  for (const line of lines) {
    // 匹配时间戳 [00:00.00] 格式
    const timeMatch = line.match(/\[(\d{2}):(\d{2})\.(\d{2,3})\](.*)/)
    if (timeMatch) {
      const minutes = parseInt(timeMatch[1])
      const seconds = parseInt(timeMatch[2])
      const milliseconds = parseInt(timeMatch[3])
      const text = timeMatch[4]

      const time = minutes * 60 + seconds + milliseconds / 1000

      lyrics.push({
        time,
        text
      })
    }
  }

  // 按时间排序
  lyrics.sort((a, b) => a.time - b.time)
  return lyrics
}

// 获取歌词列表
const lyricsList = ref([])

// 当前高亮歌词索引
const currentLyricIndex = ref(-1)

// 计算属性：当前歌曲信息
const currentSong = computed(() => {
  return {
    id: store.id,
    name: store.name,
    artistName: store.artistName,
    albumName: store.albumName,
    pic: store.pic,
    lyric: store.lyric
  }
})

// 计算属性：背景样式
const backgroundStyle = computed(() => {
  return {
    backgroundImage: `url(${currentSong.value.pic || 'https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png'})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    backgroundRepeat: 'no-repeat'
  }
})

// 格式化时间显示 (秒转为 mm:ss)
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

// 计算进度条百分比
const progressPercentage = computed(() => {
  if (store.totalTime <= 0) return 0
  return (store.currentTime / store.totalTime) * 100
})

// 计算当前播放时间（以秒为单位）
const currentPlayTime = computed(() => {
  return (sliderPercentage.value / 100) * store.totalTime
})

// 格式化tooltip显示的时间
const formatTooltip = (percentage) => {
  const timeInSeconds = (percentage / 100) * store.totalTime
  return formatTime(timeInSeconds)
}

// 用于slider的本地响应式变量
const sliderPercentage = ref(0)

// 标记是否正在拖动
const isDragging = ref(false)

// 监听播放进度变化，更新slider值（但不覆盖用户正在拖动时的值）
// 同时更新当前歌词索引
watch(() => store.currentTime, (newTime) => {
  // 更新进度条（但不覆盖用户正在拖动时的值）
  if (!isDragging.value) {
    sliderPercentage.value = progressPercentage.value
  }

  // 更新当前歌词索引
  if (lyricsList.value.length > 0) {
    let index = -1
    for (let i = 0; i < lyricsList.value.length; i++) {
      if (lyricsList.value[i].time <= newTime) {
        index = i
      } else {
        break
      }
    }
    currentLyricIndex.value = index
  }
})

// 处理开始拖动
const handleDragStart = () => {
  isDragging.value = true
}

const handleUpdateSliderValue = (value) => {
  sliderPercentage.value = value
}

// 处理进度条变化 - 仅在释放时更新
const handleProgressChange = () => {
  // 计算实际时间并跳转
  const newTime = (sliderPercentage.value / 100) * store.totalTime
  console.log("结束正在更新进度：" + sliderPercentage.value)
  console.log("结束返回的值：" + sliderPercentage.value)
  console.log("计算出的实际时间：" + newTime)

  // 直接调用Home.vue中的seek方法而不是仅仅更新store
  // 通过事件传递给Home.vue来执行实际的跳转操作
  emit('seek', newTime)
  isDragging.value = false
}

// 切换播放/暂停状态
const togglePlay = () => {
  store.togglePlay()
}

// 播放上一曲
const playPrevious = () => {
  store.playPrevious()
}

// 播放下一曲
const playNext = () => {
  store.playNext()
}

// 监听歌词变化和当前歌词索引变化
watch([() => store.lyric, currentLyricIndex], ([newLyric, oldLyric], [newIndex]) => {
  if (newLyric !== oldLyric) {
    lyricsList.value = parseLyrics(newLyric)
  }

  // 滚动到当前歌词
  scrollToCurrentLyric()
})

// 创建防抖版本的滚动到当前歌词函数
let scrollDebounceTimer = null
const scrollToCurrentLyric = () => {
  // 清除之前的定时器
  if (scrollDebounceTimer) {
    clearTimeout(scrollDebounceTimer)
  }

  // 设置新的定时器，延迟执行滚动操作
  scrollDebounceTimer = setTimeout(() => {
    nextTick(() => {
      // 添加检查确保 lyricsContainerRef 和其子元素存在
      if (!lyricsContainerRef.value) return

      const lyricsContainer = lyricsContainerRef.value.querySelector('.lyrics-content')
      if (!lyricsContainer) return

      const currentLyricElement = lyricsContainer.querySelector('.lyric-line.current')

      if (lyricsContainer && currentLyricElement) {
        const containerHeight = lyricsContainer.clientHeight
        const elementTop = currentLyricElement.offsetTop
        const elementHeight = currentLyricElement.clientHeight

        // 滚动使当前歌词居中
        lyricsContainer.scrollTop = elementTop - containerHeight / 2 + elementHeight / 2
      }
    })
  }, 300) // 100ms的防抖延迟
}

// 点击歌词跳转到指定时间
const handleLyricClick = (time) => {
  emit('seek', time)
}

// 音量控制相关变量
const volumePercentage = ref(0)
const isVolumeDragging = ref(false)

// 监听音量变化
watch(() => store.volume, (newVolume) => {
  if (!isVolumeDragging.value) {
    volumePercentage.value = newVolume * 100
  }
})

// 处理开始拖动音量
const handleVolumeDragStart = () => {
  isVolumeDragging.value = true
}

// 处理音量滑块更新
const handleUpdateVolumeValue = (value) => {
  volumePercentage.value = value
}

// 处理音量变化
const handleVolumeChange = () => {
  const newVolume = volumePercentage.value / 100
  store.setVolume(newVolume)
  isVolumeDragging.value = false
}

// 添加静音切换方法
const previousVolume = ref(50) // 保存之前的音量值

const toggleMute = () => {
  if (volumePercentage.value === 0) {
    // 如果当前是静音状态，恢复到之前的音量
    volumePercentage.value = previousVolume.value
  } else {
    // 如果当前不是静音状态，保存当前音量并设置为0
    previousVolume.value = volumePercentage.value
    volumePercentage.value = 0
  }
  handleVolumeChange()
}

</script>

<template>
  <div v-if="visible" class="music-detail-overlay">
    <div class="background-blur" :style="backgroundStyle"></div>
    <div class="music-detail-container">
      <!-- 关闭按钮 -->
      <n-icon class="close-button" @click="emit('close')">
        <Motion
            :hover="{
      scale: 1.6,
    }"
        >
          <i class="iconfont icon-close" style="font-size: 30px">&#xf02a9;</i>

        </Motion>

      </n-icon>

      <div class="content-wrapper">
        <!-- 左侧：歌曲图片、专辑和歌手信息 -->
        <div class="left-panel">
          <div class="album-art">
            <n-image
                :alt="currentSong.name"
                :src="currentSong.pic || 'https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png'"
                class="album-image"
                height="300"
                preview-disabled
                width="300"
            />
          </div>

          <div class="song-info">
            <n-text class="song-title">{{ currentSong.name || '未知歌曲' }}</n-text>
            <n-text class="artist-info">
              歌手: {{
                Array.isArray(currentSong.artistName) ? currentSong.artistName.join(', ') : currentSong.artistName || '未知歌手'
              }}
            </n-text>
            <n-text class="album-info">
              专辑: {{ currentSong.albumName || '未知专辑' }}
            </n-text>
          </div>
        </div>

        <!-- 右侧：歌词信息 -->
        <div class="right-panel">
          <div
              ref="lyricsContainerRef"
              class="lyrics-container"
          >
            <div
                class="lyrics-content"
            >
              <div
                  v-for="(line, index) in lyricsList"
                  :key="index"
                  :class="{ current: index === currentLyricIndex }"
                  class="lyric-line"
                  @click="handleLyricClick(line.time)"
              >
                {{ line.text }}
              </div>
              <div v-if="lyricsList.length === 0" class="no-lyrics">
                暂无歌词
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 播放控制区域 -->
      <div class="player-controls">
        <div class="progress-container">
          <span class="time-text">{{ formatTime(currentPlayTime) }}</span>
          <n-slider
              :format-tooltip="formatTooltip"
              :max="100"
              :min="0"
              :on-dragend="handleProgressChange"
              :on-dragstart="handleDragStart"
              :on-update:value="handleUpdateSliderValue"
              :step="0.1"
              :value="sliderPercentage"
              class="progress-slider"
              show-tooltip
          />
          <span class="time-text">{{ formatTime(store.totalTime) }}</span>
        </div>

        <n-flex justify="space-between">
          <div></div>
          <div>
            <n-flex justify="space-around" size="large">
              <Motion
                  :hover="{
      scale: 1.6,
    }"
              >
                <i class="iconfont control-icon" @click="playPrevious">&#xe69a;</i>

              </Motion>
              <Motion
                  v-if="!store.isPlaying"
                  :hover="{
      scale: 1.6,
    }"
              >
                <i class="iconfont control-icon" @click="togglePlay">&#xe62d;</i>

              </Motion>


              <Motion
                  v-else
                  :hover="{
      scale: 1.6,
    }"
              >
                <i class="iconfont control-icon" @click="togglePlay">&#xe626;</i>

              </Motion>

              <Motion
                  :hover="{
      scale: 1.6,
    }"
              >
                <i class="iconfont control-icon" @click="playNext">&#xe69b;</i>

              </Motion>
            </n-flex>

          </div>
          <div>
            <div class="volume-control">
              <Motion
                  :hover="{
      scale: 1.6,
    }"
              >
                <i
                    class="iconfont control-icon"
                    style="font-size: 24px;"
                    @click="volumePercentage === 0 ? (volumePercentage = 100, handleVolumeChange()) : (volumePercentage = 0, handleVolumeChange())"
                >
                  {{ volumePercentage === 0 ? '&#xea0f;' : '&#xeca6;' }}
                </i>
              </Motion>

              <n-slider
                  :max="100"
                  :min="0"
                  :on-dragend="handleVolumeChange"
                  :on-dragstart="handleVolumeDragStart"
                  :on-update:value="handleUpdateVolumeValue"
                  :step="1"
                  :value="volumePercentage"
                  class="volume-slider"
              />
            </div>
          </div>

        </n-flex>
        <!--        <div class="control-buttons">-->

        <!--          <div class="controls-right">-->

        <!--          </div>-->
        <!--        </div>-->
      </div>
    </div>
  </div>
</template>

<style scoped>
.music-detail-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.9);
  z-index: 10000;
  display: flex;
  justify-content: center;
  align-items: center;
}

.background-blur {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  filter: blur(30px) brightness(0.6);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1;
}

.music-detail-container {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3); /* 调整背景透明度，让模糊背景更明显 */
  border-radius: 0;
  overflow: hidden;
  position: relative;
  box-shadow: none;
}

.close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 10001;
  background-color: transparent !important;
  border: none;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: rgba(255, 255, 255, 0.7);
}

.close-button:hover {
  background-color: transparent !important;
  color: white;
}

.close-button svg {
  width: 100%;
  height: 100%;
}

.content-wrapper {
  display: flex;
  height: calc(100% - 180px); /* 增加底部控制区域的空间 */
  padding-top: 10px;
  gap: 40px;
}

.left-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 30px;
}

.album-art {
  width: 300px;
  height: 300px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.album-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.song-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  text-align: center;
  color: white;
}

.song-title {
  font-size: 28px;
  font-weight: bold;
  color: white;
}

.artist-info, .album-info {
  font-size: 18px;
  color: #ccc;
}

.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
  background-color: transparent;
  padding: 20px;
}

.lyrics-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  outline: none;
  padding: 60px 40px 20px 40px;
  cursor: grab;
  user-select: none;
  overflow: hidden;
  background-color: transparent;
}

.lyrics-container:active {
  cursor: grabbing;
}

.lyrics-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  border-radius: 12px;
  max-height: 100%;
  position: relative;
  scrollbar-width: none;
  -ms-overflow-style: none;
  overflow-y: scroll;
  background-color: transparent;
}

.lyric-line {
  padding: 12px 0;
  text-align: center;
  color: rgba(255, 255, 255, 0.6);
  transition: all 0.3s ease;
  font-size: 18px;
  line-height: 1.5;
  font-weight: 300;
  cursor: pointer;
}

.lyric-line:hover {
  color: rgba(255, 255, 255, 0.9);
}

.lyric-line.current {
  color: white;
  font-size: 24px;
  font-weight: 600;
  transform: scale(1.05);
  margin: 16px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.lyrics-title {
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin-bottom: 30px;
  text-align: center;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.no-lyrics {
  text-align: center;
  color: rgba(255, 255, 255, 0.5);
  font-style: italic;
  padding: 40px 20px;
  font-size: 18px;
}

/* 播放控制区域 */
.player-controls {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px 40px;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  z-index: 2;
}

.progress-container {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.progress-slider {
  flex: 1;
}

.time-text {
  color: white;
  font-size: 14px;
  min-width: 40px;
  text-align: center;
}

.control-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0 50px;
}

.controls-center {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
}

.controls-right {
  display: flex;
  justify-content: flex-end;
  flex: 1;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 150px;
}

.volume-slider {
  flex: 1;
}

.control-icon {
  font-size: 40px;

  color: rgba(255, 255, 255, 0.7);
  user-select: none;
  display: inline-block;
}


.control-buttons .n-button {
  background-color: rgba(255, 255, 255, 0.1);
}

.control-buttons .n-button:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
    padding: 20px;
    padding-top: 60px;
    height: calc(100% - 150px); /* 移动端为控制区域留出更多空间 */
  }

  .left-panel, .right-panel {
    width: 100%;
  }

  .album-art {
    width: 200px;
    height: 200px;
  }

  .song-title {
    font-size: 24px;
  }

  .artist-info, .album-info {
    font-size: 16px;
  }

  .control-buttons {
    gap: 20px;
  }
}
</style>