package com.sqmusicplus.v3.base.enums;


/**
 * @Classname DbBooleanConvert
 * @Description 数据库是否转化
 * @Version 1.0.0
 * @Date 2025/7/14 15:29
 * @Created by SQ
 */
public enum DbBooleanConvert {
    YES(1,true),
    NO(0, false);
    private Integer value;
    private Boolean booleanValue;

    //根据int值获取枚举
    public static boolean findByValue(Integer value) {
        for (DbBooleanConvert dbBooleanConvert : DbBooleanConvert.values()) {
            if (dbBooleanConvert.value.equals(value)) {
                return dbBooleanConvert.getBooleanValue();
            }
        }
        return false;
    }



    DbBooleanConvert(Integer value, Boolean booleanValue) {
        this.value = value;
        this.booleanValue = booleanValue;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }
}
