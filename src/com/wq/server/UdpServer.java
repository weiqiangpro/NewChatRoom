package com.wq.server;

import com.wq.utils.constants.ByteUtils;
import com.wq.utils.util.UDPConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.UUID;

public  class UdpServer {
    private static Provider PROVIDER = null;

    public static void start(int port) {
        System.out.println("UDP服务器开始监听");
        stop();
        String sn = UUID.randomUUID().toString();
         PROVIDER = new Provider(sn, port);
        PROVIDER.start();
    }

    public   static void stop() {
        if (PROVIDER != null) {
            PROVIDER.exit();
            PROVIDER=null;
        }
    }


    private static class Provider extends Thread {
        private final String sn;
        private final int port;
        private DatagramSocket ds = null;
        private boolean done = false;

        public Provider(String sn, int port) {
            this.sn = sn;
            this.port = port;
        }

        @Override
        public void run() {
            super.run();
            try {
                ds = new DatagramSocket(UDPConstants.PORT_SERVER);
                byte[] buffer = new byte[128];
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                while (!done) {
                    ds.receive(dp);
                    String clientIp = dp.getAddress().getHostAddress();
                    int clientPort = dp.getPort();
                    int clientDataLen = dp.getLength();
                    byte[] clientData = dp.getData();
                    boolean isValid = clientDataLen >= (UDPConstants.HEADER.length + 2 + 4)
                            && ByteUtils.startsWith(clientData, UDPConstants.HEADER);
                    System.out.println("UDP服务及接收到广播， ip:" + clientIp + "\tport:" + clientPort + "\tdataValid:" + isValid);
                    if (!isValid)
                        continue;

                    int index = UDPConstants.HEADER.length;
                    short cmd = (short) ((clientData[index++] << 8) | (clientData[index++] & 0xff));
                    int responsePort = (((clientData[index++]) << 24) |
                            ((clientData[index++] & 0xff) << 16) |
                            ((clientData[index++] & 0xff) << 8) |
                            ((clientData[index] & 0xff)));
                    if (cmd == 1 && responsePort > 0) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                        byteBuffer.put(UDPConstants.HEADER);
                        byteBuffer.putShort((short) 2);
                        byteBuffer.putInt(port);
                        byteBuffer.put(sn.getBytes());
                        DatagramPacket respondp = new DatagramPacket(buffer, byteBuffer.position(), dp.getAddress(), responsePort);
                        ds.send(respondp);
                        System.out.println("UDP服务器返回消息:" + clientIp + "\tport:" + responsePort);
                    } else {
                        System.out.println("cmd错误 cmd:" + cmd + "\tport:" + port);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }

        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        private void exit() {
            done = true;
            close();
        }
    }
}
