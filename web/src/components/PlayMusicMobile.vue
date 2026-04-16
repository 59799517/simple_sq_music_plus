<script lang="js" setup>
import {ref, onMounted, onUnmounted, computed, watch, nextTick} from 'vue'
import {NImage, NText, NSpace, NButton, NIcon, NSlider} from 'naive-ui'
import {usePlayListStore} from '../stores/playList'
import { useRouter } from 'vue-router'
import '../assets/icons/iconfont.css';

const router = useRouter()
const emit = defineEmits(['close'])

// 使用播放列表存储
const store = usePlayListStore()

const lyricsContainerRef = ref(null)
const volumeSliderContainer = ref(null) // 音量滑块容器引用
const isFadingOut = ref(false) // 播放按钮淡出动画状态

// 添加滑动关闭相关变量
const startY = ref(0)
const currentY = ref(0)
const isSwiping = ref(false)
const swipeThreshold = 100 // 滑动阈值
const containerTransform = ref(0) // 容器变换值
const isClosing = ref(false) // 是否正在关闭

// 监听键盘事件
const handleKeydown = (event) => {
  // 检查是否按下了ESC键 (keyCode 27)
  if (event.keyCode === 27 || event.key === 'Escape') {
    closeWithAnimation()
  }
}

// 处理触摸开始
const handleTouchStart = (event) => {
  startY.value = event.touches[0].clientY
  isSwiping.value = true
}

// 处理触摸移动
const handleTouchMove = (event) => {
  if (!isSwiping.value) return
  currentY.value = event.touches[0].clientY
  
  // 计算垂直位移
  const deltaY = currentY.value - startY.value
  
  // 只有从顶部开始滑动才触发下拉效果
  if (startY.value < 100 && deltaY > 0) {
    containerTransform.value = Math.min(deltaY, 200) // 限制最大下拉距离
  }
}

// 处理触摸结束
const handleTouchEnd = () => {
  if (!isSwiping.value) return
  isSwiping.value = false
  
  const deltaY = currentY.value - startY.value
  
  // 检查是否是从顶部向下滑动且滑动距离超过阈值
  if (startY.value < 100 && deltaY > swipeThreshold) {
    closeWithAnimation()
  } else {
    // 如果没有达到阈值，恢复原位
    containerTransform.value = 0
  }
}

// 带动画的关闭方法
const closeWithAnimation = () => {
  isClosing.value = true
  containerTransform.value = 100 // 向下移动
  
  // 延迟一段时间后发出关闭事件
  setTimeout(() => {
    emit('close')
  }, 300) // 与CSS过渡时间保持一致
}

// 处理音量按钮触摸事件

// 组件挂载时添加键盘事件监听器
onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
  lyricsList.value = parseLyrics(store.lyric)
  volumePercentage.value = store.volume * 100
  
  // 添加点击其他地方隐藏音量滑块的事件监听器
  
  // 确保音频播放状态同步
  syncAudioState()
})

// 组件卸载时移除事件监听器
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
  // document.removeEventListener('click', handleDocumentClick)
})

// 同步音频状态
const syncAudioState = () => {
  // 通过事件总线或全局状态通知Home.vue更新播放状态
  window.dispatchEvent(new CustomEvent('mobile-player-mounted', {
    detail: {
      isPlaying: store.isPlaying
    }
  }))
}

// 处理文档点击事件，用于隐藏音量滑块
// const handleDocumentClick = (event) => {
//   if (volumeSliderContainer.value && !volumeSliderContainer.value.contains(event.target)) {
//     showVolumeSlider.value = false
//   }
// }

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
    lyric: store.lyric,
    url: store.musicUrl
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
  seek(newTime)
  isDragging.value = false
}

// 切换播放/暂停状态
const togglePlay = () => {
  console.log("切换播放状态")
  isFadingOut.value = true
  
  // 在淡出动画结束后切换播放状态
  setTimeout(() => {
    store.togglePlay()
    // 通知Home.vue更新播放状态
    window.dispatchEvent(new CustomEvent('mobile-player-toggle-play'))
    isFadingOut.value = false
  }, 150)
}

// 播放上一曲
const playPrevious = () => {
  console.log("播放上一曲")
  store.playPrevious()
  // 通知Home.vue播放上一曲
  window.dispatchEvent(new CustomEvent('mobile-player-previous'))
}

// 播放下一曲
const playNext = () => {
  console.log("播放下一曲")
  store.playNext()
  // 通知Home.vue播放下一曲
  window.dispatchEvent(new CustomEvent('mobile-player-next'))
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
  seek(time)
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
  // 通知Home.vue更新音量
  window.dispatchEvent(new CustomEvent('mobile-player-volume-change', {
    detail: { volume: newVolume }
  }))
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

// 跳转到指定时间
const seek = (time) => {
  // 在移动端页面中直接调用store的方法
  store.setCurrentTime(time)
  // 通知Home.vue跳转到指定时间
  window.dispatchEvent(new CustomEvent('mobile-player-seek', {
    detail: { time }
  }))
}

// 返回上一页（带动画）
const goBack = () => {
  closeWithAnimation()
}
</script>

<template>
  <div class="music-detail-overlay" 
       @touchstart="handleTouchStart"
       @touchmove="handleTouchMove"
       @touchend="handleTouchEnd">
    <div class="background-blur" :style="backgroundStyle"></div>
    <div class="music-detail-container" 
         :class="{ 'closing': isClosing }"
         :style="{ transform: `translateY(${containerTransform}px)` }">
      <!-- 顶部导航栏 -->
      <div class="mobile-header">
        <n-icon class="back-button" @click="goBack">
          <i class="iconfont icon-back" style="font-size: 24px">&#xe600;</i>
        </n-icon>
        <div class="header-title"></div> <!-- 移除"正在播放"文字 -->
        <n-icon class="close-button" @click="goBack">
          <i class="iconfont icon-close" style="font-size: 24px">&#xf02a9;</i>
        </n-icon>
      </div>

      <div class="content-wrapper">
        <!-- 歌曲图片 -->
        <div class="album-art-container">
          <div class="album-art">
            <n-image
                :alt="currentSong.name"
                :src="currentSong.pic || 'https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png'"
                class="album-image"
                height="200"
                preview-disabled
                width="200"
            />
          </div>
        </div>

        <!-- 歌曲信息 -->
        <div class="song-info">
          <n-text class="song-title">{{ currentSong.name || '未知歌曲' }}</n-text>
          <n-text class="artist-info">
            {{ Array.isArray(currentSong.artistName) ? currentSong.artistName.join(', ') : currentSong.artistName || '未知歌手' }}
          </n-text>
          <n-text class="album-info">
            {{ currentSong.albumName || '未知专辑' }}
          </n-text>
        </div>

        <!-- 歌词信息 -->
        <div class="lyrics-section">
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

          <div class="control-buttons">
            <n-icon class="control-icon large-icon" @click="playPrevious">
              <i class="iconfont" style="font-size: 48px">&#xe69a;</i>
            </n-icon>
            
            <n-icon 
              v-if="!store.isPlaying"
              class="control-icon play-button large-icon"
              @click="togglePlay"
              :class="{ 'fade-out': isFadingOut }"
            >
              <i class="iconfont" style="font-size: 36px">&#xe62d;</i>
            </n-icon>
            
            <n-icon 
              v-else
              class="control-icon play-button large-icon"
              @click="togglePlay"
              :class="{ 'fade-out': isFadingOut }"
            >
              <i class="iconfont" style="font-size: 36px">&#xe626;</i>
            </n-icon>
            
            <n-icon class="control-icon large-icon" @click="playNext">
              <i class="iconfont" style="font-size: 48px">&#xe69b;</i>
            </n-icon>
          </div>

          <div class="additional-controls">
            <n-icon 
              class="control-icon volume-button"
              @click="toggleMute"
            >
              <i class="iconfont" style="font-size: 24px">{{ volumePercentage === 0 ? '&#xea0f;' : '&#xeca6;' }}</i>
            </n-icon>
            
            <!-- 音量滑块 -->
            <div class="volume-slider-container" ref="volumeSliderContainer">
              <n-slider
                :max="100"
                :min="0"
                :on-dragend="handleVolumeChange"
                :on-dragstart="handleVolumeDragStart"
                :on-update:value="handleUpdateVolumeValue"
                :step="1"
                :value="volumePercentage"
                class="volume-slider"
                :style="{ width: '100px' }"
              />
            </div>
          </div>
        </div>
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
  filter: blur(50px) brightness(0.5);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: -1;
}

.music-detail-container {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
  overflow: hidden;
  position: relative;
  box-shadow: none;
  transition: transform 0.3s ease-out, opacity 0.3s ease-out;
}

.music-detail-container.closing {
  opacity: 0;
  transform: translateY(100vh) !important;
}

/* 移动端头部 */
.mobile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 3;
}

.back-button {
  color: white;
  cursor: pointer;
  padding: 10px; /* 增大点击区域 */
}

.close-button {
  color: white;
  cursor: pointer;
  padding: 10px; /* 增大点击区域 */
}

.header-title {
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  flex-direction: column;
  height: calc(100% - 70px); /* 减去头部高度 */
  padding: 20px;
  gap: 20px;
}

/* 专辑图片区域 */
.album-art-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.album-art {
  width: 200px;
  height: 200px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.album-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 歌曲信息 */
.song-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  text-align: center;
  color: white;
  padding: 0 20px;
}

.song-title {
  font-size: 22px;
  font-weight: bold;
  color: white;
  text-align: center;
}

.artist-info, .album-info {
  font-size: 16px;
  color: #ccc;
  text-align: center;
}

/* 歌词区域 */
.lyrics-section {
  flex: 1;
  min-height: 0;
}

.lyrics-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  outline: none;
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
  font-size: 16px;
  line-height: 1.5;
  font-weight: 300;
  cursor: pointer;
}

.lyric-line:hover {
  color: rgba(255, 255, 255, 0.9);
}

.lyric-line.current {
  color: white;
  font-size: 18px;
  font-weight: 600;
  transform: scale(1.05);
  margin: 16px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.no-lyrics {
  text-align: center;
  color: rgba(255, 255, 255, 0.5);
  font-style: italic;
  padding: 40px 20px;
  font-size: 16px;
}

/* 播放控制区域 */
.player-controls {
  padding: 20px 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  z-index: 2;
}

.progress-container {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding: 0 10px;
}

.progress-slider {
  flex: 1;
}

.time-text {
  color: white;
  font-size: 12px;
  min-width: 35px;
  text-align: center;
}

.control-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 40px;
  margin-bottom: 15px;
}

.control-icon {
  color: rgba(255, 255, 255, 0.7);
  user-select: none;
  display: inline-block;
  cursor: pointer;
  /* 增大点击区域 */
  padding: 15px;
  border-radius: 50%;
}

.control-icon.large-icon {
  font-size: 48px;
}

.control-icon.play-button {
  font-size: 36px;
  transition: all 0.3s ease;
  opacity: 1;
}

.control-icon.play-button.large-icon {
  font-size: 36px;
  transition: all 0.3s ease;
  opacity: 1;
}

.control-icon.play-button.fade-out {
  opacity: 0;
}

.additional-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  position: relative;
}

.volume-button {
  font-size: 24px;
  /* 增大点击区域 */
  padding: 15px;
  border-radius: 50%;
}

/* 音量滑块容器 */
.volume-slider-container {
  position: relative;
  bottom: 0;
  right: 0;
  border-radius: 10px;
  padding: 10px 5px;
  transform: none;
  display: inline-block;
  margin-left: 10px;
  width: 100px;
}

.volume-slider {
  margin: 0 auto;
}

/* 滚动条样式 */
.lyrics-content::-webkit-scrollbar {
  display: none;
}

.lyrics-content {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>