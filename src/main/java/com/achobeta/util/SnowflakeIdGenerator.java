package com.achobeta.util;

import cn.hutool.core.util.RandomUtil;

import java.util.Random;

public class SnowflakeIdGenerator {

    // 起始时间戳（2020-01-01）
    private static final long START_TIMESTAMP = 1577808000000L;
    // 机器标识位数
    private static final long WORKER_ID_BITS = 5L;
    // 数据中心标识位数
    private static final long DATACENTER_ID_BITS = 5L;
    // 序列号位数
    private static final long SEQUENCE_BITS = 12L;

    // 最大机器标识
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    // 最大数据中心标识
    private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);
    // 机器标识位移
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心标识位移
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳位移
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private static final int MIN_LENGTH = 1;

    private static final Random RANDOM = new Random();

    private long workerId;
    private long datacenterId;

    private int length;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long workerId, long datacenterId, int length) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("Datacenter ID can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.length = Math.max(MIN_LENGTH, length);
    }

    public long getSnowflakeId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & (-1L ^ (-1L << SEQUENCE_BITS));
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT) |
                (datacenterId << DATACENTER_ID_SHIFT) |
                (workerId << WORKER_ID_SHIFT) |
                sequence;
    }

    private int getRandom(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length);
        return RANDOM.nextInt(min, max);
    }

    public long nextId() {
        // 获取雪花id
        long snowflakeId = getSnowflakeId();
        String snowflakeIdString = String.valueOf(snowflakeId);
        int snowFlakeIdLength = snowflakeIdString.length();
        // 预防位数不够
        StringBuilder snowFlakeIdBuilder = new StringBuilder();
        int randomLength = length - snowFlakeIdLength;
        if(randomLength >= 1) {
            snowFlakeIdBuilder.append(getRandom(randomLength));
        }
        // 截取雪花id
        int subLength = Math.min(snowFlakeIdLength, length);
        snowFlakeIdBuilder.append(snowflakeIdString.substring(snowFlakeIdLength - subLength));
        // 返回雪花id
        return Long.parseLong(snowFlakeIdBuilder.toString());
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

}