package com.bjpowernode.settings;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;
import org.junit.Test;

public class test01 {
    public static void main(String[] args) {
        //失效时间
        String expireTime = "2021-06-03 00:00:00";
        String currenTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currenTime);
        System.out.println(count);
        //锁定状态
        String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("账号已锁定");
        }
        //允许访问的IP地址
        String ip = "192.168.1.3";
        String allowIps = "192.168.1.1";
        if (allowIps.equals(ip)){
            System.out.println("有效的IP地址，允许登录系统");
        }else {
            System.out.println("IP地址受限，请联系管理员");
        }
        //密码
        String pwd = "123456";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);


    }

    @Test
    public void testsave(){
        System.out.println(123);
    }



}
