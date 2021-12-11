package com.sdf;

import com.sdf.api.Hello;
import com.sdf.client.RpcClient;

public class Application {
    //定义协议头
    public final static String providerName = "bM4FyMM5D5c#";
    public final static String HOST = "127.0.0.1";
    public final static Integer PORT = 8848;

    public static void main(String[] args) throws InterruptedException {
        RpcClient rpcClient = new RpcClient();
        Hello service = rpcClient.getBean(Hello.class, providerName);

        for (; ; ) {
            Thread.sleep(2 * 1000);
            //通过代理对象调用服务提供者的方法(服务)
            String result = service.hello("你好 dubbo~");
            System.out.println("调用的结果 res= " + result);
        }

    }
}
