package cloud;


import com.wq.client.TCPClient;
import com.wq.client.bean.ServerInfo;
import com.wq.clink.Context;
import com.wq.clink.core.impl.MyProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Client {
    public static void main(String[] args) throws IOException {
        Context.setup(new MyProvider());
//        ServerInfo info = UDPSearcher.searchServer(10000);
//        System.out.println("Server:" + info);
        ServerInfo info = new ServerInfo(8888,"120.79.87.220","fdsfds");
        if (info != null) {
            TCPClient tcpClient = null;
            try {
                tcpClient = TCPClient.startWith(info);
                if (tcpClient == null) {
                    return;
                }
                write(tcpClient);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (tcpClient != null) {
                    tcpClient.exit();
                }
            }
        }
    }

    private static void write(TCPClient tcpClient) throws IOException {
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        do {
            String str = input.readLine();
            tcpClient.send(str);
            if ("00bye00".equalsIgnoreCase(str)) {
                break;
            }
        } while (true);
    }

}
