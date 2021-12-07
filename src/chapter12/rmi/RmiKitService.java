package chapter12.rmi;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 15:52
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiKitService extends Remote {
    //以下远程方法全部由学生端实现（即后面的第2步任务），由教师端进行回调

    //远程方法一 将ipv4格式字符串转为长整型
    public long ipToLong(String ip) throws RemoteException;

    //远程方法二 将长整型转为ipv4字符串格式
    public String longToIp(long ipNum) throws RemoteException;

    //远程方法三 将“-”格式连接的MAC地址转为Jpcap可用的字节数组
    public byte[] macStringToBytes(String macStr) throws RemoteException;

    //远程方法四 将Jpcap的byte[]格式的MAC地址转为"-"连接MAC字符串
    public String bytesToMACString(byte[] macBytes) throws RemoteException;
}
