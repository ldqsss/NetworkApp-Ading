package chapter06.server;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/10/19 15:23
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
 */
import chapter06.client.UDPClient;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;


public class UDPServer {
    private int serverPort;
    private DatagramSocket serverSocket;//UDP套接字
    public InetAddress remoteIP;
    public int remotePort;


    //用于接收数据的报文字节数组缓存最大容量，字节为单位
    private static final int MAX_PACKET_SIZE = 512;

    //private static final int MAX_PACKET_SIZE = 65507;
    public UDPServer(int serverPort) throws IOException {   // 构造，目标udp客户端的ip、port

        //创建一个UDP套接字，选定一个未使用的UDP端口绑定, 作为udpserver
        serverSocket = new DatagramSocket(serverPort);
        //server 不能设置接收数据超时
//    socket.setSoTimeout(30000);
    }

    //定义一个数据的发送方法
    public void send(String msg, InetAddress remoteIP, int remotePort) {
        try {
            //将待发送的字符串转为字节数组
            byte[] outData = msg.getBytes("utf-8");
            //构建用于发送的数据报文，构造方法中传入远程通信方（服务器)的ip地址和端口
            DatagramPacket outPacket = new DatagramPacket(outData, outData.length, remoteIP, remotePort);
            //给UDPServer发送数据报
            serverSocket.send(outPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //定义数据接收方法
    public String receive() {
        /*
        * 与TCP不同，小负荷的UDP服务器往往不是多线程的。
        * 由于UDP是同一个套接字对应多个客户端，对于UDP服务端，可以不需要采用多线程方式
        * */
        String msg;
        //先准备一个空数据报文
        DatagramPacket inPacket = new DatagramPacket(
                new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
        try {
            //读取报文，阻塞语句，有数据就装包在inPacket报文中，装完或装满为止。
            serverSocket.receive(inPacket);
            //将接收到的字节数组转为对应的字符串
            remoteIP =  inPacket.getAddress();
            remotePort = inPacket.getPort();

            msg = new String(inPacket.getData(),
                    0, inPacket.getLength(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            msg = null;
        }
        return msg;
    }
    public void service(){
        while (true) { //等待客户端
            String mm = receive(); //阻塞等待,来了哪个客户就服务哪个客服  ……处理请求，发回响应…… }
            String msg = "20191002883&刘鼎谦&"+new Date().toString()+ "&"+mm;
            send(msg,remoteIP,remotePort);
        }
    }

    public static void main(String[] args) throws IOException {
        new UDPServer(6511).service();
    }
}

