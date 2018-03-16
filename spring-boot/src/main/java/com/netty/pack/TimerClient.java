package com.netty.pack;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 *
 * @ClassName: TimerClient
 * @Description: 定时器客户端
 * @Author: Ian
 * @Date: 2018/3/14 17:43
 * @Version: 1.0
 */
@SuppressWarnings("all")
public class TimerClient {

    public void connect(int port, String host) throws InterruptedException {
        //   配置服务端NIO组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            // 发起异步连接请求
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 等待客户端连接关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new TimerClient().connect(port, "127.0.0.1");
    }
}

/**
 *
 * @ClassName: TimeClientHandler
 * @Description: 定时器帮助类
 * @Author: Ian
 * @Date: 2018/3/14 17:45
 * @Version: 1.0
 */
@SuppressWarnings("all")
class TimeClientHandler extends ChannelHandlerAdapter {

    private int counter;
    private byte[] req;

    public TimeClientHandler() {
          req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i=0; i<100;i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
          String body = (String) msg;
          System.out.println("Now is " + body + "; the counter is : " + ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception :" + cause.getMessage());
        ctx.close();
    }
}
