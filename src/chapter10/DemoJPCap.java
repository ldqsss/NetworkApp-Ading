package chapter10;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-16 14:45
 * @Author: 刘鼎谦-Ading
 * @file_desc:  测试类，测试Jpcap
 */


import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;

public class DemoJPCap {
    public static void main(String[] args) {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();  //可返回NetworkInterface 类型的数组
        for(int i=0; i< devices.length; i++){
            System.out.println(i+':'+devices[i].name +' '+
                    devices[i].description);
            String mac = "";   // mac 地址6bytes
            for(byte b:devices[i].mac_address){
                mac = mac + Integer.toHexString(b&0xff) + ":";
                //和0xff相与，这样就只保留低 8位
            }
            System.out.println("MAC address: "+ mac.substring(0, mac.length()-1));
            for (NetworkInterfaceAddress addr : devices[i].addresses) {
                System.out.println(" address:"+addr.address + " " + addr.subnet + " "+ addr.broadcast );
            }
            System.out.println("*****************************************************************************"+"\n\n");
        }

    }
}
