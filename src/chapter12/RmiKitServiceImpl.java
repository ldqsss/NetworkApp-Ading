package chapter12;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 15:59
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 */

import chapter12.rmi.RmiKitService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiKitServiceImpl extends UnicastRemoteObject implements RmiKitService {

    public RmiKitServiceImpl() throws RemoteException {
    }

    @Override
    public long ipToLong(String ip) throws RemoteException {
        return 2;

    }

    @Override
    public String longToIp(long ipNum) throws RemoteException {
        return null;
    }

    @Override
    public byte[] macStringToBytes(String macStr) throws RemoteException {
        return new byte[0];
    }

    @Override
    public String bytesToMACString(byte[] macBytes) throws RemoteException {
        return null;
    }
}
