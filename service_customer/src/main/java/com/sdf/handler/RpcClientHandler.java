package com.sdf.handler;

import com.sdf.exceptions.InitYetException;
import com.sdf.exceptions.NoParamsException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

public class RpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {
    private ChannelHandlerContext context;
    private String result;
    private String param;


    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        result = msg;
        notify();//唤醒一个等待的线程，让它参与到cpu的竞争中去
    }

    @Override
    public synchronized Object call() throws Exception {

        //代理对象调用call，向服务端请求接口服务
        if (null == context) {
            throw new InitYetException("clien尚未初始化！");
        }
        if (null == param){
            throw new NoParamsException("没有参数！");
        }
        ChannelFuture future = context.writeAndFlush(param);
        boolean success = future.isSuccess();
        Throwable cause = future.cause();
        wait();//等待服务器返回结果后，channelRead0（）唤醒

        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
