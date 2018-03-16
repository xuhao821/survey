package com.netty.chat.chatClient;

import com.netty.chat.common.IMConfig;
import com.netty.chat.common.IMMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

/**
 *
 * @ClassName: ClientHandler
 * @Description: 聊天客户端帮助类
 * @Author: Ian
 * @Date: 2018/3/16 14:50
 * @Version: 1.0
 */
public class ClientHandler extends ChannelHandlerAdapter implements IMConfig {

    private ChannelHandlerContext ctx;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("与服务器断开连接:"+cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String){
            System.out.println(msg);
        }else{
            IMMessage m = (IMMessage)msg;
            System.out.println(m.getUid() + ":" + m.getMsg());
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("连接服务器成功");
        this.ctx = ctx;
        IMMessage message = new IMMessage(
                APP_IM,
                CLIENT_VERSION,
                Client.UID,
                TYPE_CONNECT,
                SERVER_ID,
                MSG_EMPTY);
        sendMsg(message);
    }

    /**
     * 发送消息
     * @param msg
     * @return
     * @throws IOException
     */
    public boolean sendMsg(IMMessage msg) throws IOException {
        System.out.println("client:" + msg);
        ctx.channel().writeAndFlush(msg);
        return msg.getMsg().equals("q") ? false : true;
    }
}
