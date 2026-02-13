package com.sqmusicplus.v3.plug.apple.hander;

import lombok.extern.slf4j.Slf4j;

/**
 * @Classname WidevineCDMHandler
 * @Description JNI包装器用于与Widevine CDM库交互
 * @Version 1.0.0
 * @Date 2025/10/18 15:00
 * @Created by SQ
 */
@Slf4j
public class WidevineCDMHandler {
    
    // 加载本地库
    static {
        try {
            System.loadLibrary("widevinecdm"); // 加载libwidevinecdm.so, libwidevinecdm.dylib, 或widevinecdm.dll
        } catch (UnsatisfiedLinkError e) {
            log.error("无法加载Widevine CDM库", e);
        }
    }
    
    /**
     * 初始化CDM模块
     * @return 初始化是否成功
     */
    public native boolean initializeCDMModule();
    
    /**
     * 反初始化CDM模块
     */
    public native void deinitializeCDMModule();
    
    /**
     * 创建CDM实例
     * @param keySystem 密钥系统标识
     * @return CDM实例ID
     */
    public native long createCDMInstance(String keySystem);
    
    /**
     * 销毁CDM实例
     * @param instanceId CDM实例ID
     */
    public native void destroyCDMInstance(long instanceId);
    
    /**
     * 获取挑战数据
     * @param instanceId CDM实例ID
     * @param isPrivacyMode 是否隐私模式
     * @param isRenewal 是否续订
     * @param serverCertificate 服务器证书
     * @param certificateLength 证书长度
     * @return 挑战数据
     */
    public native byte[] getChallenge(long instanceId, boolean isPrivacyMode, boolean isRenewal, 
                                    byte[] serverCertificate, int certificateLength);
    
    /**
     * 提供许可证
     * @param instanceId CDM实例ID
     * @param licenseData 许可证数据
     * @param licenseLength 许可证长度
     * @return 是否成功
     */
    public native boolean provideLicense(long instanceId, byte[] licenseData, int licenseLength);
    
    /**
     * 获取密钥
     * @param instanceId CDM实例ID
     * @param keyId 密钥ID
     * @return 密钥数据
     */
    public native byte[] getKey(long instanceId, byte[] keyId);
    
    /**
     * 解密数据
     * @param instanceId CDM实例ID
     * @param encryptedData 加密数据
     * @param iv 初始化向量
     * @param keyId 密钥ID
     * @return 解密后的数据
     */
    public native byte[] decrypt(long instanceId, byte[] encryptedData, byte[] iv, byte[] keyId);
}