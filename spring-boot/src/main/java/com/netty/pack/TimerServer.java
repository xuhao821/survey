package com.netty.pack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 *
 * @ClassName: TimerServer
 * @Description: netty timer
 * @Author: Ian
 * @Date: 2018/3/14 16:21
 * @Version: 1.0 
 */
@SuppressWarnings("all")
public class TimerServer {
  public void bind(int port) throws Exception {
    //   配置服务端NIO组
     EventLoopGroup bossGroup = new NioEventLoopGroup();
     EventLoopGroup workerGroup = new NioEventLoopGroup();
     try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup,workerGroup)
              .channel(NioServerSocketChannel.class)
              .option(ChannelOption.SO_BACKLOG, 1024)
              .childHandler(new ChildChannelHandler());
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
   new TimerServer().bind(port);
 }

}

/**
 *
 * @ClassName: ChannelHandler
 * @Description: 通道帮助类
 * @Author: Ian
 * @Date: 2018/3/14 17:11
 * @Version: 1.0
 */
    @SuppressWarnings("all")
 class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
      ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
      ch.pipeline().addLast(new StringDecoder());
      ch.pipeline().addLast(new TimeServerHandler());
  }
 }

/**
 *
 * @ClassName: TimeServerHandler
 * @Description: 帮助类
 * @Author: Ian
 * @Date: 2018/3/14 17:09
 * @Version: 1.0
 */
@SuppressWarnings("all")
class TimeServerHandler extends ChannelHandlerAdapter{

    private int counter;
 @Override
 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
     String body = (String) msg;

     System.out.println("The time server receive order : " + body + "; the counter is : " + ++counter);
     String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
             new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
            currentTime = currentTime + System.getProperty("line.separator");

     ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
         ctx.write(resp);
 }

 @Override
 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
     ctx.close();
 }

 @Override
 public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
 }
}