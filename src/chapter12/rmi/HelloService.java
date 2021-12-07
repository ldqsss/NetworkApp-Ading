package chapter12.rmi;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 13:29
 * @Author: 刘鼎谦-Ading
 * @file_desc: 创建远程接口
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

public interface HelloService extends Remote {
    public String echo(String msg) throws RemoteException;
    public Date getTime() throws RemoteException;
    public ArrayList<Integer> sort(ArrayList<Integer> list) throws RemoteException;
}
