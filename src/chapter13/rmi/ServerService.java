package chapter13.rmi;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-12-05 15:58
 * @Author: 刘鼎谦-Ading
 * @file_desc: 服务端远程接口ServerService
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 服务端远程对象接口，该接口为客户端提供服务
 * 由服务端实现以下远程方法
 */
public interface ServerService extends Remote {
    public String addClientToOnlineGroup(String client, ClientService clientService) throws RemoteException;

    public String removeClientFromOnlineGroup(String client,ClientService clientService) throws RemoteException;

    public void sendPublicMsgToServer(String client,String msg) throws RemoteException;
}
