package com.wq.utils.constants;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Author: weiqiang
 * @Time: 2020/3/6 下午10:05
 */
public class CloseUtil {
    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
