package com.wq.server;

import com.wq.clink.Context;
import com.wq.clink.core.impl.MyProvider;
import com.wq.utils.util.TCPConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    public static void main(String[] args) throws IOException {
        Context.setup(new MyProvider());

        TcpServer tcpServer = new TcpServer();
        boolean start = tcpServer.start(TCPConstants.PORT_SERVER);
        if (!start){
            System.out.println("TCP服务器启动失败");
            return;
        }
        UdpServer.start(TCPConstants.PORT_SERVER);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = bufferedReader.readLine();
            tcpServer.send(str);
        } while (!"00bye00".equalsIgnoreCase(str));
        UdpServer.stop();
        tcpServer.stop();
        Context.close();
    }
}
