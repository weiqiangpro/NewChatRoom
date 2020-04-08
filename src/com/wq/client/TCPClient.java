package com.wq.client;


import com.wq.client.bean.ServerInfo;
import com.wq.clink.Connector;
import com.wq.ui.chatroom.UiArriveMes;
import com.wq.utils.constants.CloseUtil;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class TCPClient extends Connector {
    private UiArriveMes uiArriveMes;
    public TCPClient(SocketChannel socket) throws IOException {
       setup(socket);
    }

    public void exit() {
        CloseUtil.close(this);
    }


    public static TCPClient startWith(ServerInfo info) throws IOException {
        SocketChannel socket = SocketChannel.open();

        socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()));

        System.out.println("已发起服务器连接，并进入后续流程～");
        System.out.println("客户端信息：" + socket.getLocalAddress());
        System.out.println("服务器信息：" + socket.getRemoteAddress());
        try {
            return new TCPClient(socket);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接异常");
            CloseUtil.close(socket);
        }
        return null;
    }

    @Override
    protected void onReceiveNewMessage(String str) {
        super.onReceiveNewMessage(str);
        if (uiArriveMes!=null){
            uiArriveMes.onArrive(str.toString() + ":  " + str);
        }

    }

    public void onChannelClosed(SocketChannel channel) {
        System.out.println("链接关闭");
    }


    public void setUiArriveMes(UiArriveMes uiArriveMes) {
        this.uiArriveMes = uiArriveMes;
    }
}
