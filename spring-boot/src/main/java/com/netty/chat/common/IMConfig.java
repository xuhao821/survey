package com.netty.chat.common;

public interface IMConfig {
    /**客户端配置*/
    int     CLIENT_VERSION = 1;         //版本号
    /**服务端配置*/
    String  SERVER_HOST = "127.0.0.1";  //服务器IP
    int     SERVER_PORT = 8080;         //服务器端口
    /**消息相关*/
    int     SERVER_ID   = 0;            //表示服务器消息
    byte    APP_IM = 1;         //即时通信应用ID为1
    byte    TYPE_CONNECT = 0;   //连接后第一次消息确认建立连接和发送认证信息
    byte    TYPE_MSG_TEXT = 1;  //文本消息
    String  MSG_EMPTY = "";     //空消息
}