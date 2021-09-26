package chapter02;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/9/14 15:13
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private int port = 8008;
    private ServerSocket serverSocket;   // define 变量: java.net.ServerSocket

    public TCPServer() throws IOException{
        serverSocket = new ServerSocket(port); // 创建serverSocket对象, using port 8008
        System.out.println("Server started, Listening to "+port+" port");
    }

    private PrintWriter getWriter(Socket socket) throws IOException{
        // get 输出缓冲区的addr
        OutputStream socketOut = socket.getOutputStream();
        //网络流写出,使用flush，这里在PrintWriter构造方法中,设为auto-flush ---true
        return new PrintWriter(
                new OutputStreamWriter(
                        socketOut,"utf-8"),true);
    }

    // 类似的,也要获得输入缓冲区的addr
    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(
                new InputStreamReader(socketIn, "utf-8"));
    }
    //single-client version, 即每一次只能与一个客户建立通信连接
    public void Service() {
        while(true) {
            Socket socket = null;
            try {
                //此处程序阻塞，监听并等待客户发起连接，有连接请求就生成一个套接字。
                socket = serverSocket.accept();

                //本地服务器控制台显示客户端连接的用户信息
                System.out.println("New connection accepted： " + socket.getInetAddress());
                BufferedReader br = getReader(socket);//定义字符串输入流
                PrintWriter pw = getWriter(socket);//定义字符串输出流

                //客户端正常连接成功，则发送服务器的欢迎信息，然后等待客户发送信息
                pw.println("From Server: Welcome ~ Service started! ");

                String msg = null;
                //此处程序阻塞，每次从输入流中读入一行字符串
                while ((msg = br.readLine()) != null) {
                    //如果客户发送的消息为"bye"，就结束通信
                    if (msg.equals("bye")) {
                        //向输出流中输出一行字符串,远程客户端可以读取该字符串
                        pw.println("From Server: Connection closed,service ended");
                        System.out.println("Client leaves");
                        break; //结束循环
                    }
                    //向输出流中输出一行字符串,远程客户端可以读取该字符串
                    // 正则表达式实现“人工智能”（扩展练习）
                    msg = msg.replaceAll("[吗?？]", "~") + "!";
                    pw.println("From Server:" + msg);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) {
                        socket.close(); //关闭socket连接及相关的输入输出流
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new TCPServer().Service();
    }
}

