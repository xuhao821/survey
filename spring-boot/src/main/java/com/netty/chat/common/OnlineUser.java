package com.netty.chat.common;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

/**
 *
 * @ClassName: OnlineUser
 * @Description: 在线用户管理
 * @Author: Ian
 * @Date: 2018/3/16 15:46
 * @Version: 1.0
 */
public class OnlineUser {

    //用户表
    private static HashMap<Integer, ChannelHandlerContext> onlineUser = new HashMap<>();

    public static void put(Integer uid, ChannelHandlerContext uchc){
        onlineUser.put(uid, uchc);
    }
    public static void remove(Integer uid){
        onlineUser.remove(uid);
    }
    public static ChannelHandlerContext get(Integer uid){
        return onlineUser.get(uid);
    }
}
