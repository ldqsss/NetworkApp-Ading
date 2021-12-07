package chapter12.server;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-30 13:46
 * @Author: 刘鼎谦-Ading
 * @file_desc: 实现一个远程服务发布程序

 远程服务发布程序HelloServer启动后，
 即使main()方法执行完毕，程序仍然不会结束运行，因为它向注册器注册了远程对象，
 注册器一直引用这个远程对象，使得这个远程对象不会结束生命周期，
 因而HelloServer程序也不会结束运行，远程对象一直为客户端提供服务。
 */

import chapter12.HelloServiceImpl;
import chapter12.rmi.HelloService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloServer {
    public static void main(String[] args) {
        try {
//对于有多个网卡的机器，建议用下面的命令绑定固定的ip
            System.setProperty("java.rmi.server.hostname","10.173.40.178");
            //(1)启动RMI注册器，并监听在1099端口（这是RMI的默认端口，正如HTTP的默认端口是80）
            Registry registry = LocateRegistry.createRegistry(1099);

            //(2)实例化远程服务对象，如果有多个远程接口，只实例化自己实现的接口（为什么可能有没有实例化的接口？）
            HelloService helloService = new HelloServiceImpl("小明的远程服务");

            //(3)用助记符来注册发布远程服务对象,助记符建议和远程服务接口命名相同，这样更好起到”助记"效果
            registry.rebind("HelloService",helloService);
            //也可以用另外一种方式进行注册发布，建议用上面的方式
            //Naming.rebind("HelloService",helloService);

            System.out.println("发布了一个HelloService RMI远程服务");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
