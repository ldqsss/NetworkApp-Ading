package chapter12;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 13:34
 * @Author: 刘鼎谦-Ading
 * @file_desc:  创建服务端的远程接口实现类
 */

import chapter12.rmi.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
    private String name;
    public HelloServiceImpl() throws RemoteException {
    }
    public HelloServiceImpl(String name) throws RemoteException {
        this.name = name;
    }
    @Override
    public String echo(String msg) throws RemoteException {
        System.out.println("服务端完成一些echo方法相关任务......");
        return "echo: " + msg + " from " + name;
    }

    @Override
    public Date getTime() throws RemoteException {
        System.out.println("服务端完成一些getTime方法相关任务......");
        return new Date();
    }
    @Override
    public ArrayList<Integer> sort(ArrayList<Integer> list) throws RemoteException {
        System.out.println("服务端完成排序相关任务......");
        Collections.sort(list);
        return list;
    }


}
