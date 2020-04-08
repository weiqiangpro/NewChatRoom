package com.wq.clink.core.impl;

import com.wq.clink.callback.ReadCallBack;
import com.wq.clink.callback.WriteCallBack;
import com.wq.clink.core.IoArgs;
import com.wq.clink.core.IoProvider;
import com.wq.clink.core.Receiver;
import com.wq.clink.core.Sender;
import com.wq.clink.dispather.dispathercal.IoArgsCallback;
import com.wq.utils.constants.CloseUtil;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketChannelAdapter implements Sender, Receiver, Closeable {

    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final SocketChannel channel;
    private final IoProvider ioProvider;
    private IoArgsCallback receiveDisCallback;
    private IoArgsCallback sendDisCallback;
    private IoArgs ioArgs;
    public SocketChannelAdapter(SocketChannel channel, IoProvider ioProvider) throws IOException {
        this.channel = channel;
        this.ioProvider = ioProvider;
        channel.configureBlocking(false);
    }

    @Override
    public boolean receiveAsync(IoArgs ioArgs, IoArgsCallback callBack) throws IOException {
        if (isClosed.get()) {
            throw new IOException("Current channel is closed!");
        }
        this.readCallBack.setIoArgs(ioArgs);
        this.receiveDisCallback = callBack;
        return ioProvider.readRegister(channel, readCallBack);
    }

    @Override
    public boolean senderAsync(IoArgs ioArgs, IoArgsCallback callBack) throws IOException {
        if (isClosed.get()) {
            throw new IOException("Current channel is closed!");
        }
        this.sendDisCallback = callBack;
        writeCallBack.setIoArgs(ioArgs);
        return ioProvider.writeRegister(channel, writeCallBack);
    }

    public final ReadCallBack readCallBack = new ReadCallBack() {
        @Override
        protected void canProviderInput() {
            if (isClosed.get()) {
                return;
            }
            IoArgs args = getIoArgs();
            IoArgsCallback receiveDisCallback = SocketChannelAdapter.this.receiveDisCallback;
            try {
                receiveDisCallback.onStart();
                // 具体的读取操作
                if (args.read(channel) > 0) {
                    // 读取完成回调
                    receiveDisCallback.onCompleted();
                } else {
                    throw new IOException("Cannot read any data!");
                }
            } catch (IOException ignored) {
                CloseUtil.close(SocketChannelAdapter.this);
            }
        }
    };

    public final WriteCallBack writeCallBack = new WriteCallBack() {
        @Override
        protected void canProviderOutput() {
            if (isClosed.get()) {
                return;
            }
            IoArgs args = getIoArgs();
            try {
                // 具体的读取操
                if (args.write(channel) > 0) {
                    SocketChannelAdapter.this.sendDisCallback.onCompleted();
                    // 读取完成回调
                } else {
                    throw new IOException("Cannot write any data!");
                }

            } catch (IOException ignored) {
                CloseUtil.close(SocketChannelAdapter.this);
            }
        }
    };

    @Override
    public void close() throws IOException {

    }


}