package com.sqmusicplus.lm.teaboss.Qimei;
public final class Device {
    private String android_id = "0dca69b184829b6c";
    private String qimei36 = "92aed99a63e5406e20a5c28e100018917314";
    private String guid = "626023d442144afd3948565cd266e070";
    private String brand = "samsung";
    private String model = "SM-S9010";
    private String UUID = "5f0d6dc0-3a45-49c8-9ddf-3173e66b3208";
    private long UtcTime = System.currentTimeMillis() / 1000L;
    private String qq_ver = "";
    private String qq_ver_sub = "";
    private String signver = "0x07";
    private String FEKitSoLoader_LocalSdkVersion = "";
    private String qq_code = "";
    private String qua = "";

    public Device(String qq_ver) {
        this.qq_ver = qq_ver;
        String var2;
        switch ((var2 = this.qq_ver).hashCode()) {
            case 48522365:
                if (var2.equals("3.5.1")) {
                    this.qua = "V1_AND_SQ_8.3.9_351_TIM_D";
                    this.qq_code = "351";
                    this.qq_ver_sub = "3168";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.127";
                    this.signver = "0x03";
                    return;
                }
                break;
            case 48522366:
                if (var2.equals("3.5.2")) {
                    this.qua = "V1_AND_SQ_8.3.9_352_TIM_D";
                    this.qq_code = "352";
                    this.qq_ver_sub = "3178";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.148";
                    this.signver = "0x05";
                    return;
                }
                break;
            case 1647458321:
                if (var2.equals("8.9.28")) {
                    this.qua = "V1_AND_SQ_8.9.28_3700_YYB_D";
                    this.qq_code = "3700";
                    this.qq_ver_sub = "10155";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.103";
                    this.signver = "0x03";
                    return;
                }
                break;
            case 1647458344:
                if (var2.equals("8.9.30")) {
                    this.qua = "V1_AND_SQ_8.9.30_3730_YYB_D";
                    this.qq_code = "3730";
                    this.qq_ver_sub = "10230";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.122";
                    this.signver = "0x03";
                    return;
                }
                break;
            case 1647458347:
                if (var2.equals("8.9.33")) {
                    this.qua = "V1_AND_SQ_8.9.33_3772_YYB_D";
                    this.qq_code = "3772";
                    this.qq_ver_sub = "10335";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.130";
                    this.signver = "0x03";
                    return;
                }
                break;
            case 1647458349:
                if (var2.equals("8.9.35")) {
                    this.qua = "V1_AND_SQ_8.9.35_3814_YYB_D";
                    this.qq_code = "3814";
                    this.qq_ver_sub = "10440";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.148";
                    this.signver = "0x05";
                    return;
                }
                break;
            case 1647458352:
                if (var2.equals("8.9.38")) {
                    this.qua = "V1_AND_SQ_8.9.38_3856_YYB_D";
                    this.qq_code = "3856";
                    this.qq_ver_sub = "10545";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.163";
                    this.signver = "0x05";
                    return;
                }
                break;
            case 1647458406:
                if (var2.equals("8.9.50")) {
                    this.qua = "V1_AND_SQ_8.9.50_3898_YYB_D";
                    this.qq_code = "3886";
                    this.qq_ver_sub = "10650";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.166";
                    this.signver = "0x05";
                    return;
                }
                break;
            case 1647458409:
                if (var2.equals("8.9.53")) {
                    this.qua = "V1_AND_SQ_8.9.53_3964_YYB_D";
                    this.qq_code = "3964";
                    this.qq_ver_sub = "10815";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.187";
                    this.signver = "0x07";
                    return;
                }
                break;
            case 1647458411:
                if (var2.equals("8.9.55")) {
                    this.qua = "V1_AND_SQ_8.9.55_4030_YYB_D";
                    this.qq_code = "4030";
                    this.qq_ver_sub = "10980";
                    this.FEKitSoLoader_LocalSdkVersion = "2.2.196";
                    this.signver = "0x07";
                    return;
                }
                break;
            case 1647458414:
                if (var2.equals("8.9.58")) {
                    this.qua = "V1_AND_SQ_8.9.58_4106_YYB_D";
                    this.qq_code = "4106";
                    this.qq_ver_sub = "11170";
                    this.FEKitSoLoader_LocalSdkVersion = "6.2.221";
                    this.signver = "0x07";
                    return;
                }
                break;
            case 1647458440:
                if (var2.equals("8.9.63")) {
                    this.qua = "V1_AND_SQ_8.9.63_4194_YYB_D";
                    this.qq_code = "4194";
                    this.qq_ver_sub = "11390";
                    this.FEKitSoLoader_LocalSdkVersion = "6.100.248";
                    this.signver = "0x09";
                    return;
                }
                break;
            case 1647458445:
                if (var2.equals("8.9.68")) {
                    this.qua = "V1_AND_SQ_8.9.68_4264_YYB_D";
                    this.qq_code = "4264";
                    this.qq_ver_sub = "11565";
                    this.FEKitSoLoader_LocalSdkVersion = "7.0.300";
                    this.signver = "0x0B";
                    return;
                }
                break;
            case 1647458468:
                if (!var2.equals("8.9.70")) {
                    this.qua = "V1_AND_SQ_8.9.70_4330_YYB_D";
                    this.qq_code = "4330";
                    this.qq_ver_sub = "11730";
                    this.FEKitSoLoader_LocalSdkVersion = "7.0.326";
                    this.signver = "0x0B";
                }
        }


    }

    public String getQq_ver_sub() {
        return this.qq_ver_sub;
    }

    public String getQq_completename() {
        return this.getQq_ver() + "." + this.getQq_ver_sub();
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
    public final String getQq_ver() {
        return this.qq_ver;
    }

    public String getQua() {
        return this.qua;
    }

    public void setQua(String qua) {
        this.qua = qua;
    }

}
