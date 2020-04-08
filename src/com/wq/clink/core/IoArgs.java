package com.wq.clink.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: weiqiang
 * @Time: 2020/3/10 上午10:26
 */
public class IoArgs {
    private byte[] bytes = new byte[6];
    private ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
    private int limit = 6;

    public int read(SocketChannel channel) throws IOException {
        startWriting();
        int bytesProduced = 0;
        while (byteBuffer.hasRemaining()) {
            int len = channel.read(byteBuffer);
            if (len < 0) {
                throw new EOFException();
            }
            bytesProduced += len;
        }
        finishWriting();
        return bytesProduced;
    }

    public int readFrom(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, byteBuffer.remaining());
        byteBuffer.put(bytes, offset, size);
        return size;
    }
    public int write(SocketChannel channel) throws IOException {
        int bytesProduced = 0;
        while (byteBuffer.hasRemaining()) {
            int len = channel.write(byteBuffer);
            if (len < 0) {
                throw new EOFException();
            }
            bytesProduced += len;
        }
        return bytesProduced;
    }
    public int writeTo(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, byteBuffer.remaining());
        byteBuffer.get(bytes, offset, size);
        return size;
    }
    public void startWriting() {
        byteBuffer.clear();
        byteBuffer.limit(limit);
    }

    public void finishWriting() {
        byteBuffer.flip();
    }

    public void limt(int limit) {
        this.limit = limit;
    }

    public void writeLength(int total) {
        byteBuffer.putInt(total);
    }

    public int capacity() {
        return byteBuffer.capacity();
    }

    public int readLength() {
        return byteBuffer.getInt();
    }

    public String bufferString() {
        // 丢弃换行符
        return new String(bytes, 0, byteBuffer.position() - 1);
    }

}
