<script setup>
import {
  getAllSet,
  updateConfig,
  getAliyunAuthUrl,
  saveAliyunAuthCode,
  checkAccessToken,
  getAndSetUserInfo,
  checkFolder,
  getDefaultSavePath,
  autoCreateFolder,
  syncOnce,
  incrementalSync,
  queryAllUploadFile,
  queryAllUploadFileTree
} from "../utils/api.js";
import { ref, onMounted, computed, h } from 'vue';
import configInfoStore from "../stores/config";
import {storeToRefs} from "pinia";

const stconfigInfoStore = configInfoStore();
const { data: configData } = storeToRefs(stconfigInfoStore);

// 当前激活的步骤
const currentStep = ref(1);

// 当前激活的 Tab
const activeTab = ref('dataQuery');

// 上传相关状态
let uploading = ref(false);
let uploadType = ref(''); // 'incremental' 或 'full'

// 已上传文件列表相关状态
let fileListLoading = ref(false);
let fileListData = ref([]);
let fileListTotal = ref(0);
let treeListLoading = ref(false);
let treeListData = ref([]);
let treeListTotal = ref(0);
let fileViewMode = ref('tree'); // 'list' 或 'tree'，默认文件夹视图

// 表格列显示控制
let showExtraColumns = ref(false); // 是否显示额外字段

// 文件夹浏览相关状态
let currentFolderPath = ref(''); // 当前文件夹路径
let folderBreadcrumbs = ref([]); // 面包屑导航
let currentFolderContents = ref([]); // 当前文件夹内容

// 步骤状态计算
const stepsStatus = computed(() => {
  const status = [];
  
  // 获取每个配置的 remark - 从 configData 中查找
  const getRemark = (key) => {
    if (!configData.value || !Array.isArray(configData.value)) {
      console.log('configData 为空:', configData.value);
      return '';
    }
    const config = configData.value.find(item => item && item.configKey === key);
    const remark = (config && config.configRemark) ? config.configRemark : '';
    console.log(`getRemark(${key}):`, remark);
    return remark;
  };
  
  // 步骤 1: AppID 检查
  status.push({
    title: 'AppID 配置',
    description: appIdValue.value ? '已配置' : '未配置',
    status: appIdValue.value ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.appid')
  });
  
  // 步骤 2: 获取授权码链接
  status.push({
    title: '获取授权链接',
    description: accessTokenValue.value ? '已完成' : '待获取',
    status: accessTokenValue.value ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.authorization.url')
  });
  
  // 步骤 3: 授权码验证
  status.push({
    title: '授权码验证',
    description: accessTokenValue.value ? '已完成' : '待验证',
    status: accessTokenValue.value ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.auth_code')
  });
  
  // 步骤 4: 授权信息查看
  status.push({
    title: '授权信息',
    description: tokenExpireTimeValue.value ? '正常' : (accessTokenValue.value ? '检查中' : '待检查'),
    status: tokenExpireTimeValue.value ? 'finish' : (accessTokenValue.value ? 'process' : 'wait'),
    remark: getRemark('expand.aliyun.access_token.expire.time')
  });
  
  // 步骤 5: 获取用户信息
  status.push({
    title: '用户信息',
    description: (userName.value && backupDriveId.value && resourceDriveId.value) ? '已获取' : '未获取',
    status: (userName.value && backupDriveId.value && resourceDriveId.value) ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.user.name')
  });
  
  // 步骤 6: 保存位置设置
  status.push({
    title: '保存位置',
    description: saveLocation.value ? '已设置' : '待设置',
    status: saveLocation.value ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.folder.path')
  });
  
  // 步骤 7: 选择同步模式
  status.push({
    title: '同步模式',
    description: syncMode.value ? '已设置' : '待选择',
    status: syncMode.value ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.sync.mode')
  });
  
  // 步骤 8: 开启/关闭同步
  status.push({
    title: '同步开关',
    description: isOpen.value ? '已开启' : '已关闭',
    status: isOpen.value ? 'finish' : 'wait',
    remark: getRemark('expand.aliyun.open')
  });
  
  return status;
});

// 检查上一步是否完成
const checkPreviousStep = (stepIndex) => {
  if (stepIndex === 1) return true;
  
  const previousStep = stepIndex - 1;
  
  if (previousStep === 1) return !!appIdValue.value;
  if (previousStep === 2) return !!accessTokenValue.value;
  if (previousStep === 3) return !!accessTokenValue.value;
  if (previousStep === 4) return !!tokenExpireTimeValue.value || !!accessTokenValue.value;
  if (previousStep === 5) return !!(userName.value && backupDriveId.value && resourceDriveId.value);
  if (previousStep === 6) return !!saveLocation.value;
  if (previousStep === 7) return !!syncMode.value;
  
  return false;
};

// 跳转到指定步骤
const jumpToStep = (stepIndex) => {
  if (!checkPreviousStep(stepIndex)) {
    window.$message.warning('请先完成上一步骤');
    return;
  }
  
  currentStep.value = stepIndex;
  // 滚动到对应的卡片位置
  const cards = document.querySelectorAll('.step-card');
  if (cards[stepIndex - 1]) {
    cards[stepIndex - 1].scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
};

// 阿里云盘配置数据
let aliyunConfig = ref([]);
let loading = ref(false);

// 授权码相关
let authCode = ref('');
let gettingAuthUrl = ref(false);
let savingAuthCode = ref(false);
let verifyingAuthCode = ref(false);
let checkingToken = ref(false);
let gettingUserInfo = ref(false);
let appIdValue = ref('');
let savingAppId = ref(false);
let authUrlValue = ref('');
let codeVerifierValue = ref('');
let accessTokenValue = ref('');
let tokenExpireTimeValue = ref('');
// 用户信息
let backupDriveId = ref('');
let resourceDriveId = ref('');
let userName = ref('');
let userId = ref('');
let avatar = ref('');
let userInfoName = ref('');
let nickName = ref('');
// 保存位置
let saveLocation = ref('');
let savingLocation = ref(false);
// 同步模式
let syncMode = ref('');
let syncOptions = ref([]);
// 同步开关
let isOpen = ref(false);

// 加载阿里云盘配置
const loadAliyunConfig = async () => {
  loading.value = true;
  try {
    const value = await getAllSet();
    console.log("获取全部设置:", value.data);
    
    if (value.data.code === 200) {
      stconfigInfoStore.setData(value.data.data);
      
      // 筛选出 expand.aliyun 开头的配置项
      aliyunConfig.value = configData.value.filter(item => {
        const stringsKey = item.configKey.split(".");
        return stringsKey[0] === "expand" && stringsKey[1] === "aliyun";
      });
      
      // 检查 appid 配置
      const appidConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.appid');
      if (appidConfig && appidConfig.configValue) {
        appIdValue.value = appidConfig.configValue;
      } else {
        appIdValue.value = '';
      }
      
      // 检查 access_token 和到期时间
      const tokenConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.access_token');
      if (tokenConfig && tokenConfig.configValue) {
        accessTokenValue.value = tokenConfig.configValue;
      } else {
        accessTokenValue.value = '';
      }
      
      const expireTimeConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.access_token.expire.time');
      if (expireTimeConfig && expireTimeConfig.configValue) {
        tokenExpireTimeValue.value = expireTimeConfig.configValue;
      } else {
        tokenExpireTimeValue.value = '';
      }
      
      // 获取用户信息
      const backupDriveIdConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('backup.drive_id'));
      if (backupDriveIdConfig && backupDriveIdConfig.configValue) {
        backupDriveId.value = backupDriveIdConfig.configValue;
      } else {
        backupDriveId.value = '';
        console.log('未找到 backup.drive_id');
      }
      
      const resourceDriveIdConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('resource.drive_id'));
      if (resourceDriveIdConfig && resourceDriveIdConfig.configValue) {
        resourceDriveId.value = resourceDriveIdConfig.configValue;
      } else {
        resourceDriveId.value = '';
        console.log('未找到 resource.drive_id');
      }
      
      const userNameConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('user.name'));
      if (userNameConfig && userNameConfig.configValue) {
        userName.value = userNameConfig.configValue;
      } else {
        userName.value = '';
        console.log('未找到 user.name');
      }
      
      const userIdConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('user.id'));
      if (userIdConfig && userIdConfig.configValue) {
        userId.value = userIdConfig.configValue;
      } else {
        userId.value = '';
        console.log('未找到 user.id');
      }
      
      // 获取头像和账号信息
      const avatarConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('.avatar'));
      if (avatarConfig && avatarConfig.configValue) {
        avatar.value = avatarConfig.configValue;
      } else {
        avatar.value = '';
        console.log('未找到 avatar');
      }
      
      const userInfoNameConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('user.info.name'));
      if (userInfoNameConfig && userInfoNameConfig.configValue) {
        userInfoName.value = userInfoNameConfig.configValue;
      } else {
        userInfoName.value = '';
        console.log('未找到 user.info.name');
      }
      
      const nickNameConfig = aliyunConfig.value.find(item => item.configKey && item.configKey.includes('nick.name'));
      if (nickNameConfig && nickNameConfig.configValue) {
        nickName.value = nickNameConfig.configValue;
      } else {
        nickName.value = '';
        console.log('未找到 nick.name');
      }
      
      // 获取保存位置配置
      const saveLocationConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.folder.path');
      if (saveLocationConfig && saveLocationConfig.configValue) {
        saveLocation.value = saveLocationConfig.configValue;
      } else {
        saveLocation.value = '';
      }
      
      // 获取同步模式配置
      const syncModeConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.sync.mode');
      if (syncModeConfig && syncModeConfig.configValue) {
        syncMode.value = syncModeConfig.configValue;
      } else {
        syncMode.value = '';
      }
      
      // 获取同步模式的选项
      const syncModeOptionsConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.sync.mode' && item.configOptions);
      if (syncModeOptionsConfig && syncModeOptionsConfig.configOptions) {
        try {
          syncOptions.value = JSON.parse(syncModeOptionsConfig.configOptions);
        } catch (e) {
          console.error('解析同步模式选项失败:', e);
          syncOptions.value = [];
        }
      }
      
      // 获取同步开关状态
      const openConfig = aliyunConfig.value.find(item => item.configKey === 'expand.aliyun.open');
      if (openConfig && openConfig.configValue) {
        isOpen.value = openConfig.configValue === 'true' || openConfig.configValue === true;
      } else {
        isOpen.value = false;
      }
      
      console.log("用户信息汇总:", {
        backupDriveId: backupDriveId.value,
        resourceDriveId: resourceDriveId.value,
        userName: userName.value,
        userId: userId.value,
        avatar: avatar.value,
        userInfoName: userInfoName.value,
        nickName: nickName.value
      });
      
      console.log("阿里云盘配置:", aliyunConfig.value);
      
      console.log("阿里云盘配置:", aliyunConfig.value);
      
      if (aliyunConfig.value.length === 0) {
        // 不显示任何提示
      }
    } else {
      window.$message.error("获取设置失败：" + value.data.msg);
    }
  } catch (error) {
    console.error("加载配置失败:", error);
    window.$message.error("加载配置失败，请稍后重试");
  } finally {
    loading.value = false;
  }
};

// 弹出框相关
let showModal = ref(false);
let tempModalTile = ref("");
let tempModalRemark = ref("");
let tempModalType = ref("");
let tempModalValue = ref("");
let tempModalKey = ref("");
let tempModalKeyNullCheck = ref(0);
let tempModalDisabled = ref(0);
let tempOptions = ref([]);

// 关闭弹窗
const closeDialog = () => {
  showModal.value = false;
  tempModalTile.value = '';
  tempModalRemark.value = '';
  tempModalType.value = '';
  tempModalValue.value = '';
  tempModalKey.value = '';
  tempModalKeyNullCheck.value = 0;
  tempModalDisabled.value = 0;
  tempOptions.value = [];
};

// 打开弹窗
const openModal = (item) => {
  showModal.value = true;
  tempModalTile.value = item.configName;
  tempModalRemark.value = item.configRemark;
  tempModalType.value = item.configType;
  
  if (item.configType === "number") {
    tempModalValue.value = Number(item.configValue);
  } else if (item.configType === "boolean") {
    tempModalValue.value = JSON.parse(item.configValue);
  } else if (item.configType === "select") {
    tempModalValue.value = item.configValue;
    try {
      tempOptions.value = JSON.parse(item.configOptions);
    } catch (e) {
      console.error('解析 configOptions 失败:', e);
      tempOptions.value = [];
    }
  } else {
    tempModalValue.value = item.configValue;
  }
  
  tempModalKeyNullCheck.value = item.configNullCheck;
  tempModalDisabled.value = item.configDisabled;
  tempModalKey.value = item.configKey;
};

// 处理卡片点击
const handleCardClick = (item) => {
  if (item.configDisabled === 1) {
    window.$message.warning("该配置项不允许修改");
    return;
  }
  openModal(item);
};

// 保存设置
const setConfig = async () => {
  // 校验必填项
  if (tempModalKeyNullCheck.value === 1) {
    if (tempModalType.value === 'boolean') {
      if (tempModalValue.value === null || tempModalValue.value === undefined) {
        window.$message.error("请填写内容是或者否");
        return;
      }
    }
    if (tempModalType.value === 'number') {
      if (tempModalValue.value < 0) {
        window.$message.error("数值类型必须大于等于 0");
        return;
      }
    }
    if (tempModalType.value === 'input' || tempModalType.value === 'path' || tempModalType.value === 'password') {
      if (tempModalValue.value === '' || tempModalValue.value === null || tempModalValue.value === undefined) {
        window.$message.error("请填写内容");
        return;
      }
    }
  }
  
  // 校验 select 类型的值
  if (tempModalType.value === 'select') {
    if (!tempOptions.value || tempOptions.value.length === 0) {
      window.$message.error("该配置项没有有效的选项列表，请检查数据");
      return;
    }
    const validValues = tempOptions.value.map(opt => opt.value);
    if (!validValues.includes(tempModalValue.value)) {
      window.$message.error(`选择的值 "${tempModalValue.value}" 不在允许的选项中，请重新选择`);
      return;
    }
  }
  
  try {
    const value = await updateConfig(tempModalKey.value, tempModalValue.value);
    if (value.data.code === 200) {
      window.$message.success("修改成功!");
      await loadAliyunConfig();
      closeDialog();
    } else {
      window.$message.error("修改失败：" + value.data.msg);
    }
  } catch (error) {
    console.error("保存配置失败:", error);
    window.$message.error("保存失败，请稍后重试");
  }
};

// 页面加载时获取数据
onMounted(() => {
  loadAliyunConfig();
  // 默认进入数据查询的文件夹视图，自动加载根目录
  loadRootFolder();
});

// 获取授权 URL 并展示
const getAuthUrl = async () => {
  gettingAuthUrl.value = true;
  authUrlValue.value = ''; // 清空之前的 URL
  codeVerifierValue.value = ''; // 清空之前的 code_verifier
  try {
    const value = await getAliyunAuthUrl();
    if (value.data.code === 200) {
      // code == 200 时正常处理，从 data 中获取 URL 和 code_verifier
      const responseData = value.data.data;
      const authUrl = responseData.location;
      const codeVerifier = responseData.code_verifier;
      
      if (!authUrl) {
        window.$message.error('获取授权 URL 失败：返回数据为空');
        return;
      }
      if (!codeVerifier) {
        window.$message.error('获取 code_verifier 失败：返回数据为空');
        return;
      }
      
      authUrlValue.value = authUrl; // 显示 URL
      codeVerifierValue.value = codeVerifier; // 保存 code_verifier
      console.log('授权 URL 已设置:', authUrlValue.value);
      window.$message.success('授权链接已获取，请点击下方链接完成授权，然后将授权码填写到下方输入框中');
    } else {
      // 非 200 则提示 msg 中的内容
      window.$message.error(value.data.msg || '获取授权 URL 失败');
    }
  } catch (error) {
    console.error('获取授权 URL 失败:', error);
    window.$message.error('获取授权 URL 失败，请稍后重试');
  } finally {
    gettingAuthUrl.value = false;
  }
};

// 打开授权链接
const openAuthUrl = () => {
  if (!authUrlValue.value) {
    window.$message.warning('请先获取授权链接');
    return;
  }
  window.open(authUrlValue.value, '_blank');
};

// 验证授权码
const verifyAuthCode = async () => {
  // 校验是否输入授权码
  if (!authCode.value || authCode.value.trim() === '') {
    window.$message.warning('请填写授权码后再进行操作');
    return;
  }
  
  // 校验是否有 code_verifier
  if (!codeVerifierValue.value) {
    window.$message.warning('请先获取授权链接');
    return;
  }
  
  verifyingAuthCode.value = true;
  try {
    const value = await saveAliyunAuthCode(authCode.value.trim(), codeVerifierValue.value);
    if (value.data.code === 200) {
      window.$message.success('授权成功！');
      authCode.value = ''; // 清空输入框
      await loadAliyunConfig(); // 重新加载配置
      console.log('AccessToken 已设置:', accessTokenValue.value);
    } else {
      // 返回 code 500 失败则打印 msg 消息
      window.$message.error(value.data.msg || '授权码验证失败');
    }
  } catch (error) {
    console.error('授权码验证失败:', error);
    window.$message.error('授权码验证失败，请稍后重试');
  } finally {
    verifyingAuthCode.value = false;
  }
};

// 检查 access_token 是否有效
const checkTokenValid = async () => {
  checkingToken.value = true;
  try {
    const value = await checkAccessToken();
    if (value.data.code === 200) {
      window.$message.success('access_token 有效！');
      await loadAliyunConfig(); // 重新加载配置
    } else {
      window.$message.error(value.data.msg || 'access_token 无效或已过期');
    }
  } catch (error) {
    console.error('检查 access_token 失败:', error);
    window.$message.error('检查 access_token 失败，请稍后重试');
  } finally {
    checkingToken.value = false;
  }
};

// 获取用户信息
const getUserInfo = async () => {
  gettingUserInfo.value = true;
  try {
    const value = await getAndSetUserInfo();
    if (value.data.code === 200) {
      window.$message.success('用户信息获取成功！');
      await loadAliyunConfig(); // 重新加载配置
    } else {
      window.$message.error(value.data.msg || '获取用户信息失败');
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
    window.$message.error('获取用户信息失败，请稍后重试');
  } finally {
    gettingUserInfo.value = false;
  }
};

// 检测路径
const checkLocationPath = async () => {
  if (!saveLocation.value || saveLocation.value.trim() === '') {
    window.$message.warning('请填写保存路径');
    return;
  }
  
  savingLocation.value = true;
  try {
    const checkValue = await checkFolder(saveLocation.value.trim());
    console.log('路径检查结果:', checkValue);
    
    if (checkValue.data.code === 200 && checkValue.data.data === true) {
      // 路径可用，询问是否同步歌曲
      const d = window.$dialog.create({
        type: 'success',
        title: '路径可用',
        content: '是否将歌曲同步到此文件夹？',
        positiveText: '确定',
        negativeText: '取消',
        onPositiveClick: async () => {
          try {
            const value = await updateConfig('expand.aliyun.folder.path', saveLocation.value.trim());
            if (value.data.code === 200) {
              window.$message.success('已保存同步路径！');
              await loadAliyunConfig();
            } else {
              window.$message.error('保存失败：' + value.data.msg);
            }
          } catch (error) {
            console.error('保存路径失败:', error);
            window.$message.error('保存失败，请稍后重试');
          }
        },
        onNegativeClick: () => {
          d.destroy();
        }
      });
    } else if ((checkValue.data.code === 200 && checkValue.data.data === false) || checkValue.data.code === 500) {
      // 创建对话框询问是否自动创建
      const d = window.$dialog.create({
        type: 'warning',
        title: '路径不可用',
        content: checkValue.data.msg || '当前路径不可用，是否自动创建文件夹？',
        positiveText: '自动创建',
        negativeText: '取消',
        onPositiveClick: async () => {
          try {
            const res = await autoCreateFolder(saveLocation.value.trim());
            if (res.data.code === 200) {
              window.$message.success('文件夹创建成功！');
            } else {
              // 创建失败，显示说明对话框
              window.$dialog.create({
                type: 'error',
                title: '创建失败',
                content: () => h('div', { style: { 'white-space': 'pre-line', 'line-height': '2' } }, [
                  h('div', { style: { 'margin-bottom': '12px', 'font-weight': 'bold' } }, '目录命名规则：'),
                  h('div', { style: { 'margin-bottom': '8px', 'color': '#18a058' } }, '✓ 资源文件夹/SqMusic/第三级名字/第四级'),
                  h('div', { style: { 'color': '#18a058' } }, '✓ 备份文件夹/随便写/随便写/复合阿里云规则就行')
                ]),
                positiveText: '知道了'
              });
            }
          } catch (error) {
            console.error('创建文件夹失败:', error);
            window.$message.error('创建文件夹失败');
          }
        },
        onNegativeClick: () => {
          d.destroy();
        }
      });
    } else {
      window.$message.error('检测失败：' + checkValue.data.msg);
    }
  } catch (error) {
    console.error('检测路径失败:', error);
    window.$message.error('检测路径失败：' + (error.message || '请稍后重试'));
  } finally {
    savingLocation.value = false;
  }
};

// 获取默认路径
const loadDefaultPath = async () => {
  savingLocation.value = true;
  try {
    const res = await getDefaultSavePath();
    if (res.data.code === 200) {
      saveLocation.value = res.data.data;
    } else {
      window.$message.error('获取默认路径失败：' + res.data.msg);
    }
  } catch (error) {
    console.error('获取默认路径失败:', error);
    window.$message.error('获取默认路径失败');
  } finally {
    savingLocation.value = false;
  }
};

// 保存同步模式
const saveSyncMode = async () => {
  if (!syncMode.value || syncMode.value.trim() === '') {
    window.$message.warning('请选择同步模式');
    return;
  }
  
  savingLocation.value = true;
  try {
    const value = await updateConfig('expand.aliyun.sync.mode', syncMode.value);
    if (value.data.code === 200) {
      window.$message.success('同步模式设置成功！');
      await loadAliyunConfig();
    } else {
      window.$message.error('设置失败：' + value.data.msg);
    }
  } catch (error) {
    console.error('保存同步模式失败:', error);
    window.$message.error('保存失败，请稍后重试');
  } finally {
    savingLocation.value = false;
  }
};

// 保存 AppID
const saveAppId = async () => {
  if (!appIdValue.value || appIdValue.value.trim() === '') {
    window.$message.warning('请填写 AppID');
    return;
  }
  
  savingAppId.value = true;
  try {
    const value = await updateConfig('expand.aliyun.appid', appIdValue.value.trim());
    if (value.data.code === 200) {
      window.$message.success('AppID 设置成功！');
      await loadAliyunConfig();
    } else {
      window.$message.error('设置失败：' + value.data.msg);
    }
  } catch (error) {
    console.error('保存 AppID 失败:', error);
    window.$message.error('保存失败，请稍后重试');
  } finally {
    savingAppId.value = false;
  }
};

// 切换同步开关
const toggleSync = async () => {
  savingLocation.value = true;
  try {
    const newValue = !isOpen.value;
    const value = await updateConfig('expand.aliyun.open', newValue.toString());
    if (value.data.code === 200) {
      window.$message.success(newValue ? '同步已开启' : '同步已关闭');
      isOpen.value = newValue;
    } else {
      window.$message.error('操作失败：' + value.data.msg);
    }
  } catch (error) {
    console.error('切换同步开关失败:', error);
    window.$message.error('操作失败，请稍后重试');
  } finally {
    savingLocation.value = false;
  }
};

// 保存授权码（保留原有功能用于高级设置）
const saveAuthCode = async () => {
  if (!authCode.value || authCode.value.trim() === '') {
    window.$message.warning('请填写授权码后再进行操作');
    return;
  }
  
  savingAuthCode.value = true;
  try {
    const value = await updateConfig('expand.aliyun.auth_code', authCode.value.trim());
    if (value.data.code === 200) {
      window.$message.success('授权码保存成功！');
      authCode.value = ''; // 清空输入框
      await loadAliyunConfig(); // 重新加载配置
    } else {
      window.$message.error('保存授权码失败：' + value.data.msg);
    }
  } catch (error) {
    console.error('保存授权码失败:', error);
    window.$message.error('保存授权码失败，请稍后重试');
  } finally {
    savingAuthCode.value = false;
  }
};

// 执行全量上传
const handleFullUpload = async () => {
  const d = window.$dialog.create({
    type: 'warning',
    title: '确认全量上传',
    content: '全量上传将扫描并上传所有歌曲到阿里云盘，此操作可能耗时较长，是否继续？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      uploading.value = true;
      uploadType.value = 'full';
      try {
        const value = await syncOnce();
        if (value.data.code === 200) {
          window.$message.success('全量上传任务已启动！');
        } else {
          window.$message.error(value.data.msg || '全量上传失败');
        }
      } catch (error) {
        console.error('全量上传失败:', error);
        window.$message.error('全量上传失败，请稍后重试');
      } finally {
        uploading.value = false;
        uploadType.value = '';
      }
    },
    onNegativeClick: () => {
      d.destroy();
    }
  });
};

// 执行增量上传
const handleIncrementalUpload = async () => {
  const d = window.$dialog.create({
    type: 'info',
    title: '确认增量上传',
    content: '增量上传将只上传新增的歌曲到阿里云盘，是否继续？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      uploading.value = true;
      uploadType.value = 'incremental';
      try {
        const value = await incrementalSync();
        if (value.data.code === 200) {
          window.$message.success('增量上传任务已启动！');
        } else {
          window.$message.error(value.data.msg || '增量上传失败');
        }
      } catch (error) {
        console.error('增量上传失败:', error);
        window.$message.error('增量上传失败，请稍后重试');
      } finally {
        uploading.value = false;
        uploadType.value = '';
      }
    },
    onNegativeClick: () => {
      d.destroy();
    }
  });
};

// 查询所有已上传的文件列表
const loadFileList = async () => {
  fileListLoading.value = true;
  try {
    const value = await queryAllUploadFile();
    if (value.data.code === 200) {
      fileListData.value = value.data.data || [];
      fileListTotal.value = fileListData.value.length;
    } else {
      window.$message.error(value.data.msg || '查询失败');
    }
  } catch (error) {
    console.error('查询文件列表失败:', error);
    window.$message.error('查询失败，请稍后重试');
  } finally {
    fileListLoading.value = false;
  }
};

// 查询树状文件列表
const loadTreeList = async () => {
  treeListLoading.value = true;
  try {
    const value = await queryAllUploadFileTree();
    if (value.data.code === 200) {
      const rawData = value.data.data || [];
      // 转换数据格式以适配 n-tree 组件
      treeListData.value = convertToTreeData(rawData);
      // 计算总节点数
      treeListTotal.value = countTreeNodes(treeListData.value);
      console.log('树状数据:', treeListData.value);
      window.$message.success(`查询成功，共 ${treeListTotal.value} 个节点`);
    } else {
      window.$message.error(value.data.msg || '查询失败');
    }
  } catch (error) {
    console.error('查询树状列表失败:', error);
    window.$message.error('查询失败，请稍后重试');
  } finally {
    treeListLoading.value = false;
  }
};

// 转换数据格式为 n-tree 所需格式
const convertToTreeData = (nodes) => {
  if (!nodes || !Array.isArray(nodes)) return [];
  
  return nodes.map((node, index) => {
    // 生成唯一 key
    const nodeKey = `${node.type}_${node.name}_${index}`;
    
    const treeNode = {
      key: nodeKey,
      label: node.name || '未知',
      type: node.type, // folder 或 file
      // 如果是文件，保留 fileInfo 信息
      ...(node.fileInfo || {})
    };
    
    // 递归处理子节点
    if (node.children && node.children.length > 0) {
      treeNode.children = convertToTreeData(node.children);
    }
    
    return treeNode;
  });
};

// 递归计算树节点总数
const countTreeNodes = (nodes) => {
  let count = 0;
  nodes.forEach(node => {
    count++;
    if (node.children && node.children.length > 0) {
      count += countTreeNodes(node.children);
    }
  });
  return count;
};

// 切换视图模式
const switchViewMode = (mode) => {
  fileViewMode.value = mode;
  if (mode === 'list' && fileListData.value.length === 0) {
    loadFileList();
  } else if (mode === 'tree') {
    // 切换到文件夹视图时，加载根目录
    loadRootFolder();
  }
};

// 加载根目录
const loadRootFolder = async () => {
  treeListLoading.value = true;
  currentFolderPath.value = '';
  folderBreadcrumbs.value = [{ label: '根目录', path: '' }];
  
  try {
    const value = await queryAllUploadFileTree();
    if (value.data.code === 200) {
      const rawData = value.data.data || [];
      // 转换数据格式
      treeListData.value = convertToTreeData(rawData);
      // 显示根目录内容
      currentFolderContents.value = treeListData.value;
      treeListTotal.value = countTreeNodes(treeListData.value);
      console.log('根目录数据:', currentFolderContents.value);
    } else {
      window.$message.error(value.data.msg || '查询失败');
    }
  } catch (error) {
    console.error('查询失败:', error);
    window.$message.error('查询失败，请稍后重试');
  } finally {
    treeListLoading.value = false;
  }
};

// 进入文件夹
const enterFolder = (folder) => {
  if (folder.type !== 'folder') return;
  
  // 添加到面包屑
  folderBreadcrumbs.value.push({
    label: folder.label,
    path: folder.key,
    data: folder
  });
  
  // 显示子内容
  currentFolderContents.value = folder.children || [];
  currentFolderPath.value = folder.key;
};

// 面包屑导航点击
const navigateToBreadcrumb = (index) => {
  if (index === 0) {
    // 返回根目录
    loadRootFolder();
  } else {
    // 返回到指定层级
    const breadcrumb = folderBreadcrumbs.value[index];
    folderBreadcrumbs.value = folderBreadcrumbs.value.slice(0, index + 1);
    currentFolderContents.value = breadcrumb.data.children || [];
    currentFolderPath.value = breadcrumb.path;
  }
};

// 返回上一级
const goBack = () => {
  if (folderBreadcrumbs.value.length > 1) {
    navigateToBreadcrumb(folderBreadcrumbs.value.length - 2);
  }
};

// 计算表格列配置
const tableColumns = computed(() => {
  const baseColumns = [
    { title: 'ID', key: 'id', width: 80 },
    { title: '文件名', key: 'name', width: 200, ellipsis: { tooltip: true } },
    { title: '本地路径', key: 'path', ellipsis: { tooltip: true } },
    { title: '阿里云盘路径', key: 'aliPath', ellipsis: { tooltip: true } },
    { title: '上传时间', key: 'uploadTime', width: 180 },
    { 
      title: '秒传', 
      key: 'rapid', 
      width: 80,
      render: (row) => {
        return row.rapid === 1 ? 
          h('n-tag', { type: 'success', size: 'small' }, { default: () => '是' }) :
          h('n-tag', { type: 'default', size: 'small' }, { default: () => '否' });
      }
    }
  ];

  // 额外字段
  if (showExtraColumns.value) {
    baseColumns.push(
      { title: 'SHA1', key: 'sha1', width: 150, ellipsis: { tooltip: true } },
      { title: 'MD5', key: 'md5', width: 150, ellipsis: { tooltip: true } },
      { title: '分片SHA1', key: 'shardingSha1', width: 150, ellipsis: { tooltip: true } },
      { title: '阿里云盘ID路径', key: 'aliIdPath', width: 150, ellipsis: { tooltip: true } },
      { title: '歌曲名称', key: 'musicName', width: 150, ellipsis: { tooltip: true } },
      { title: '歌手', key: 'musicArtist', width: 120, ellipsis: { tooltip: true } },
      { title: '专辑', key: 'musicAlbum', width: 150, ellipsis: { tooltip: true } },
      { title: '文件后缀', key: 'suffix', width: 100 },
      { title: '结果', key: 'result', width: 120, ellipsis: { tooltip: true } },
      { title: '下载表ID', key: 'downloadId', width: 100 }
    );
  }

  return baseColumns;
});
</script>

<template>
  <n-spin :show="loading">
    <n-card>

      <!-- Tabs 标签页 -->
      <n-tabs v-model:value="activeTab" type="line" animated justify-content="center">
        <!-- 阿里设置标签页 -->
        <n-tab-pane name="settings" tab="阿里设置">

      
      <!-- 授权码区域 -->
      <n-card title="阿里云盘授权配置" type="info" size="small" style="margin-bottom: 16px;">
        <n-space vertical>
          <n-alert type="warning" title="操作指引">
            请按照以下步骤顺序完成阿里云盘的授权配置。
          </n-alert>
          
          <!-- 步骤导航 -->
          <n-steps 
            v-model:current="currentStep" 
            vertical
            clickable
            @update:current="jumpToStep"
          >
            <n-step 
              v-for="(step, index) in stepsStatus" 
              :key="index"
              :title="step.title"
              :description="step.description"
              :status="step.status"
            />
          </n-steps>
        </n-space>
      </n-card>
      
      <!-- 步骤 1: AppID 配置 -->
      <n-card title="① AppID 配置" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center">
          <n-input 
            v-model:value="appIdValue" 
            placeholder="请输入阿里云盘 AppID"
            style="width: 400px;"
            :disabled="savingAppId"
          />
          <n-button 
            @click="saveAppId" 
            type="primary" 
            :loading="savingAppId"
            secondary
          >
            {{ savingAppId ? '保存中...' : '保存 AppID' }}
          </n-button>
        </n-space>
      </n-card>
      
      <!-- 步骤 2: 获取授权码链接 -->
      <n-card title="② 获取授权码链接" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center" wrap>
          <n-button 
            @click="getAuthUrl" 
            :type="authUrlValue ? 'success' : 'primary'" 
            :loading="gettingAuthUrl"
            secondary
          >
            {{ gettingAuthUrl ? '正在获取...' : '获取授权码链接' }}
          </n-button>
          <!-- 授权链接用 Tag 显示 -->
          <n-tag 
            v-if="authUrlValue" 
            type="success"
            size="large"
            bordered
            style="word-break: break-all; white-space: normal; cursor: pointer;"
            @click="openAuthUrl"
          >
            {{ authUrlValue }}
          </n-tag>
        </n-space>
      </n-card>
      
      <!-- 步骤 3: 授权码验证 -->
      <n-card title="③ 授权码验证" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center">
          <n-button 
            type="info" 
            secondary
            disabled
          >
            输入授权码
          </n-button>
          <n-input 
            v-model:value="authCode" 
            placeholder="请输入授权码"
            style="width: 300px;"
            :disabled="verifyingAuthCode"
          />
          <n-button 
            @click="verifyAuthCode" 
            type="success" 
            :loading="verifyingAuthCode"
            secondary
          >
            {{ verifyingAuthCode ? '验证中...' : '验证授权码' }}
          </n-button>
        </n-space>
      </n-card>
      
      <!-- 步骤 4: 授权信息查看 -->
      <n-card title="④ 授权信息查看" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center" wrap>
          <n-button 
            type="default" 
            secondary
            disabled
          >
            当前授权状态
          </n-button>
          <n-tag 
            v-if="tokenExpireTimeValue" 
            type="warning"
            size="large"
            bordered
          >
            到期时间：{{ tokenExpireTimeValue }}
          </n-tag>
          <n-tag 
            v-if="accessTokenValue" 
            type="success"
            size="large"
            bordered
            style="word-break: break-all; white-space: normal;"
          >
            Token: {{ accessTokenValue }}
          </n-tag>
          <n-tag 
            v-if="!accessTokenValue && !tokenExpireTimeValue" 
            type="default"
            size="large"
            bordered
          >
            暂无授权信息
          </n-tag>
          <n-button 
            @click="checkTokenValid" 
            :type="accessTokenValue ? 'primary' : 'default'"
            :loading="checkingToken"
            secondary
            style="margin-left: 10px;"
          >
            {{ checkingToken ? '检查中...' : '检查 access_token 有效性' }}
          </n-button>
        </n-space>
      </n-card>
      
      <!-- 步骤 5: 获取用户信息 -->
      <n-card title="⑤ 获取用户信息" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center" wrap>
          <n-button 
            @click="getUserInfo" 
            type="primary" 
            :loading="gettingUserInfo"
            secondary
          >
            {{ gettingUserInfo ? '获取中...' : '获取用户信息' }}
          </n-button>
          <!-- 头像显示 -->
          <n-avatar 
            v-if="avatar" 
            :src="avatar" 
            size="large"
            fallback-src="https://gw.alicdn.com/imgextra/i4/O1CN016rR7fN1cMBFbMzW8P_!!6000000003589-2-tps-200-200.png"
          />
          <n-tag 
            v-if="userInfoName" 
            type="blue"
            size="large"
            bordered
          >
            账号名称：{{ userInfoName }}
          </n-tag>
          <n-tag 
            v-if="nickName" 
            type="success"
            size="large"
            bordered
          >
            昵称：{{ nickName }}
          </n-tag>
          <n-tag 
            v-if="userName" 
            type="info"
            size="large"
            bordered
          >
            用户名：{{ userName }}
          </n-tag>
          <n-tag 
            v-if="userId" 
            type="warning"
            size="large"
            bordered
          >
            用户 ID: {{ userId }}
          </n-tag>
          <n-tag 
            v-if="backupDriveId" 
            type="error"
            size="large"
            bordered
          >
            Backup Drive ID: {{ backupDriveId }}
          </n-tag>
          <n-tag 
            v-if="resourceDriveId" 
            type="error"
            size="large"
            bordered
          >
            Resource Drive ID: {{ resourceDriveId }}
          </n-tag>
          <n-tag 
            v-if="!userName && !userId && !backupDriveId && !resourceDriveId && !avatar && !userInfoName && !nickName" 
            type="default"
            size="large"
            bordered
          >
            暂无用户信息
          </n-tag>
        </n-space>
      </n-card>
      
      <!-- 步骤 6: 保存位置设置 -->
      <n-card title="⑥ 保存位置设置" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center">
          <n-input 
            v-model:value="saveLocation" 
            placeholder="请输入阿里云盘保存路径"
            style="width: 400px;"
            :disabled="savingLocation || !checkPreviousStep(6)"
          />
          <n-button 
            v-if="saveLocation && saveLocation.trim() !== ''" 
            @click="checkLocationPath" 
            type="primary" 
            :loading="savingLocation"
            secondary
            :disabled="!checkPreviousStep(6)"
          >
            {{ savingLocation ? '检测中...' : '检测路径' }}
          </n-button>
          <n-button 
            v-else
            @click="loadDefaultPath" 
            type="success" 
            :loading="savingLocation"
            secondary
            :disabled="!checkPreviousStep(6)"
          >
            {{ savingLocation ? '获取中...' : '获取默认路径' }}
          </n-button>
        </n-space>
      </n-card>
      
      <!-- 步骤 7: 选择同步模式 -->
      <n-card title="⑦ 选择同步模式" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center">
          <n-select 
            v-model:value="syncMode" 
            :options="syncOptions"
            placeholder="请选择同步模式"
            style="width: 300px;"
            :disabled="!checkPreviousStep(7)"
          />
          <n-button 
            @click="saveSyncMode" 
            type="success" 
            :loading="savingLocation"
            secondary
            :disabled="!checkPreviousStep(7)"
          >
            保存设置
          </n-button>
        </n-space>
      </n-card>
      
      <!-- 步骤 8: 同步开关 -->
      <n-card title="⑧ 同步开关" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-space align="center">
          <n-button 
            @click="toggleSync" 
            :type="isOpen ? 'error' : 'success'"
            :loading="savingLocation"
            secondary
            :disabled="!checkPreviousStep(8)"
          >
            {{ isOpen ? '关闭同步' : '开启同步' }}
          </n-button>
          <n-tag 
            :type="isOpen ? 'success' : 'default'"
            size="large"
            bordered
          >
            当前状态：{{ isOpen ? '已开启' : '已关闭' }}
          </n-tag>
        </n-space>
      </n-card>
      
      <!-- 步骤 9: 上传操作（仅在同步开启后显示） -->
      <n-card v-if="isOpen" title="⑨ 上传操作" type="info" size="small" style="margin-bottom: 16px;" class="step-card">
        <n-alert type="info" title="上传功能说明" style="margin-bottom: 12px;">
          <div style="line-height: 1.8;">
            <div><strong>增量上传：</strong>仅上传新增的歌曲文件，速度快，适合日常使用。</div>
            <div><strong>全量上传：</strong>扫描并上传所有歌曲文件，耗时长，适合首次使用或需要完整同步时使用。</div>
          </div>
        </n-alert>
        <n-space align="center" wrap>
          <n-button 
            @click="handleIncrementalUpload" 
            type="primary" 
            :loading="uploading && uploadType === 'incremental'"
            secondary
            size="large"
          >
            {{ uploading && uploadType === 'incremental' ? '上传中...' : '增量上传' }}
          </n-button>
          <n-button 
            @click="handleFullUpload" 
            type="success" 
            :loading="uploading && uploadType === 'full'"
            secondary
            size="large"
          >
            {{ uploading && uploadType === 'full' ? '上传中...' : '全量上传' }}
          </n-button>
        </n-space>
      </n-card>
        </n-tab-pane>
        
        <!-- 数据查询标签页 -->
        <n-tab-pane name="dataQuery" tab="数据查询">
          <n-space vertical>
            <n-card type="info" size="small">
              <n-space vertical>
                <!-- 顶部工具栏：统计 + 视图切换 + 刷新/额外字段 -->
                <n-flex justify="space-between" align="center">
                  <!-- 左侧：统计信息 -->
                  <n-statistic 
                    v-if="fileViewMode === 'list'" 
                    label="文件总数" 
                    :value="fileListTotal" 
                  />
                  <n-statistic 
                    v-else 
                    label="当前项数" 
                    :value="currentFolderContents.length" 
                  />
                  
                  <!-- 右侧：视图切换按钮组 -->
                  <n-space align="center">
                    <n-button-group>
                      <n-button 
                        @click="switchViewMode('list')"
                        :type="fileViewMode === 'list' ? 'primary' : 'default'"
                        secondary
                      >
                        列表视图
                      </n-button>
                      <n-button 
                        @click="switchViewMode('tree')"
                        :type="fileViewMode === 'tree' ? 'primary' : 'default'"
                        secondary
                      >
                        文件夹视图
                      </n-button>
                    </n-button-group>
                    
                    <!-- 刷新按钮 -->
                    <n-button 
                      v-if="fileViewMode === 'list'"
                      @click="loadFileList" 
                      type="primary" 
                      :loading="fileListLoading"
                      secondary
                    >
                      {{ fileListLoading ? '查询中...' : '刷新' }}
                    </n-button>
                    <n-button 
                      v-else
                      @click="loadRootFolder" 
                      type="primary" 
                      :loading="treeListLoading"
                      secondary
                    >
                      {{ treeListLoading ? '加载中...' : '刷新' }}
                    </n-button>

                    <!-- 显示/隐藏额外字段按钮（仅列表视图） -->
                    <n-button 
                      v-if="fileViewMode === 'list' && fileListData.length > 0"
                      @click="showExtraColumns = !showExtraColumns"
                      :type="showExtraColumns ? 'warning' : 'default'"
                      secondary
                    >
                      {{ showExtraColumns ? '隐藏额外字段' : '显示额外字段' }}
                    </n-button>
                  </n-space>
                </n-flex>

                <!-- 列表视图 -->
                <div v-if="fileViewMode === 'list'">
                  <n-alert v-if="fileListData.length === 0" type="info" style="margin-top: 12px;">
                    {{ fileListLoading ? '正在查询...' : '暂无数据，请点击“刷新”按钮查询' }}
                  </n-alert>
                  <div v-else>
                    <n-scrollbar style="max-height: 500px;">
                      <n-data-table
                        :columns="tableColumns"
                        :data="fileListData"
                        :pagination="{ pageSize: 20 }"
                        :bordered="false"
                        size="small"
                      />
                    </n-scrollbar>
                  </div>
                </div>

                <!-- 文件夹视图（Windows 风格） -->
                <div v-else>
                  <n-alert v-if="currentFolderContents.length === 0 && !treeListLoading" type="info" style="margin-top: 12px;">
                    暂无数据，请点击“刷新”按钮查询
                  </n-alert>
                  
                  <div v-else>
                    <!-- 面包屑导航 -->
                    <n-space align="center" wrap style="margin-bottom: 16px;">
                      <!-- 面包屑导航 -->
                      <n-breadcrumb separator=">" style="padding: 8px; border-radius: 4px;">
                        <n-breadcrumb-item 
                          v-for="(crumb, index) in folderBreadcrumbs" 
                          :key="index"
                          @click="navigateToBreadcrumb(index)"
                          style="cursor: pointer;"
                        >
                          {{ crumb.label }}
                        </n-breadcrumb-item>
                      </n-breadcrumb>
                    </n-space>
                    
                    <!-- 文件夹和文件列表 -->
                    <n-scrollbar style="max-height: 500px;">
                      <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 12px; padding: 8px;">
                        <!-- 返回上一级卡片（仅在非根目录时显示） -->
                        <n-card 
                          v-if="folderBreadcrumbs.length > 1"
                          hoverable
                          @click="goBack"
                          :style="{
                            cursor: 'pointer',
                            textAlign: 'center'
                          }"
                        >
                          <!-- 图标 -->
                          <div style="font-size: 48px; margin-bottom: 8px; color: #666;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                              <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/>
                              <line x1="12" y1="11" x2="12" y2="17"/>
                              <polyline points="9 14 12 11 15 14"/>
                            </svg>
                          </div>
                          
                          <!-- 名称 -->
                          <div style="font-size: 14px; color: #666;">
                            返回上一级
                          </div>
                        </n-card>
                        
                        <!-- 文件夹和文件卡片 -->
                        <n-card 
                          v-for="item in currentFolderContents" 
                          :key="item.key"
                          hoverable
                          :style="{
                            cursor: item.type === 'folder' ? 'pointer' : 'default',
                            textAlign: 'center'
                          }"
                          @click="item.type === 'folder' ? enterFolder(item) : null"
                        >
                          <!-- 图标 -->
                          <div style="font-size: 48px; margin-bottom: 8px;">
                            {{ item.type === 'folder' ? '📁' : '🎵' }}
                          </div>
                          
                          <!-- 名称 -->
                          <div 
                            style="
                              font-size: 14px;
                              word-break: break-all;
                              overflow: hidden;
                              text-overflow: ellipsis;
                              display: -webkit-box;
                              -webkit-line-clamp: 2;
                              -webkit-box-orient: vertical;
                              line-height: 1.4;
                            "
                            :title="item.label"
                          >
                            {{ item.label }}
                          </div>
                          
                          <!-- 文件信息 -->
                          <div v-if="item.type === 'file' && item.uploadTime" style="margin-top: 8px; font-size: 12px; color: #999;">
                            {{ item.uploadTime }}
                          </div>
                          
                          <!-- 秒传标记 -->
                          <n-tag 
                            v-if="item.type === 'file' && item.rapid === 1" 
                            type="success" 
                            size="tiny" 
                            style="margin-top: 4px;"
                          >
                            秒传
                          </n-tag>
                        </n-card>
                      </div>
                    </n-scrollbar>
                  </div>
                </div>
              </n-space>
            </n-card>
          </n-space>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </n-spin>

  <!-- 弹出框 -->
<!--  <n-modal-->
<!--    v-model:show="showModal"-->
<!--    :title="tempModalTile"-->
<!--    :draggable="{ bounds: 'none' }"-->
<!--    :style="{ width: '800px' }"-->
<!--  >-->
<!--    <n-card-->
<!--      :title="tempModalTile"-->
<!--      :bordered="false"-->
<!--      size="huge"-->
<!--      role="card"-->
<!--      aria-modal="true"-->
<!--    >-->
<!--      <p>描述：{{ tempModalRemark }}</p>-->
<!--      -->
<!--      <template #footer>-->
<!--        <div v-if="tempModalType === 'input'">-->
<!--          <n-input v-model:value="tempModalValue" :disabled="tempModalDisabled === 1" />-->
<!--        </div>-->
<!--        <div v-if="tempModalType === 'path'">-->
<!--          <n-input v-model:value="tempModalValue" :disabled="tempModalDisabled === 1" />-->
<!--        </div>-->
<!--        <div v-if="tempModalType === 'password'">-->
<!--          <n-input-->
<!--            type="password"-->
<!--            show-password-on="mousedown"-->
<!--            placeholder="密码"-->
<!--            v-model:value="tempModalValue"-->
<!--            :disabled="tempModalDisabled === 1"-->
<!--          />-->
<!--        </div>-->
<!--        <div v-if="tempModalType === 'boolean'">-->
<!--          <n-switch v-model:value="tempModalValue" :disabled="tempModalDisabled === 1" />-->
<!--        </div>-->
<!--        <div v-if="tempModalType === 'number'">-->
<!--          <n-input-number v-model:value="tempModalValue" :disabled="tempModalDisabled === 1" />-->
<!--        </div>-->
<!--        <div v-if="tempModalType === 'select'">-->
<!--          <n-select-->
<!--            v-model:value="tempModalValue"-->
<!--            :options="tempOptions"-->
<!--            :disabled="tempModalDisabled === 1"-->
<!--            placeholder="请选择"-->
<!--            :multiple="false"-->
<!--          />-->
<!--        </div>-->
<!--        <div v-if="tempModalType === '' || tempModalType === null">-->
<!--          <n-input v-model:value="tempModalValue" :disabled="tempModalDisabled === 1" />-->
<!--        </div>-->

<!--        <br><br>-->
<!--        <div style="display: flex; flex-direction: row; justify-content: flex-end">-->
<!--          <n-button @click="setConfig" type="primary">确定</n-button>-->
<!--          <div style="width: 30px;"></div>-->
<!--          <n-button @click="closeDialog">取消</n-button>-->
<!--        </div>-->
<!--      </template>-->
<!--    </n-card>-->
<!--  </n-modal>-->
</template>

<style scoped>
.config-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
}

@media (max-width: 1600px) {
  .config-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 1300px) {
  .config-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 1000px) {
  .config-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 700px) {
  .config-grid {
    grid-template-columns: repeat(1, 1fr);
  }
}

.n-card:hover {
  transform: translateY(-2px);
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>
