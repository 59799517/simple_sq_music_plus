package com.sqmusicplus.v3.plug.apple.hander;

import lombok.extern.slf4j.Slf4j;

/**
 * @Classname WidevineJNIWrapper
 * @Description Widevine JNI包装器实现
 * @Version 1.0.0
 * @Date 2025/10/18 15:30
 * @Created by SQ
 */
@Slf4j
public class WidevineJNIWrapper {
    
    private static WidevineJNIWrapper instance;
    private WidevineCDMHandler cdmHandler;
    private long cdmInstanceId;
    private boolean initialized = false;
    
    private WidevineJNIWrapper() {
        try {
            cdmHandler = new WidevineCDMHandler();
            initialized = cdmHandler.initializeCDMModule();
            if (initialized) {
                // 创建CDM实例，使用Widevine密钥系统
                cdmInstanceId = cdmHandler.createCDMInstance("com.widevine.alpha");
                log.info("Widevine CDM初始化成功，实例ID: {}", cdmInstanceId);
            } else {
                log.error("Widevine CDM模块初始化失败");
            }
        } catch (Exception e) {
            log.error("Widevine JNI包装器初始化失败", e);
        }
    }
    
    public static synchronized WidevineJNIWrapper getInstance() {
        if (instance == null) {
            instance = new WidevineJNIWrapper();
        }
        return instance;
    }
    
    /**
     * 获取挑战数据
     * @param isFirefox 是否Firefox浏览器
     * @param serverCertificate 服务器证书（可选）
     * @return 挑战数据
     */
    public byte[] getChallenge(boolean isFirefox, byte[] serverCertificate) {
        if (!initialized) {
            log.error("Widevine CDM未初始化");
            return new byte[0];
        }
        
        try {
            boolean isRenewal = false; // 初始请求不是续订
            int certificateLength = serverCertificate != null ? serverCertificate.length : 0;
            
            return cdmHandler.getChallenge(
                cdmInstanceId, 
                isFirefox, 
                isRenewal, 
                serverCertificate != null ? serverCertificate : new byte[0], 
                certificateLength
            );
        } catch (Exception e) {
            log.error("获取挑战数据失败", e);
            return new byte[0];
        }
    }
    
    /**
     * 提供许可证
     * @param licenseData 许可证数据
     * @return 是否成功
     */
    public boolean provideLicense(byte[] licenseData) {
        if (!initialized) {
            log.error("Widevine CDM未初始化");
            return false;
        }
        
        try {
            return cdmHandler.provideLicense(cdmInstanceId, licenseData, licenseData.length);
        } catch (Exception e) {
            log.error("提供许可证失败", e);
            return false;
        }
    }
    
    /**
     * 获取解密密钥
     * @param keyId 密钥ID
     * @return 密钥数据
     */
    public byte[] getKey(byte[] keyId) {
        if (!initialized) {
            log.error("Widevine CDM未初始化");
            return new byte[0];
        }
        
        try {
            return cdmHandler.getKey(cdmInstanceId, keyId);
        } catch (Exception e) {
            log.error("获取密钥失败", e);
            return new byte[0];
        }
    }
    
    /**
     * 解密数据
     * @param encryptedData 加密数据
     * @param iv 初始化向量
     * @param keyId 密钥ID
     * @return 解密后的数据
     */
    public byte[] decrypt(byte[] encryptedData, byte[] iv, byte[] keyId) {
        if (!initialized) {
            log.error("Widevine CDM未初始化");
            return new byte[0];
        }
        
        try {
            return cdmHandler.decrypt(cdmInstanceId, encryptedData, iv, keyId);
        } catch (Exception e) {
            log.error("解密数据失败", e);
            return new byte[0];
        }
    }
    
    /**
     * 销毁资源
     */
    public void destroy() {
        if (initialized && cdmHandler != null) {
            try {
                cdmHandler.destroyCDMInstance(cdmInstanceId);
                cdmHandler.deinitializeCDMModule();
                log.info("Widevine CDM资源已销毁");
            } catch (Exception e) {
                log.error("销毁Widevine CDM资源失败", e);
            }
        }
        instance = null;
    }
}