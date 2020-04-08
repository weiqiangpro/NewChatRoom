package com.wq.clink;

import com.wq.clink.callback.OnArrivedAndReadNext;
import com.wq.clink.core.Receiver;
import com.wq.clink.core.impl.SocketChannelAdapter;
import com.wq.clink.dispather.ReceiveDispather;
import com.wq.clink.dispather.SendDispather;
import com.wq.clink.dispather.box.StringReceivePacket;
import com.wq.clink.dispather.box.StringSendPacket;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

/**
 * @Author: weiqiang
 * @Time: 2020/3/10 上午10:58
 */
public class Connector implements Closeable {
    private UUID key = UUID.randomUUID();
    private SocketChannel channel;
    private Receiver receiver;
    private SendDispather sendDispather;
    public void setup(SocketChannel socketChannel) throws IOException {
        this.channel = socketChannel;
        Context context = Context.get();
        SocketChannelAdapter adapter = new SocketChannelAdapter(channel, context.getIoProvider());
        this.sendDispather = new SendDispather(adapter);
        new ReceiveDispather(adapter,onArrivedAndReadNext).start();

    }

    public void send(String str){
        StringSendPacket stringSendPacket = new StringSendPacket(str);
        sendDispather.send(stringSendPacket);
    }


    private final OnArrivedAndReadNext onArrivedAndReadNext = new OnArrivedAndReadNext() {

        @Override
        public void onCompleted(StringReceivePacket packet) {
            onReceiveNewMessage(packet.string());
}
    };

    protected void onReceiveNewMessage(String str) {
        // System.out.println(key.toString() + ":" + str);
    }

    @Override
    public void close() throws IOException {

    }

}
