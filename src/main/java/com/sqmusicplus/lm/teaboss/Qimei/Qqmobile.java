package com.sqmusicplus.lm.teaboss.Qimei;

import com.sqmusicplus.lm.teaboss.Pow.Converts;
import lombok.Data;

@Data
public class Qqmobile  {
    private String imei;
    private String androidid;
    private String androidver;
    private String releasekeys;
    private String guid;
    private String model;
    private String brand;


    public Qqmobile() {
    }

    public byte[] getGuidBin() {
        // 将字符串转换为字节数组
        if (this.getGuid() == null) {
            return null;
        }
        if (this.getGuid().length() == 0) {
            return null;
        }
        return Converts.hexStringToByte(this.getGuid());
    }

    public String getApiLevel(String Ver) {
        String apiLevel = "23";
        switch (Ver) {
            case "9":
                apiLevel = "28";
                return apiLevel;
            case "10":
                apiLevel = "29";
                return apiLevel;
            case "11":
                apiLevel = "30";
                return apiLevel;
            case "12":
                apiLevel = "31";
                return apiLevel;
            case "13":
                apiLevel = "33";
                return apiLevel;
            case "6.0":
                apiLevel = "23";
                return apiLevel;
            case "7.0":
                apiLevel = "24";
                return apiLevel;
            case "7.1":
                apiLevel = "25";
                return apiLevel;
            case "8.0":
                apiLevel = "26";
                return apiLevel;
            case "8.1":
                apiLevel = "27";
                return apiLevel;
            case "6.0.1":
                apiLevel = "23";
                return apiLevel;
            case "7.1.1":
                apiLevel = "25";
                return apiLevel;
            case "7.1.2":
                apiLevel = "25";
                return apiLevel;
        }

        apiLevel = "28";
        return apiLevel;
    }
}
