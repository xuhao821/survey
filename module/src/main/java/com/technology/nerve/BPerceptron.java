package com.technology.nerve;


/**
 *
 * @ClassName: BPerceptron
 * @Description: 神经网络
 * @Author: Ian
 * @Date: 2018/3/6 17:42
 * @Version: 1.0
 */
public class BPerceptron {
   static double[][] qz = new double[10][2];
   static {
       for (int i=0;i<10;i++){
           System.out.println(Math.random() * 10);
           qz[i][0] =  Math.random() * 10d;
           qz[i][1] =  Math.random() * 10d;
       }
   }

    public static void main(String[] args) {
        double z = 0.0;
        for (double[] doubles : qz) {
            z += (doubles[0]*doubles[1]);
//            System.out.println(doubles[0]*doubles[1]);
            System.out.println(z);
        }
    }
}
