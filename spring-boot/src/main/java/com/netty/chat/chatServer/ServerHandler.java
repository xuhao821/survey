package com.netty.chat.chatServer;

import com.netty.chat.common.IMMessage;
import com.netty.chat.common.OnlineUser;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import javax.management.ObjectName;
import java.io.IOException;

/**
 *
 * @ClassName: ServerHandler
 * @Description: 聊天服务器助类
 * @Author: Ian
 * @Date: 2018/3/16 14:20
 * @Version: 1.0
 */
public class ServerHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.err.println("服务端Handler创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.err.println("服务端Handler移除...");
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("与客户端断开连接:"+cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.err.println("注册channel...");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        System.err.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMMessage message = (IMMessage)msg;
        if (0 == message.getMsgType()){
            OnlineUser.put(message.getUid(), ctx);
            ctx.writeAndFlush("登陆成功");
        } else {
            ChannelHandlerContext receiverCtx = OnlineUser.get(message.getReceiveId());
            if(receiverCtx == null){
                message.setMsg("对方不在线！");
                OnlineUser.get(message.getUid()).writeAndFlush(message);
            }
            else
                receiverCtx.writeAndFlush(message);
        }
    }
    /**
     * 发送消息
     */
    public boolean sendMsg(IMMessage msg) throws IOException {
        System.err.println("服务器推送消息:"+msg);
        ctx.writeAndFlush(msg);
        return msg.getMsg().equals("q") ? false : true;
    }
}
