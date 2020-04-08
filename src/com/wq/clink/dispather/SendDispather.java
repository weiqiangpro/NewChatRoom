package com.wq.clink.dispather;

import com.wq.clink.core.IoArgs;
import com.wq.clink.core.Sender;
import com.wq.clink.dispather.box.StringSendPacket;
import com.wq.clink.dispather.dispathercal.IoArgsCallback;
import com.wq.utils.constants.CloseUtil;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: weiqiang
 * @Time: 2020/3/20 下午10:14
 */
public class SendDispather {
    private final Queue<StringSendPacket> queue = new ConcurrentLinkedDeque<>();
    private final AtomicBoolean isSending = new AtomicBoolean();
    private final AtomicBoolean isClosed = new AtomicBoolean();
    private IoArgs ioArgs = new IoArgs();
    private int total;
    private int position;
    private final Sender sender;
    private StringSendPacket temp;

    public SendDispather(Sender sender) {
        this.sender = sender;
    }

    public void send(StringSendPacket packet) {
        queue.add(packet);
        if (isSending.compareAndSet(false, true))
            sendNextMes();
    }

    private void sendNextMes() {
        if (temp != null)
            CloseUtil.close(temp);
        StringSendPacket packet = temp = queue.poll();
        if (packet == null){
            isSending.set(false);
            return;
        }
        total = packet.getLength();
        position = 0;
        sendCurrentPacket();
    }
    private void sendCurrentPacket() {
        IoArgs args = ioArgs;
        args.startWriting();
        if (position >= total) {
            sendNextMes();
            return;
        } else if (position == 0) {
            args.writeLength(total);
        }
        byte[] bytes = temp.bytes();
        int cout = args.readFrom(bytes, position);
        position += cout;
        args.finishWriting();
        try {
            sender.senderAsync(ioArgs,sendDisCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final IoArgsCallback sendDisCallback = new IoArgsCallback() {
        @Override
        public void onStart() {

        }

        @Override
        public void onCompleted() {
            sendCurrentPacket();
        }
    };
}
