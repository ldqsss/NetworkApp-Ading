package chapter09;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/11/9 15:24
 * @Author: 刘鼎谦-Ading
 * @Homework_des:  工具类
 */

public class IPNumTransfm {

    public static long ip2num(String ip){
        String[] ipArr = ip.split("\\.");
        long num = 0;
        for(int i=0;i<ipArr.length;i++){
            long valueOfSection = Long.parseLong(ipArr[i]);
            num = (valueOfSection << 8*(3-i)) | num;
            System.out.println(valueOfSection);
            System.out.println(num);
        }
        return num;
    }
    public static String num2ip(long i){
        return ((i>>24)&0xFF)+"."+((i>>16)&0xFF)+"."+((i>>8)&0xFF)+"."+((i)&0xFF);
    }
}
