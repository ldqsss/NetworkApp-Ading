package chapter05.groupServer;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/9/14 15:13
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupServer {
    private int port = 6511;   // 自定义监听在6511 （手机尾号
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newCachedThreadPool();;
    public static int clientCount = 0;
    public static Set<Socket> members = new CopyOnWriteArraySet<>();    // 静态的set，成员为socket

    /*频繁创建大量线程需要消耗大量系统资源。对于服务器，一般是使用线程池来管理和复用线程。
    线程池内部维护了若干个线程，没有任务的时候，这些线程都处于等待状态。如果有新任务，就分配一个空闲线程执行。
    如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理
    */

    public GroupServer() throws IOException{
        serverSocket = new ServerSocket(port);
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

    private void sendToAllMembers(String msg,String hostAddress) throws IOException{
        PrintWriter pw;
        OutputStream out;
        for (Socket tempSocket : members) { //members是什么类型的变量?
            out = tempSocket.getOutputStream();
            pw = new PrintWriter(
                    new OutputStreamWriter(out, "utf-8"), true);
            pw.println(hostAddress + " 发言：" + msg );
        }
    }

    //single-client version, 即每一次只能与一个客户建立通信连接
    public void Service() {
        while(true) {
            Socket socket = null;
            try {
                //此处程序阻塞，监听并等待客户发起连接，有连接请求就生成一个套接字。
                socket = serverSocket.accept();
                executorService.execute(new Handler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class Handler implements Runnable{
        private Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //本地服务器控制台显示客户端连接的用户信息
            System.out.println("New connection accepted： " + socket.getInetAddress().getHostAddress());
            GroupServer.clientCount += 1;
            try {
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
                    else if (msg.equals("来自教师服务器的连接")) {
                        //向输出流中输出一行字符串,远程客户端可以读取该字符串
                        pw.println("1");
                    }
                    else if (msg.equals("教师服务器再次发送信息")) {
                        //向输出流中输出一行字符串,远程客户端可以读取该字符串
                        pw.println("2");
                    }
                    //向输出流中输出一行字符串,远程客户端可以读取该字符串
                    // 正则表达式实现“人工智能”（扩展练习）
                    msg = msg.replaceAll("[吗?？]", "") + "!";
                    msg = msg.replaceAll("我", "You");
                    msg = msg.replaceAll("你","I");
                    pw.println("From Server:" + msg);
                    pw.println("来自服务器，重复发送: " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(socket != null)
                        socket.close(); //关闭socket连接及相关的输入输出流
                    GroupServer.clientCount -= 1;
                    System.out.println(String.format("目前有 %d 个client在线", clientCount));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new GroupServer().Service();
    }
}

