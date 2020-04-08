package com.wq.clink.dispather;

import com.wq.clink.callback.OnArrivedAndReadNext;
import com.wq.clink.core.IoArgs;
import com.wq.clink.core.Receiver;
import com.wq.clink.dispather.box.StringReceivePacket;
import com.wq.clink.dispather.dispathercal.IoArgsCallback;
import com.wq.utils.constants.CloseUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: weiqiang
 * @Time: 2020/3/20 下午10:14
 */
public class ReceiveDispather {
    private final AtomicBoolean isClosed = new AtomicBoolean();
    private IoArgs ioArgs = new IoArgs();
    private int total;
    private int position;
    private final Receiver receiver;
    private byte[] buffer;
    private StringReceivePacket temp;
    private  final OnArrivedAndReadNext onArrivedAndReadNext;
    public ReceiveDispather(Receiver receiver, OnArrivedAndReadNext onArrivedAndReadNext) {
        this.receiver = receiver;
        this.onArrivedAndReadNext = onArrivedAndReadNext;
    }

    public void start() {
        try {
            receiver.receiveAsync(ioArgs,receiveDisCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private final IoArgsCallback receiveDisCallback = new IoArgsCallback() {
        @Override
        public void onStart() {
            int receiveSize;
            if (temp == null) {
                receiveSize = 4;
            } else {
                receiveSize = Math.min(total - position, ioArgs .capacity());
            }
            ioArgs.limt(receiveSize);
        }

        @Override
        public void onCompleted() {
            assemblePacker();
            start();
        }
    };

    private void assemblePacker() {
        if (temp == null) {
            int length = ioArgs.readLength();
            temp = new StringReceivePacket(length);
            buffer = new byte[length];
            total = length;
            position = 0;
        }
        int count = ioArgs.writeTo(buffer, 0);
        if (count > 0) {
            temp.save(buffer, count);
            position += count;
            if (position == total) {
                CloseUtil.close(temp);
                onArrivedAndReadNext.onCompleted(temp);
                temp = null;
            }
        }
    }
}
