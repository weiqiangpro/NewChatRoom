package com.wq.server.handle;

import com.wq.clink.Connector;
import com.wq.utils.constants.CloseUtil;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ServerHandler extends  Connector{
    private SocketChannel client;
    private boolean done = false;
    private String info;
    private CallBack callBack;

    public ServerHandler(SocketChannel client, CallBack callBack) throws IOException {
        this.callBack = callBack;
            this.info = "客户端：[" + client.getRemoteAddress().toString() + "]";
        setup(client);
    }

    @Override
    public boolean equals(Object obj) {
        return info.equals(((ServerHandler) obj).getInfo());
    }

    public String getInfo() {
        return info;
    }

    @Override
    protected void onReceiveNewMessage(String str) {
        super.onReceiveNewMessage(str);
        callBack.onArriveMes(this,str);
    }

    public void exit() {
        CloseUtil.close(client);
        callBack.onCloseSelf(ServerHandler.this);
        System.out.println( this.info+" 已退出");
    }

    public interface CallBack {
        void onArriveMes(ServerHandler serverHandler, String mes);
        void onCloseSelf(ServerHandler serverHandler);
    }

}