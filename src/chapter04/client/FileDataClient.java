package chapter04.client;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/9/26 17:26
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
 */

import java.io.*;
import java.net.Socket;

public class FileDataClient {
    private Socket dataSocket;

    public FileDataClient(String ip,String port) throws IOException {  //要功能是向服务器的数据端口请求连接；
        //主动向服务器发起连接，实现TCP的三次握手过程
        //如果不成功，则抛出错误信息，其错误信息交由调用者处理
        dataSocket = new Socket(ip, Integer.parseInt(port));
    }

    public void getFile(File saveFile) throws IOException {
            /***
             * 该方法主要功能是先在本地新建一个空文件，
             * 向服务器发送其文件名（基于字符串的字节流包装操作），
             * 然后接收网络文件数据并保存为本地的这个文件
             * （基于文件的字节流包装操作），关闭数据套接字。
             * */
        if (dataSocket != null) { // dataSocket是Socket类型的成员变量


            FileOutputStream fileOut = new FileOutputStream(saveFile);//新建本地空文件
            byte[] buf = new byte[1024]; // 用来缓存接收的字节数据
            //网络字节输入流
            InputStream socketIn = dataSocket.getInputStream();
            //网络字节输出流
            OutputStream socketOut = dataSocket.getOutputStream();

            //(2)向服务器发送请求的文件名，字符串读写功能
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socketOut, "utf-8"), true);
            pw.println(saveFile.getName());

            //(3)接收服务器的数据文件，字节读写功能
            int size = 0;
            while ((size = socketIn.read(buf)) != -1) {//读一块到缓存，读取结束返回-1
                fileOut.write(buf, 0, size); //写一块到文件
            }
            fileOut.flush();//关闭前将缓存的数据全部推出
            //文件传输完毕，关闭流
            fileOut.close();
            if (dataSocket != null) {
                dataSocket.close();
            }
        } else {
            System.err.println("连接ftp数据服务器失败");
        }
    }



}
