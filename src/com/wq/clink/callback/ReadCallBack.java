package com.wq.clink.callback;

import com.wq.clink.core.IoArgs;

public abstract class ReadCallBack implements Runnable {
    private IoArgs ioArgs;

    public void setIoArgs(IoArgs ioArgs) {
        this.ioArgs = ioArgs;
    }
    public IoArgs getIoArgs() {
        return ioArgs;
    }
    @Override
    public void run() {
         canProviderInput();
    }

    protected abstract void canProviderInput();
}

