package com.sqmusicplus.lm.teaboss.Pow;

import org.apache.commons.lang3.ArrayUtils;

public class BufferHelper {
    private byte[] buffer;

    public BufferHelper(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte[] getArray(int length) {
        return this.getArray(length, true);
    }

    public byte[] getArray(int length, boolean delete) {
        byte[] dest = ArrayUtils.subarray(this.buffer, 0, length);
        if (delete) {
            this.removeHasRead(dest.length);
        }

        return dest;
    }



    public void removeHasRead(int length) {
        for(int i = 0; i < length; ++i) {
            this.buffer = ArrayUtils.remove(this.buffer, 0);
        }

    }


    public byte getByte() {
        return this.getArray(1)[0];
    }


    public Short getShort() {
        return (short)((int)Converts.bytesToNum(this.getArray(2)));
    }

}
