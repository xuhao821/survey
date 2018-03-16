package com.survey;

import java.util.Random;

/**
 *
 * @ClassName: Survey
 * @Description: TODO
 * @Author: Ian
 * @Date: 2018/3/2 15:19
 * @Version: 1.0
 */
public class Survey {
    public static void main(String[] args) {
//        //返回Java虚拟机试图使用的最大内存量。
//        long maxMemory = Runtime.getRuntime().maxMemory();
//        //返回Java虚拟机中的内存总量。
//        Long totalMemory = Runtime. getRuntime().totalMemory();
//        System.out.println("MAX_MEMORY ="+maxMemory +"(字节)、"+(maxMemory/(double)1024/1024) + "MB");
//        System.out.println("TOTAL_ MEMORY = "+totalMemory +"(字节)"+(totalMemory/(double)1024/1024) + "MB");
        long maxMemory = Runtime.getRuntime().maxMemory();//返回Java虚拟机试图使用的最大内存量。
        Long totalMemory = Runtime. getRuntime().totalMemory();//返回Java虚拟机中的内存总量。
        System.out.println("MAX_MEMORY ="+maxMemory +"(字节)、"+(maxMemory/(double)1024/1024) + "MB");
        System.out.println("TOTAL_ MEMORY = "+totalMemory +"(字节)"+(totalMemory/(double)1024/1024) + "MB");
        String str = "www.baidu.com";
        while(true){
            str += str + new Random().nextInt(88888888) + new Random().nextInt(99999999);
        }
    }
}
