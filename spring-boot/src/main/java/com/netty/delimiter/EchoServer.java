package com.netty.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 *
 * @ClassName: EchoServer
 * @Description: 分隔符
 * @Author: Ian
 * @Date: 2018/3/15 15:59
 * @Version: 1.0
 */
public class EchoServer {

    public void bind(int port) throws InterruptedException {
        //   配置服务端NIO组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                                ch.pipeline().addLast(
                                        new DelimiterBasedFrameDecoder(1024, delimiter));
                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast(new ChannelHandlerAdapter(){
                                    int counter = 0;
                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                        cause.printStackTrace();
                                        ctx.close();
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        String body = (String)msg;
                                        System.out.println("This is " + ++counter + " time receive client :["
                                                + body + "]");
                                            body += "$_";
                                        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
                                        ctx.writeAndFlush(echo);
                                    }
                                });
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new EchoServer().bind(port);
    }
}
