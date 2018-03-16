package com.netty.chat.chatClient;

import com.netty.chat.common.IMConfig;
import com.netty.chat.common.IMMessage;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 * @ClassName: ClientThead
 * @Description: 启动客户端
 * @Author: Ian
 * @Date: 2018/3/16 16:23
 * @Version: 1.0
 */
public class ClientThread extends Thread implements IMConfig {

    public static ClientHandler clientHandler = null;

    @Override
    public void run() {
        try {
            runServiceCMD();
            new Client().startClient(SERVER_PORT, SERVER_HOST);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("启动客户端失败");
        }

    }


    /**
     * 启动控制台输入
     */
    public void runServiceCMD(){
        new Thread(() -> {
            IMMessage message = new IMMessage(
                    APP_IM,
                    CLIENT_VERSION,
                    Client.UID,
                    TYPE_MSG_TEXT,
                    Client.UID,
                    MSG_EMPTY);
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println("input = " + scanner.next());
                do{
                    message.setMsg(scanner.nextLine());
                    System.out.println(ClientThread.clientHandler);
                }
                while (ClientThread.clientHandler.sendMsg(message));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("启动客户端控制台失败");
            }
        }).start();
    }


    public static void main(String[] args) {
        new ClientThread().start();
    }
}


