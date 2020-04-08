package com.wq.clink;


import com.wq.clink.core.IoProvider;

import java.io.IOException;

/**
 * @Author: weiqiang
 * @Time: 2020/3/10 上午10:24
 */
public class Context {
    private static Context INSTANCE;
    private final IoProvider ioProvider;

    private Context(IoProvider ioProvider) {
        this.ioProvider = ioProvider;
    }

    public IoProvider getIoProvider() {
        return ioProvider;
    }

    public static Context get() {
        return INSTANCE;
    }

    public static void setup(IoProvider ioProvider) {
        INSTANCE = new Context(ioProvider);

    }

    public static void close() throws IOException {
        if (INSTANCE != null) {
            INSTANCE.callClose();
        }
    }

    private void callClose() throws IOException {
        ioProvider.close();
    }
}
