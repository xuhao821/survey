package com.netty.chat.chatClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 *
 * @ClassName: Client
 * @Description: 聊天客户端
 * @Author: Ian
 * @Date: 2018/3/16 14:54
 * @Version: 1.0
 */
public class Client {

    public static int UID = 8888;

    public void startClient(int port, String host) throws InterruptedException {
        ClientThread.clientHandler  = new ClientHandler();
        //   配置服务端NIO组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.weakCachingConcurrentResolver(null)));
                            ch.pipeline().addLast("frameDecoder",
                                    new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            ch.pipeline().addLast(ClientThread.clientHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
