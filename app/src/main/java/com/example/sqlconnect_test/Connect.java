package com.example.sqlconnect_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Connect {
    public Socket socket;
    public InputStream inputStream;
    public OutputStream outputStream;
    public static String ip="192.168.43.57";
    public static int port=9988;
    public Connect(String ip, int port) throws IOException {
        while (socket==null){
            socket=new Socket(ip,port);
            socket.setSoTimeout(50000);
        }
    }
    public void post(String message) throws IOException {
        outputStream = socket.getOutputStream();
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        outputStream.flush();
    }
    public String get() throws IOException {
        inputStream = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String reply = null;
        StringBuilder sb = new StringBuilder();
        reply=br.readLine();
//        String data[]=reply.split("_");
//        sb.append("教室："+data[0]+"已到："+data[1]+"未到："+data[2]+"总共："+data[3].replace("'",""));
        sb.append(reply);
        return sb.toString();
    }

    public void relase() throws IOException {
        if (inputStream!=null){
            inputStream.close();
        }
        if (outputStream!=null){
            outputStream.close();
        }
        if(socket!=null){
            socket.close();
        }

    }

}
