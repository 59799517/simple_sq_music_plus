package com.sqmusicplus.v3.plug.qq.enums;

/**
 * @Classname QRCodeLoginEvents
 * @Description 二维码状态
 * @Version 1.0.0
 * @Date 2025/4/18 16:45
 * @Created by SQ
 */

public enum QRCodeLoginEvents {


//  SCAN: 等待扫描二维码
//  CONF: 已扫码未确认登录
//  TIMEOUT: 二维码已过期
//  DONE: 扫码成功
//  REFUSE: 拒绝登录
//  OTHER: 未知情况
//  STOP: 扫码任务停止
//  SUCCESS 获取到code 全部成功
//  NOTFOUND 找不到code


    DONE(0, 405, "扫码成功"),
    SCAN(66, 408,"等待扫描二维码"),
    CONF(67, 404, "已扫码未确认登录"),
    TIMEOUT(65, null,"二维码已过期,需要重新扫码"),
    REFUSE(68, 403,  "拒绝登录,需要重新扫码"),
    OTHER(99, null,"未知情况,需要重新扫码"),
    //停止扫描
    STOP(100, 100,  "扫码任务停止,需要重新扫码"),
    CODE_SUCCESS(101, 100,"扫码成功获得授权成功"),
    NOTFOUND(102, 100,  "未找到code,需要重新扫码"),
    COOKIE_SUCCESS(103, 100,  "获取cookie成功");



    private final Integer key;
    private final Integer value;
    private final String desc;

    QRCodeLoginEvents(Integer key, Integer value, String desc) {
        this.key = key;
        this.value = value;
        this.desc = desc;
    }

    /**
     * 根据传入的值查找对应的枚举成员
     *
     * @param value 传入的整数值
     * @return 对应的 QRCodeLoginEvents 枚举成员
     */
    public static QRCodeLoginEvents getByValue(Integer value) {
        if (value == null){
            return QRCodeLoginEvents.OTHER;
        }
        QRCodeLoginEvents[] values = QRCodeLoginEvents.values();
        for (QRCodeLoginEvents member : values) {
            if (member.getKey().intValue()==value) {
                return member;
            }
        }
        return QRCodeLoginEvents.OTHER;
    }

    /**
     * 判断值是否相等（支持 null 值判断）
     */
    private static boolean valueEquals(Integer enumValue, Integer value) {
        return enumValue != null && enumValue.intValue() == value;
    }

    public Integer getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}


