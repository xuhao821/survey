package com.technology.nerve;

/**
 *
 * @ClassName: Perceptron
 * @Description: 神经网络感知器
 * @Author: Ian
 * @Date: 2018/3/6 11:20
 * @Version: 1.0
 */
public class Perceptron {
    /**
     * 学习率
     */
    private final float learnRate;

    /**
     * 学习次数
     */
    private final int studyCount;

    /**
     * 阈值
     */
    private float e;

    /**
     * 权值
     * 因为判断整数正负只需要一条输入，所以这里只有一个权值，多条输入可以设置为数组
     */
    private float w;

    /**
     * 每次学习的正确率
     */
    private float[] correctRate;

    //

    /**
     * 构造函数初始化学习率，学习次数，权值、阈值初始化为0
     * @param learnRate 学习率（取值范围 0 < learnRate < 1）
     * @param studyCount 学习次数
     */
    public Perceptron(float learnRate, int studyCount) {
        this.learnRate = learnRate;
        this.studyCount = studyCount;

        this.e = 0;
        this.w = 0;

        this.correctRate = new float[studyCount];
    }


    /**
     * 学习函数，samples 是一个包含输入数据和分类结果的二维数组，
     * samples[][0] 表示输入数据
     * samples[][1] 表示正确的分类结果
     * @param samples 训练数据
     */
    public void fit(int[][] samples) {
        int sampleLength = samples.length;

        for(int i = 0 ; i < studyCount ; i ++) {
            int errorCount = 0;

            for (int[] sample : samples) {

                //  新的权值=原权值+学习速率×该节点的误差×激励函数的导函数的值（f(e)的倒数）×与该节点相连的输入值
                float update = learnRate * (sample[1]-predict(sample[0]));


                //更新权值、阈值
                w += update * sample[0];
                System.out.printf("权限 = %f, 阀值 = %f ，学习率 = %d \n" , w, e, sample[1]);
//                System.out.printf("权值 ： %f \n", w);

                e += update;


                //计算错误次数
                if (update != 0)
                    errorCount++;
            }

            //计算此次学习的正确率
            correctRate[i] = 1 - errorCount * 1.0f / sampleLength;
        }
    }

    /**
     * 求和函数，模拟求和结点操作 输入数据 * 权值
     * @param num 输入数据
     * @return 求和结果 z
     */
    private float sum(int num) {
        return num * w + e;
    }

    /**
     * 激活函数，通过求和结果 z 和阈值 e 进行判断
     * @param num 输入数据
     * @return 分类结果
     */
    public int predict(int num) {
        return sum(num) >= 0 ? 1 : -1;
    }

    /**
     * 打印正确率
     */
    public void printCorrectRate() {
        for (int i = 0 ; i < studyCount ; i ++)
            System.out.printf("第%d次学习的正确率 -> %.2f%%\n",i + 1,correctRate[i] * 100);
    }
    /**
     * 生成训练数据
     * @return 训练数据
     */
    private static int[][] genStudyData() {
        //这里我们取 -100 ~ 100 之间的整数，大于0的设为模式 y = 1，反之为 y = -1
        int[][] data = new int[201][2];

        for(int i = -100 , j = 0; i <= 100 ; i ++ , j ++) {
            data[j][0] = i;
            data[j][1] = i >= 0 ? 1 : -1;
        }

        return data;
    }

    /**
     * 生成训练数据
     * @return 训练数据
     */
    private static int[][] genStudyData2() {
        //这里我们取 1~250 之间的整数，大于125的设为模式 y = 1，反之为 y = -1
        int[][] data = new int[250][2];

        for(int i = 1 , j = 0; i <= 250 ; i ++ , j ++) {
            data[j][0] = i;
            data[j][1] = i >= 125 ? 1 : -1;
        }

        return data;
    }
    public static void main(String[] args) {
        //这里的学习率和训练次数可以根据情况人为调整
        Perceptron perceptron = new Perceptron(0.4f,500);

        perceptron.fit(genStudyData2());
        perceptron.fit(genStudyData2());
        perceptron.fit(genStudyData2());
        perceptron.printCorrectRate();

        System.out.println(perceptron.predict(-1));
        System.out.println(perceptron.predict(126));
    }
}

