package com.sdf.client;

import com.sdf.Application;
import com.sdf.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.*;

public class RpcClient{

    private static RpcClientHandler rpcClientHandler;

    //创建调用服务的线程池
    private ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>()
            );

    private static void initClient(){
        rpcClientHandler = new RpcClientHandler();
        EventLoopGroup group = null;
        try {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(rpcClientHandler);
                        }
                    }).connect(Application.HOST,Application.PORT).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public <T> T getBean(final Class<T> clazz,final String providerName){

        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{clazz},((proxy, method, args) -> {
            if (rpcClientHandler == null){
                initClient();
            }
            rpcClientHandler.setParam(providerName + args[0]);

            return executor.submit(rpcClientHandler).get();
        }));
    }



}
