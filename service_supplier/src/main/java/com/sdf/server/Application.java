package com.sdf.server;

import java.net.InetSocketAddress;

public class Application {

    //定义协议头
    public final static String providerName = "bM4FyMM5D5c#";
    public final static String HOST = "127.0.0.1";
    public final static Integer PORT = 8848;
    public static void main(String[] args) {
        RpcServer.startServer(new InetSocketAddress(HOST,PORT));
    }
}
