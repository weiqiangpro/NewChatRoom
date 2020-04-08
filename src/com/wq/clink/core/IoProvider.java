package com.wq.clink.core;

import java.io.Closeable;
import java.nio.channels.SocketChannel;

/**
 * @Author: weiqiang
 * @Time: 2020/3/10 上午10:30
 */
public interface IoProvider extends Closeable {

    boolean readRegister(SocketChannel channel, Runnable runnable);
    boolean writeRegister(SocketChannel channel, Runnable runnable);
    void unReadRegister(SocketChannel channel);
    void unWriteRegister(SocketChannel channel);

}
