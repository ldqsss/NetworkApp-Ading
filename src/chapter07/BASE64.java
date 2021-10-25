package chapter07;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-10-25 10:05
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 */

import java.nio.charset.StandardCharsets;

public class BASE64 {
    public static String encode(String str){
        return new sun.misc.BASE64Encoder().encode(str.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        String userName="734200940@qq.com";
        String authCode="wxuhjbiaippcbbac"; // "njmchwijbooybfdh";
        System.out.println(encode(userName));
        System.out.printf(encode(authCode));
    }
}
