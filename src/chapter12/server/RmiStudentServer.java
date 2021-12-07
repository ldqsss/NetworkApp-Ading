package chapter12.server;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 15:57
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 */

import chapter12.RmiKitServiceImpl;
import chapter12.rmi.RmiKitService;


import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiStudentServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            RmiKitService rmiKitService = new RmiKitServiceImpl();
            registry.rebind("RmiKitService",rmiKitService);

        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
