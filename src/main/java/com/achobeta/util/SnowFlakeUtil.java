package com.achobeta.util;
public class SnowFlakeUtil{
    //起始时间戳
    private static long startTimeStamp;
    //机器ID
    private static long workID;
    //数据中心ID
    private static long  dataCenterID;
    //序列号
    private static long sequence;
    //数据中心ID移动位数
    private static long dataCenterIndex;
    //机器ID移动位数
    private static long workIDIndex;
    //时间戳移动位数
    private static long timeStampIndex;
    //记录上一次时间戳
    private static long lastTimeStamp;
    //序列号掩码
    private static long sequenceMask;
    //序列号长度12位
    private static long sequenceLength;

    //初始化数据
    static {
        startTimeStamp = 1577808000000L;
        //设置机器编号 1
        workID = 1L;
        //设置数据中心ID 1
        dataCenterID = 1L;
        //起始序列号 0开始
        sequence = 0L;
        //数据中心位移位数
        dataCenterIndex = 12L;
        //机器ID位移位数
        workIDIndex = 17L;
        //时间戳位移位数
        timeStampIndex = 22L;
        //记录上次时间戳
        lastTimeStamp = -1L;
        //序列号长度
        sequenceLength = 12L;
        //序列号掩码
        sequenceMask = -1L ^ (-1L << sequenceLength);
    }
    public synchronized static long getID(){
        //获得当前时间
        long now = System.currentTimeMillis();
        //当前系统时间小于上一次记录时间
        if (now < lastTimeStamp){
            throw new RuntimeException("时钟回拨异常");
        }
        //相同时间 要序列号进制增量
        if (now == lastTimeStamp){
            //防止溢出
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0L){
                //溢出处理
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //获取下一毫秒时间 （有锁）
                now = System.currentTimeMillis();
            }
        }else{
        	//置0 之前序列号同一时间并发后自增 到这里说明时间不同了 版本号所以置0
        	sequence = 0L;
        }
        //记录当前时间
        lastTimeStamp = now;
        //当前时间和起始时间插值 右移 22位
        long handleTimeStamp = (now - startTimeStamp) << timeStampIndex;
        // 数据中心数值 右移动 17位 并且 按位或
        long handleWorkID = (dataCenterID << dataCenterIndex) | handleTimeStamp;
        // 机器ID数值 右移动 12位 并且 按位或
        long handleDataCenterID = (workID << workIDIndex) | handleWorkID;
        // 序列号 按位或
        long ID = handleDataCenterID | sequence;
        return ID;
    }

}
