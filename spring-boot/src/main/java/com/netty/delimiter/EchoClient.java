package com.netty.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.omg.PortableInterceptor.INACTIVE;

/**
 *
 * @ClassName: EchoClient
 * @Description: TODO
 * @Author: Ian
 * @Date: 2018/3/16 9:34
 * @Version: 1.0 
 */
public class EchoClient {
    public void connect(int port, String host) throws InterruptedException {
        //   配置服务端NIO组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ChannelHandlerAdapter(){
                                private int counter = 0;
                                static final String ECHO_REQ = "Hi, LiLinFeng. Welcome to Netty. $_";
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    for (int i = 0 ;i < 10; i++) {
                                        ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
                                    }
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("This is " + ++counter + " time receive server: [" + msg + "]");
                                }

                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                                    ctx.flush();
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length >0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new EchoClient().connect(port, "127.0.0.1");
    }
}
