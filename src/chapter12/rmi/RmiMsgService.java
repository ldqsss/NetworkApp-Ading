package chapter12.rmi;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 15:51
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 /**
 * 教师端和学生端各自一份的远程服务接口，定义了2个远程方法
 * 以下远程方法全部由教师端实现，学生端调用
 */


import java.rmi.Remote;
import java.rmi.RemoteException;
public interface RmiMsgService extends Remote{
    //声明远程方法一，用于学生发送信息给教师端，该方法由教师端实现，学生端调用
    public String send(String msg) throws RemoteException;

    //声明远程方法二 用于学生发送学号和姓名给教师端，该方法由教师端实现，学生端调用
    public String send(String yourNo, String yourName) throws RemoteException;
}
