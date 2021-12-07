package chapter13.client;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-12-05 16:07
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 */

import chapter13.rmi.ClientService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientServiceImpl extends UnicastRemoteObject implements ClientService {

    //获取客户端窗体变量（第3步创建该客户端窗体）
    private RmiClientFX rmiClientFX;

    public ClientServiceImpl(RmiClientFX rmiClientFX) throws RemoteException{
        this.rmiClientFX = rmiClientFX;
    }

    @Override
    public void showMsgToClient(String msg) throws RemoteException {
        rmiClientFX.appendMsg(msg);
    }


}
