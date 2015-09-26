package com.practice.noyet.chatroom.socket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by noyet on 2015/9/26.
 */
public class ChatClient {

    public static Socket socket;
    /** 数据写入服务器端 */
    public static PrintWriter writer;
    /** 从服务器读取数据 */
    public static BufferedReader reader;

    public ChatClient() {
        createSocket();
    }

    /**
     * 获取ChatClient实例
     * @return ChatClient
     */
    public synchronized ChatClient getChatClient() {
        return new ChatClient();
    }

    /**
     * 创建Socket
     */
    public void createSocket() {
        try {
            /** 初始化Socket，参数：主机ip（服务器主机ip）  端口号 */
            socket = new Socket("192.168.1.142", 1314);
            /** 初始化写数据流 */
            writer = new PrintWriter(socket.getOutputStream());
            /** 初始化读数据流 */
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            /** 打印错误信息 */
            printErrorInfo("ChatClient.createSocket", e);
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送消息
     * @param chatMsg 信息内容
     */
    public void sendChatMsg(String chatMsg) {
        /** 将数据写入服务器端 */
        writer.println(chatMsg);
        /** 刷新输出流(写入服务器)，使Server马上收到该字符串 */
        writer.flush();
    }

    public String getChatMsg() {
        try {
            return reader.readLine();
        } catch (Exception e) {
            /** 打印错误信息 */
            printErrorInfo("ChatClient.getChatMsg", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭IO流
     */
    public void destroySocket() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            /** 打印错误信息 */
            printErrorInfo("ChatClient.destroySocket", e);
            e.printStackTrace();
        }
    }

    /**
     * 打印错误信息
     * @param tag 产生错误信息的方法
     * @param info 错误信息
     */
    private void printErrorInfo(String tag, Exception info) {
        Log.d(tag + " Error",  "Error:  " + info);
    }
}
