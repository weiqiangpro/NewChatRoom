package com.wq.clink.dispather.box;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class StringSendPacket implements Closeable {

    private byte[] bytes;
    private int length;

    public StringSendPacket(String msg) {
        try {
            this.bytes = msg.getBytes("utf-8");
            this.length = bytes.length;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public int getLength(){
        return length;
    }

    public byte[] bytes() {
        return bytes;
    }

    @Override
    public void close() throws IOException {

    }
}