package com.sqmusicplus.v3.teaboss.Pow;


public class Tlv
{
    private String tag;
    private Integer length;
    private byte[] value;
    private byte[] bin;

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public byte[] getValue() {
        return this.value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }






    public byte[] getBin() {
        if (this.bin != null) {
            return this.bin;
        }
        byte[] head = Converts.hexStringToByte(getTag());
        byte[] length = Converts.numTo2Bytes(getLength());
        byte[] value = getValue();
        return ByteUtil.merger(ByteUtil.merger(head, length), value);
    }

    public void setBin(byte[] bin) {
        this.bin = bin;
    }
}
