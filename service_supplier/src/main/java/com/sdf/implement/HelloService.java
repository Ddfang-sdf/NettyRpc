package com.sdf.implement;

import com.sdf.api.Hello;

public class HelloService implements Hello {

    private static int count;

    @Override
    public String hello(String msg) {
        return null == msg ?
                "hello,服务端已收到消费者的请求"
                :
                "hello,服务端收到消费者请求，入参["+msg+"],调用次数 count = "+ ++count;
    }
}
