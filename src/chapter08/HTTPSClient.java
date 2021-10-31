package chapter08;/*
* @project: NetworkApp-Ading
* @Created-Time: 2021-10-31 10:58
* @Author: 刘鼎谦-Ading
* @file_desc:
*/

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class HTTPSClient {
    private SSLSocket socket;
    private SSLSocketFactory sslSocketFactory;
    private PrintWriter pw;
    private BufferedReader br;

    public HTTPSClient(String ip, String port) throws IOException {
        sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        socket = (SSLSocket) sslSocketFactory.createSocket(ip,Integer.parseInt(port));
        OutputStream socketOut = socket.getOutputStream();
        pw = new PrintWriter( // 设置最后一个参数为true，表示自动flush数据
                new OutputStreamWriter(//设置utf-8编码
                        socketOut, "utf-8"), true);

        //得到网络输入字节流地址，并封装成网络输入字符流
        InputStream socketIn = socket.getInputStream();
        br = new BufferedReader(
                new InputStreamReader(socketIn, "utf-8"));
    }
    public void send(String msg) {
        //输出字符流，由Socket调用系统底层函数，经网卡发送字节流
        pw.println(msg);
    }

    public String receive() {
        String msg = null;
        try {
            //从网络输入字符流中读信息，每次只能接受一行信息
            //如果不够一行（无行结束符），则该语句阻塞，
            // 直到条件满足，程序才往下运行
            msg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void close() {
        try {
            if (socket != null) {
                //关闭socket连接及相关的输入输出流,实现四次握手断开
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
