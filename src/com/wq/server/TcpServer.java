package com.wq.server;

import com.wq.server.handle.ServerHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer implements ServerHandler.CallBack {
    private ServerListener serverListener;
    private List<ServerHandler> serverHandlerList = new ArrayList<ServerHandler>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ServerSocketChannel serverSocketChannel = null;

    public boolean start(int port) {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.serverSocketChannel = serverSocketChannel;

            System.out.println("服务器信息：" + serverSocketChannel.getLocalAddress());
            serverListener = new ServerListener(selector);
            serverListener.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("端口号被占用");
            return false;
        }
        return true;
    }

    public void stop() {
        if (serverListener != null) {
            serverListener.exit();
            serverListener = null;
        }
        for (ServerHandler serverHandler : serverHandlerList) {
            serverHandler.exit();
        }
        serverHandlerList.clear();
    }


    public synchronized void send(String str) {
        for (ServerHandler serverHandler : serverHandlerList)
            serverHandler.send(str);
    }


    @Override
    public void onArriveMes(ServerHandler serverHandler, String mes) {

        System.out.println(serverHandler.getInfo() + mes);
        executorService.execute(() -> {
            synchronized (TcpServer.this) {
                for (ServerHandler server : serverHandlerList) {
                    if (server.equals(serverHandler))
                        continue;
                    server.send(mes);
                }
            }
        });
    }

    @Override
    public synchronized void onCloseSelf(ServerHandler serverHandler) {
        serverHandlerList.remove(serverHandler);
    }

    private class ServerListener extends Thread {
        private Selector selector;
        private boolean done = false;

        private ServerListener(Selector selector ) {
        this.selector = selector;
        }

        public void run() {
            super.run();
            System.out.println("服务器准备就绪～");
            // 等待客户端连接
            Selector selector = this.selector;
            while (!done) {

                try {
                    if (selector.select()==0){
                        if (done)
                            break;
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        if (done)
                            break;
                        SelectionKey next = iterator.next();
                        iterator.remove();
                        if (next.isAcceptable()){
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) next.channel();
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            ServerHandler clientHandler = new ServerHandler(socketChannel, TcpServer.this);
                            serverHandlerList.add(clientHandler);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("TcpServer --113");
                    continue;
                }
                // 客户端构建异步线程

            }
            System.out.println("服务器已关闭！");
        }

        void exit() {
            done = true;

        }
    }
}
