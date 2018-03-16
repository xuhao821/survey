package com.survey;
/**
 *
 * @ClassName: Variable
 * @Description: reading java code
 * @Author: Ian
 * @Date: 2018/3/5 14:19
 * @Version: 1.0
 */
public class Variable {
    public static void main(String[] args) {
        // byte 数据类型是8位、有符号的，以二进制补码表示的整数；
        byte small = 127;
        byte negative_small = -128;
        int i = 130;
        System.out.println(Integer.toString(small,2));
        System.out.println(Integer.toString(negative_small,2));

        System.out.println((byte)i);

        Integer a = Integer.MAX_VALUE;
        Integer b = Integer.MIN_VALUE;
        System.out.println(Integer.toString(a, 10));
        Character c = '中';
        System.out.println(Integer.toString(Character.MAX_VALUE, 10));

        Integer x = 130, y = 131;
        swap(x,y);
        System.out.printf("x = %d, y = %d", x, y);
        System.out.println(" ");
    }

    public static void swap(Integer a, Integer b){
        System.out.printf("x = %d, y = %d", a, b);
        System.out.println(" ");
        Integer temp = a;
        a = b;
        b = temp;
        System.out.printf("x = %d, y = %d", a, b);
        System.out.println(" ");
    }
}
