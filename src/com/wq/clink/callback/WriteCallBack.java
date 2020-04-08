package com.wq.clink.callback;

import com.wq.clink.core.IoArgs;

public abstract class WriteCallBack implements Runnable {
    private IoArgs ioArgs;

    public void setIoArgs(IoArgs ioArgs) {
        this.ioArgs = ioArgs;
    }
    public IoArgs getIoArgs() {
        return ioArgs;
    }

    @Override
    public void run() {
        canProviderOutput();
    }

    protected abstract void canProviderOutput();
}

