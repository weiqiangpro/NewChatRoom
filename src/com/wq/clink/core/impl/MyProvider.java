package com.wq.clink.core.impl;

import com.wq.clink.core.IoProvider;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyProvider implements IoProvider, Closeable {

    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final AtomicBoolean inReg = new AtomicBoolean(false);
    private final AtomicBoolean outReg = new AtomicBoolean(false);
    private final Selector readSelector;
    private final Selector writeSelector;
    private final HashMap<SelectionKey, Runnable> inputHashMap = new HashMap<>();
    private final HashMap<SelectionKey, Runnable> outputHashMap = new HashMap<>();
    private final ExecutorService inputHandel = Executors.newFixedThreadPool(4);
    private final ExecutorService outputHandel = Executors.newFixedThreadPool(4);

    public MyProvider() throws IOException {
        this.readSelector = Selector.open();
        this.writeSelector = Selector.open();
        startRead();
        startWrite();
    }

    private void startRead() {
        Thread thread = new Thread(() -> {
            while (!isClosed.get()){
                try {
                    if (readSelector.select() == 0) {
                        waitSelection(inReg);
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {

                        if (selectionKey.isReadable()) {
                            selectionKey.interestOps(selectionKey.readyOps() & ~SelectionKey.OP_READ);
                            inputHandel.execute(inputHashMap.get(selectionKey));
                        }
                    }
                    selectionKeys.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "Clink IoSelectorProvider ReadSelector Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }


    private void startWrite() {
        Thread thread = new Thread(() -> {
            while (!isClosed.get()){
                try {
                    if (writeSelector.select() == 0) {
                        waitSelection(outReg);
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = writeSelector.selectedKeys();
                    for (SelectionKey selectionKey : selectionKeys) {

                        if (selectionKey.isValid()) {
                            selectionKey.interestOps(selectionKey.readyOps() & ~SelectionKey.OP_WRITE);
                            outputHandel.execute(outputHashMap.get(selectionKey));
                        }
                    }
                    selectionKeys.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "Clink IoSelectorProvider ReadSelector Thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    @Override
    public boolean writeRegister(SocketChannel channel, Runnable runnable) {
        return resign(channel,runnable,outReg,writeSelector,outputHashMap,SelectionKey.OP_WRITE);
    }
    @Override
    public boolean readRegister(SocketChannel channel, Runnable runnable) {
        return resign(channel,runnable,inReg,readSelector,inputHashMap,SelectionKey.OP_READ);
    }

    private boolean resign(SocketChannel channel,Runnable runnable,AtomicBoolean lock,Selector selector,HashMap hashMap,int op){
        synchronized (lock) {
            lock.set(true);
            try {
                selector.wakeup();
                SelectionKey key = null;
                if (channel.isRegistered()) {
                    key = channel.keyFor(selector);
                    if (key != null)
                        key.interestOps(key.readyOps() | op );
                }
                if (key == null) {
                    key = channel.register(selector, op);
                    hashMap.put(key, runnable);
                }
                return key!=null;
            } catch (ClosedChannelException e) {
                return false;
            } finally {
                lock.set(false);
                try {
                    lock.notify();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static void waitSelection(final AtomicBoolean locker) {
        synchronized (locker) {
            if (locker.get()) {
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void unReadRegister(SocketChannel channel) {
        if (channel.isRegistered()) {
            SelectionKey key = channel.keyFor(readSelector);
            if (key != null) {
                // 取消监听的方法
                key.cancel();
                inputHashMap.remove(key);
                readSelector.wakeup();
            }
        }
    }

    @Override
    public void unWriteRegister(SocketChannel channel) {
        if (channel.isRegistered()) {
            SelectionKey key = channel.keyFor(writeSelector);
            if (key != null) {
                // 取消监听的方法
                key.cancel();
                outputHashMap.remove(key);
                writeSelector.wakeup();
            }
        }
    }

    @Override
    public void close() throws IOException {

    }
}
