<script setup>
import TopWitge from "./V3TopWitge.vue";
import PlayMusic from "./PlayMusic.vue";
import { useRoute, useRouter } from 'vue-router'
import vDraggable from "../utils/v-draggable";
import { ref,watch ,nextTick, onMounted, onUnmounted } from 'vue';
import {NButton, NSpace,NTag,NImage,NAvatar,NText} from "naive-ui";
import usePlayListStore from "../stores/playList";
import configInfoStore from "../stores/config";


const router = useRouter()
const stplayListStore = usePlayListStore()
const stconfigInfoStore =configInfoStore()


// 添加一个测试方法，验证 pushPlayListAndPlay 是否存在
onMounted(() => {
  console.log('stplayListStore 方法检查:')
  console.log('pushPlayListAndPlay exists:', typeof stplayListStore.pushPlayListAndPlay === 'function')
  console.log('addToPlayList exists:', typeof stplayListStore.addToPlayList === 'function')
})

const audioElement = ref(null)
const showMusicDetail = ref(false)
const isDraggable = ref(true)
const listContainerRef = ref(null)

// 添加键盘事件处理
const handleKeyDown = (event) => {
  // 防止在输入框中按这些键时触发播放控制
  if (event.target.tagName === 'INPUT' || event.target.tagName === 'TEXTAREA') {
    return;
  }

  switch (event.key) {
    case ' ':
      event.preventDefault();
      toggleAudio();
      break;
    case 'p':
    case 'P':
      event.preventDefault();
      stplayListStore.playPrevious();
      break;
    case 'n':
    case 'N':
      event.preventDefault();
      stplayListStore.playNext();
      break;
  }
};

let nowPlay = ref({
  pic:"https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
})

let nowplayList = ref([])

// 组件挂载时滚动到当前播放的歌曲
onMounted(() => {
  nextTick(() => {
    // 页面加载完成后检查播放列表是否有数据
    if (stplayListStore.playList && stplayListStore.playList.length > 0) {
      console.log("初始化数据："+JSON.stringify(stplayListStore.playList))
      // 更新本地播放列表
      nowplayList.value = [...stplayListStore.playList];

      // 如果播放列表有数据，则更新当前播放信息
      if (stplayListStore.playIndex >= 0 && stplayListStore.playIndex < stplayListStore.playList.length) {
        const currentSong = stplayListStore.playList[stplayListStore.playIndex];
        console.log("当前播放歌曲："+JSON.stringify(currentSong))
        nowPlay.value = { ...currentSong };

        // 如果有音乐URL，则设置到audio元素
        if (stplayListStore.musicUrl && audioElement.value) {
          audioElement.value.src = stplayListStore.musicUrl;
        }
      }

      // 设置播放状态
      if (stplayListStore.isPlaying && audioElement.value) {
        nextTick(() => {
          // 确保在DOM更新后再尝试播放
          setTimeout(() => {
            playAudio();
          }, 100);
        });
      }
    }

    scrollToCurrentSong();
  });

  // 添加键盘事件监听器
  window.addEventListener('keydown', handleKeyDown);
});

// 组件卸载时移除键盘事件监听器
onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown);
});

// 监听 当前播放ID 的变化
watch(
    () => stplayListStore.id,
    (newValue, oldValue) => {
      nowPlay.value = {
        albumName : stplayListStore.albumName,
        albumid : stplayListStore.albumid,
        artistName : stplayListStore.artistName,
        artistids : stplayListStore.artistids,
        brTypes : stplayListStore.brTypes,
        duration : stplayListStore.duration,
        id : stplayListStore.id,
        lyric : stplayListStore.lyric,
        lyricId : stplayListStore.lyricId,
        name : stplayListStore.name,
        pic : stplayListStore.pic,
        plugName : stplayListStore.plugName,
      }

      // 当前播放的歌曲变化时，滚动到对应位置
      nextTick(() => {
        scrollToCurrentSong();
      });
    },
    {
      deep: true
    }
);

// 监听播放列表的变化
watch(
    () => stplayListStore.playList,
    (newPlayList) => {
      nowplayList.value = [...newPlayList]; // 创建新数组确保响应性
      console.log(`播放列表已更新：${JSON.stringify(nowplayList.value)}`);

      // 如果播放列表不为空且有当前播放索引，则更新当前播放歌曲信息
      if (newPlayList && newPlayList.length > 0) {
        if (stplayListStore.playIndex >= 0 && stplayListStore.playIndex < newPlayList.length) {
          const currentSong = newPlayList[stplayListStore.playIndex];
          nowPlay.value = { ...currentSong };
        }
      } else {
        // 如果播放列表为空，使用默认图片
        nowPlay.value = {
          pic: "https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
        };
      }

      // 在下次DOM更新后滚动到当前播放的歌曲
      nextTick(() => {
        scrollToCurrentSong();
      });
    },
    {
      deep: true
    }
);

// 滚动到当前播放歌曲的函数
const scrollToCurrentSong = () => {
  if (listContainerRef.value && nowplayList.value.length > 0) {
    const container = listContainerRef.value.$el || listContainerRef.value;
    // 获取当前播放的歌曲索引
    const currentIndex = stplayListStore.playIndex;

    // 查找对应索引的DOM元素并滚动到该元素
    nextTick(() => {
      const itemElements = container.querySelectorAll('.n-list-item');
      if (itemElements.length > currentIndex && currentIndex >= 0) {
        const targetElement = itemElements[currentIndex];
        if (targetElement) {
          // 滚动到目标元素，使其在可视区域居中
          targetElement.scrollIntoView({
            behavior: 'smooth',
            block: 'center'
          });
        }
      }
    });
  }
};

const handleImageError = (e) => {
  console.log('图片加载失败:', e);
  // 可以设置一个默认图片或者保持空值
}

// 播放指定索引的歌曲
const playSong = (index) => {
  // 检查播放列表是否为空
  if (!stplayListStore.playList || stplayListStore.playList.length === 0) {
    window.$message.warning("播放列表为空")
    return
  }

  // 检查索引是否有效
  if (index < 0 || index >= stplayListStore.playList.length) {
    window.$message.error("无效的歌曲索引")
    return
  }
  stplayListStore.setPlayIndex(index)
  window.$message.success("开始播放歌曲")
};

// 处理列表项点击事件
const handleItemClick = (index) => {
  // 如果点击的是当前正在播放的歌曲
  if (index === stplayListStore.playIndex) {
    showSongDetail()
    window.$message.info("正在播放点击成功");
  } else {
    // 如果点击的是其他歌曲，则播放该歌曲
    playSong(index);
  }
};

// 显示歌曲详情
const showSongDetail = () => {
  showMusicDetail.value = true;
};

// 隐藏歌曲详情
const hideSongDetail = () => {
  showMusicDetail.value = false;
};

// 清空播放列表
const clearPlayList = () => {
  stplayListStore.clearPlayList();
  nowplayList.value = [];
  nowPlay.value = {
    pic:"https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png"
  };

  // 重置音频元素
  if (audioElement.value) {
    // 暂停播放
    audioElement.value.pause();
    // 清空src
    audioElement.value.src = '';
    // 重置当前时间
    audioElement.value.currentTime = 0;
  }

  // 重置播放状态
  stplayListStore.setIsPlaying(false);
  stplayListStore.setCurrentTime(0);
  stplayListStore.setTotalTime(0);
  stplayListStore.setMusicUrl('');
  // 重置当前播放索引为-1（表示没有正在播放的歌曲）
  stplayListStore.playIndex = -1;

  window.$message.success("播放列表已清空");
};

// 音频事件处理函数
const handleLoadedMetadata = () => {
  if (audioElement.value) {
    stplayListStore.setTotalTime(audioElement.value.duration)
  }
}

const handleTimeUpdate = () => {
  if (audioElement.value) {
    stplayListStore.setCurrentTime(audioElement.value.currentTime)
  }
}

const handlePlay = () => {
  stplayListStore.setIsPlaying(true)
}

const handlePause = () => {
  stplayListStore.setIsPlaying(false)
}

const handleEnded = () => {
  stplayListStore.setIsPlaying(false)
  stplayListStore.setCurrentTime(0)
  // 自动播放下一首
  stplayListStore.playNext()
}

const handleError = (e) => {
  // 如果播放列表为空，则不显示错误（可能是清空播放列表导致的正常现象）
  if (!stplayListStore.playList || stplayListStore.playList.length === 0) {
    console.log("播放列表为空，忽略音频错误");
    return;
  }

  console.error("音频播放出错:", e);
  // 检查 e 是否存在以及是否有具体的错误信息
  if (e && e.target && e.target.error) {
    switch (e.target.error.code) {
      case e.target.error.MEDIA_ERR_ABORTED:
        window.$message.error("媒体播放被中止");
        break;
      case e.target.error.MEDIA_ERR_NETWORK:
        window.$message.error("网络错误导致媒体加载失败");
        break;
      case e.target.error.MEDIA_ERR_DECODE:
        window.$message.error("媒体解码失败");
        break;
      case e.target.error.MEDIA_ERR_SRC_NOT_SUPPORTED:
        window.$message.error("不支持的媒体源或格式");
        break;
      default:
        window.$message.error("音频播放出错，请检查网络或文件格式");
        break;
    }
  } else {
    window.$message.error("音频播放出错，请检查网络或文件格式");
  }

  // 确保播放状态被正确设置为停止
  stplayListStore.setIsPlaying(false);
}

// 监听音乐URL变化，更新音频元素的src
watch(() => stplayListStore.musicUrl, (newUrl) => {
  console.log("音乐URL变化:", newUrl)

  if (audioElement.value) {
    // 检查URL是否有效
    if (!newUrl || newUrl.trim() === '') {
      console.warn("音频URL为空")
      return
    }

    audioElement.value.src = newUrl

    // 如果有自动播放标志，则开始播放
    if (stplayListStore.shouldAutoPlay) {
      nextTick(() => {
        // 添加一个小延迟确保src已更新
        setTimeout(() => {
          playAudio()
        }, 100)
      })
      stplayListStore.shouldAutoPlay = false
    }
  }
})

// 播放音频
const playAudio = () => {
  // 检查播放列表是否为空
  if (!stplayListStore.playList || stplayListStore.playList.length === 0) {
    console.warn("播放列表为空，无法播放")
    window.$message.warning("播放列表为空")
    stplayListStore.setIsPlaying(false)
    return
  }

  // 检查当前播放索引是否有效
  if (stplayListStore.playIndex < 0 || stplayListStore.playIndex >= stplayListStore.playList.length) {
    console.warn("当前播放索引无效")
    window.$message.error("当前播放索引无效")
    stplayListStore.setIsPlaying(false)
    return
  }

  // 检查音乐URL是否有效
  if (!stplayListStore.musicUrl || stplayListStore.musicUrl.trim() === '') {
    const currentSong = stplayListStore.playList[stplayListStore.playIndex]
    if (!currentSong || !currentSong.url || currentSong.url.trim() === '') {
      window.$message.error("没有可播放的音频文件")
      stplayListStore.setIsPlaying(false)
      return
    } else {
      // 如果store中的URL为空，但歌曲对象中有URL，则更新store
      stplayListStore.setMusicUrl(currentSong.url)
      console.log("从当前歌曲对象中获取URL:", currentSong.url)
    }
  }

  if (audioElement.value && stplayListStore.musicUrl) {
    console.log("尝试播放音频:", stplayListStore.musicUrl)

    audioElement.value.play().catch(error => {
      console.error("播放失败:", error)
      let errorMsg = "播放失败"

      if (error.name === 'NotAllowedError') {
        errorMsg = "浏览器不允许自动播放，请手动点击播放"
      } else if (error.name === 'NotSupportedError') {
        errorMsg = "不支持的音频格式"
      } else {
        errorMsg = "播放失败: " + error.message
      }

      window.$message.error(errorMsg)
      stplayListStore.setIsPlaying(false)
    })
  } else {
    console.warn("无法播放音频: audio元素或URL不存在")
    window.$message.error("播放组件未正确初始化")
  }
}

// 暂停音频
const pauseAudio = () => {
  if (audioElement.value) {
    audioElement.value.pause()
  }
}

// 切换播放/暂停状态
const toggleAudio = () => {
  if (stplayListStore.isPlaying) {
    pauseAudio()
  } else {
    playAudio()
  }
}

// 跳转到指定时间
const seekAudio = (time) => {
  if (audioElement.value) {
    audioElement.value.currentTime = time
  }
}

// 添加一个标志来标识是否是用户触发的跳转
let isUserSeeking = false

// 修改seekTo方法，添加标志
const seekTo = (time) => {
  isUserSeeking = true
  seekAudio(time)
  // 延迟重置标志，确保跳转操作完成
  setTimeout(() => {
    isUserSeeking = false
  }, 100)
}

// 设置音量
const setAudioVolume = (volume) => {
  if (audioElement.value) {
    audioElement.value.volume = volume
  }
}

// 监听播放状态变化
watch(() => stplayListStore.isPlaying, (isPlaying) => {
  if (isPlaying) {
    playAudio()
  } else {
    pauseAudio()
  }
})

// 监听音量变化
watch(() => stplayListStore.volume, (volume) => {
  setAudioVolume(volume)
})

// 监听跳转时间变化
watch(() => stplayListStore.currentTime, (time) => {
  // 使用一个简单的条件来避免不必要的跳转
  // 只有当audio元素的当前时间和store中的时间差异较大时才跳转
  if (audioElement.value && Math.abs(audioElement.value.currentTime - time) > 0.5) {
    seekAudio(time)
  }
})

// 监听播放列表变化，如果列表为空则重置播放状态
watch(() => stplayListStore.playList, (newPlayList) => {
  nowplayList.value = [...newPlayList]
  console.log(`播放列表已更新：${JSON.stringify(nowplayList.value)}`)

  // 如果播放列表为空，重置播放状态
  if (!newPlayList || newPlayList.length === 0) {
    stplayListStore.setIsPlaying(false)
    stplayListStore.setCurrentTime(0)
    stplayListStore.setTotalTime(0)
    stplayListStore.setPlayIndex(-1)
  }

  // 在下次DOM更新后滚动到当前播放的歌曲
  nextTick(() => {
    scrollToCurrentSong()
  })
})

// 创建一个新的 ref 用于约束 Motion 组件
const motionElementRef = ref(null)
// 使用绝对坐标体系（相对于视口）
const motionPosition = ref({
  x: (stconfigInfoStore.motionPosition.x !== undefined) ? stconfigInfoStore.motionPosition.x : window.innerWidth - 105,
  y: (stconfigInfoStore.motionPosition.y !== undefined) ? stconfigInfoStore.motionPosition.y : window.innerHeight - 105
})

// 添加实时位置信息
// const motionLivePosition = ref({ x: 0, y: 0 })
let isDragging = ref(false)
// 添加一个 ref 来控制 popover 的显示
const showPopover = ref(true)

// 处理拖拽过程中的位置更新
const handleDrag = (event) => {
  if (!isDragging.value) return;

  const x = event.clientX - 35; // 减去组件宽度的一半
  const y = event.clientY - 35; // 减去组件高度的一半

  // 更新实时位置信息
  // motionLivePosition.value = {
  //   x: event.clientX,
  //   y: event.clientY
  // };

  // 直接更新元素位置
  if (motionElementRef.value) {
    motionElementRef.value.style.transform = `translate(${x}px, ${y}px)`;
  }
}

// 处理拖拽开始
const handleDragStart = (event) => {
  isDragging.value = true;
  showPopover.value = false; // 开始拖拽时隐藏 popover
  // 阻止默认行为
  event.preventDefault();
}

// 处理拖拽结束
const handleDragEnd = (event) => {
  if (!isDragging.value) return;

  isDragging.value = false;
  // 延迟显示 popover，避免拖拽结束后立即显示
  setTimeout(() => {
    showPopover.value = true;
  }, 100);

  // 计算最终位置
  const position = {
    x: event.clientX - 35,  // 减去组件宽度的一半
    y: event.clientY - 35   // 减去组件高度的一半
  };

  // 更新位置状态
  motionPosition.value = position;

  // 保存位置到配置存储
  stconfigInfoStore.setMotionPosition(position);

  // 更新元素位置
  if (motionElementRef.value) {
    motionElementRef.value.style.transform = `translate(${position.x}px, ${position.y}px)`;
  }

  // 重置实时位置信息显示状态
  // setTimeout(() => {
  //   motionLivePosition.value = { x: 0, y: 0 };
  // }, 1000);
};

// 添加鼠标事件监听器
onMounted(() => {
  // 添加全局鼠标事件监听器
  document.addEventListener('mousemove', handleDrag);
  document.addEventListener('mouseup', handleDragEnd);

  // 确保初始位置在屏幕内
  if (motionPosition.value.x < 35) motionPosition.value.x = 35;
  if (motionPosition.value.y < 35) motionPosition.value.y = 35;
  if (motionPosition.value.x > window.innerWidth - 105) motionPosition.value.x = window.innerWidth - 105;
  if (motionPosition.value.y > window.innerHeight - 105) motionPosition.value.y = window.innerHeight - 105;

  // 设置初始位置
  if (motionElementRef.value) {
    motionElementRef.value.style.transform = `translate(${motionPosition.value.x}px, ${motionPosition.value.y}px)`;
  }
})

// 移除事件监听器
onUnmounted(() => {
  document.removeEventListener('mousemove', handleDrag);
  document.removeEventListener('mouseup', handleDragEnd);
})

// 添加鼠标悬停事件处理函数
const handleMouseEnter = (event) => {
  if (!isDragging.value) {
    event.currentTarget.style.transform = `translate(${motionPosition.value.x}px, ${motionPosition.value.y}px) scale(1.3)`;
  }
}

const handleMouseLeave = (event) => {
  event.currentTarget.style.transform = `translate(${motionPosition.value.x}px, ${motionPosition.value.y}px) scale(1)`;
}

</script>
<template>

  <n-flex vertical    >
    <TopWitge />
    <router-view />
    <!-- 隐藏的音频元素 -->
    <audio
      ref="audioElement"
      :src="stplayListStore.musicUrl"
      preload="auto"
      @loadedmetadata="handleLoadedMetadata"
      @timeupdate="handleTimeUpdate"
      @play="handlePlay"
      @pause="handlePause"
      @ended="handleEnded"
      @error="handleError"
    ></audio>


    <!-- 歌曲详情弹窗 -->
    <PlayMusic
      :song-info="nowPlay"
      :visible="showMusicDetail"
      @close="hideSongDetail"
      @seek="seekTo"
    />
    <div    v-if="nowplayList.length > 0" style="position: fixed; top: 0; left: 0; right: 0; bottom: 0; pointer-events: none; z-index: 999;">      <div
          ref="motionElementRef"
          style="width: 60px; height: 60px; cursor: grab; pointer-events: auto; position: fixed; top: 0; left: 0; transition: transform 0.3s ease;"
          :style="{ transform: `translate(${motionPosition.x}px, ${motionPosition.y}px)` }"
          @mousedown="handleDragStart"
          @mouseenter="handleMouseEnter"
          @mouseleave="handleMouseLeave"
      >
      <div style="width: 100%; height: 100%; position: relative;">
        <n-popover  placement="bottom"
                    trigger="hover"
                    :show-arrow="false"
                    :disabled="!showPopover">
          <template #trigger>
            <n-avatar
                size="large"
                :round="true"
                :src="nowPlay.pic||'https://h5static.kuwo.cn/upload/image/4f768883f75b17a426c95b93692d98bec7d3ee9240f77f5ea68fc63870fdb050.png'"
                :class="[
            'rotating-avatar',
            { 'paused': !stplayListStore.isPlaying }
          ]"
                @error="handleImageError"                  style="width: 100%; height: 100%;"
            />
          </template>
            <n-card  size="small" style="width: 500px;cursor:default">

              <template #header>
                <n-popover trigger="hover">
                  <template #trigger>
                    <n-h2>
                      播放列表
                    </n-h2>
                  </template>
                  <span>空格 - 暂停/播放，P - 上一曲，N - 下一曲</span>
                </n-popover>
              </template>
              <template #header-extra>
                <n-button @click="clearPlayList">
                  清空播放列表
                </n-button>
              </template>

              <n-list ref="listContainerRef" class="list-container" hoverable clickable>
                <n-list-item v-for="(item, index) in nowplayList" :key="item.id || index" @click="() => handleItemClick(index)">
                  <n-thing>

                    <template #avatar>
                      <n-avatar
                          size="large"
                          :round="true"
                          :src="item.pic || ''"
                          :class="{ 'rotating-avatar': index === stplayListStore.playIndex && stplayListStore.isPlaying }"
                          @error="handleImageError"
                      />
                    </template>

                    <template #description>
                      <n-ellipsis style="max-width: 140px">
                        {{ Array.isArray(item.artistName) ? item.artistName.join(', ') : item.artistName || '未知歌手' }}
                        <template #tooltip>
                          <div style="text-align: center">
                            {{ Array.isArray(item.artistName) ? item.artistName.join(', ') : item.artistName || '未知歌手' }}
                          </div>
                        </template>
                      </n-ellipsis>
                    </template>

                    <template #header>
                      <n-ellipsis style="max-width: 140px">
                        {{ item.name || '未知歌曲' }}
                        <template #tooltip>
                          <div style="text-align: center">
                            {{ item.name || '未知歌曲' }}
                          </div>
                        </template>
                      </n-ellipsis>
                    </template>

                    <template #header-extra>
                      <n-ellipsis style="max-width: 140px">
                        {{ item.albumName || '未知专辑' }}
                        <template #tooltip>
                          <div style="text-align: center">
                            {{ item.albumName || '未知专辑' }}
                          </div>
                        </template>
                      </n-ellipsis>
                    </template>
                  </n-thing>

                  <template #suffix>
                    <n-tag v-if="index !== stplayListStore.playIndex" @click.stop="() => playSong(index)" style="cursor: pointer;">播放</n-tag>
                    <n-tag v-else-if="stplayListStore.isPlaying" @click.stop="() => toggleAudio()" style="cursor: pointer;" type="warning">暂停</n-tag>
                    <n-tag v-else @click.stop="() => toggleAudio()" style="cursor: pointer;" type="success">播放</n-tag>
                  </template>
                </n-list-item>
              </n-list>
            </n-card>
          </n-popover>

          <!-- 添加实时位置信息显示（使用绝对坐标） -->
<!--          <div-->
<!--            v-show="isDragging"-->
<!--            style="position: absolute; bottom: -40px; left: 50%; transform: translateX(-50%);-->
<!--                   background: rgba(0, 0, 0, 0.7); color: white; padding: 4px 8px; border-radius: 4px;-->
<!--                   font-size: 12px; white-space: nowrap;">-->
<!--            Pos: {{ Math.round(motionLivePosition.x) }}, {{ Math.round(motionLivePosition.y) }}-->
<!--          </div>-->
        </div>
      </div>
    </div>
  </n-flex>




</template>
<style >
.always-top-button {
  z-index: 9999 !important;
}
::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 0;
}

::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  cursor: pointer;
  border-radius: 5px;
  background: rgba(0, 0, 0, 0.15);
  transition: color 0.2s ease;
}

.menu-center-wrapper {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 10000;
}

.n-float-button__menu {
  transform: translate(-30%, -0%) !important;
}
.list-container {
  max-height: 300px;
  overflow-y: auto;
}
</style>
<style>
/* 添加旋转动画 */
.rotating-avatar {
  animation: rotate 5s linear infinite;
  animation-play-state: running;
}

.rotating-avatar.paused {
  animation-play-state: paused;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
