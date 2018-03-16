package com.netty.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.io.RandomAccessFile;

/**
 *
 * @ClassName: FileServer
 * @Description: 文件传输服务
 * @Author: Ian
 * @Date: 2018/3/16 10:45
 * @Version: 1.0
 */
@SuppressWarnings("all")
public class FileServer {
    public void bind(int port) throws Exception {
        //   配置服务端NIO组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(null)));
                            ch.pipeline().addLast(new ChannelHandlerAdapter(){
                                private int byteRead;
                                private volatile int start = 0;
                                private String file_dir = "D:";
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    if (msg instanceof FileUploadFile){
                                        FileUploadFile ef = (FileUploadFile) msg;
//                                        ctx.writeAndFlush(Unpooled.copiedBuffer("成功".getBytes()));
                                        byte[] bytes = ef.getBytes();

                                        byteRead = ef.getEndPos();
                                        String md5 = ef.getFile_md5();//文件名
                                        String path = file_dir + File.separator + md5;
                                        File file = new File(path);
                                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                                        randomAccessFile.seek(start);
                                        randomAccessFile.write(bytes);
                                        start = start + byteRead;
                                        if (byteRead > 0) {
                                            ctx.writeAndFlush(start);
                                        } else {
                                            randomAccessFile.close();
                                            ctx.close();
                                        }
                                    }
                                }
                            });
                        }
                    });
            // 绑定端口，同步等待成功
            ChannelFuture channelFuture = serverBootstrap.bind("127.0.0.1", port).sync();
            // 等待服务端关闭端口
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new FileServer().bind(port);
    }

}
