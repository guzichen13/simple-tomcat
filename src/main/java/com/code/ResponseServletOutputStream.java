package com.code;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ResponseServletOutputStream extends ServletOutputStream {

    /**
     * 暂存字节数组
     */
    private byte[] bytes = new byte[1024];

    /**
     * 下标
     */
    private int pos = 0;

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        bytes[pos] = (byte) b;
        pos++;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getPos() {
        return pos;
    }
}
