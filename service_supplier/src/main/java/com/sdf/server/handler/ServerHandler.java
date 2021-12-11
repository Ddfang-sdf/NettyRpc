package com.sdf.server.handler;

import com.sdf.implement.HelloService;
import com.sdf.server.Application;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<String> {



    //服务端处理器只需要验证协议头,处理请求，响应请求就ok了
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println("msg=" + msg);

        if (msg.startsWith(Application.providerName)){
            String result = new HelloService().hello(msg.substring(msg.lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
