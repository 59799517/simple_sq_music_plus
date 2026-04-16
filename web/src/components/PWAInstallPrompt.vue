<template>
  <div v-if="deferredPrompt && showInstallPrompt" class="pwa-install-prompt">
    <div class="prompt-content">
      <p>将应用安装到您的设备以获得更好的体验！</p>
      <div class="prompt-actions">
        <button @click="installPWA">安装</button>
        <button @click="dismissPrompt">稍后再说</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const deferredPrompt = ref(null);
const showInstallPrompt = ref(false);

// 监听 beforeinstallprompt 事件
onMounted(() => {
  window.addEventListener('beforeinstallprompt', (e) => {
    // 阻止默认的安装提示
    e.preventDefault();
    // 保存事件以便稍后使用
    deferredPrompt.value = e;
    // 显示我们的自定义安装提示
    showInstallPrompt.value = true;
  });
});

// 安装PWA
const installPWA = () => {
  if (deferredPrompt.value) {
    // 显示安装提示
    deferredPrompt.value.prompt();
    // 等待用户响应
    deferredPrompt.value.userChoice.then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('用户接受了PWA安装');
      } else {
        console.log('用户拒绝了PWA安装');
      }
      // 重置 deferredPrompt 变量
      deferredPrompt.value = null;
      // 隐藏安装提示
      showInstallPrompt.value = false;
    });
  }
};

// 隐藏安装提示
const dismissPrompt = () => {
  showInstallPrompt.value = false;
  // 可以选择保存用户的选择，以便在一段时间后再次提示
  localStorage.setItem('pwaPromptDismissed', Date.now().toString());
};
</script>

<style scoped>
.pwa-install-prompt {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #333;
  color: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  z-index: 1000;
  max-width: 90%;
  width: 400px;
}

.prompt-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.prompt-content p {
  margin: 0 0 16px 0;
  text-align: center;
}

.prompt-actions {
  display: flex;
  gap: 12px;
}

.prompt-actions button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.prompt-actions button:first-child {
  background-color: #42b983;
  color: white;
}

.prompt-actions button:last-child {
  background-color: #666;
  color: white;
}
</style>