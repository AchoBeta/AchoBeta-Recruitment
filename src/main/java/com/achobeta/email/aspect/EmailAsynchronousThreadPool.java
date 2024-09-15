package com.achobeta.email.aspect;

import com.achobeta.util.ThreadPoolUtil;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-09-15
 * Time: 14:48
 */
public class EmailAsynchronousThreadPool {

    private final static String THREAD_NAME = "emailAsynchronousThread";

    private final static ThreadPoolExecutor EMAIL_ASYNCHRONOUS_THREAD_POOL;

    static {
        EMAIL_ASYNCHRONOUS_THREAD_POOL = ThreadPoolUtil.getIoTargetThreadPool(THREAD_NAME);
    }

    static void submit(Runnable... tasks) {
        Arrays.stream(tasks).forEach(EmailAsynchronousThreadPool::submit);
    }

    static void submit(Runnable runnable) {
        EMAIL_ASYNCHRONOUS_THREAD_POOL.submit(runnable);
    }

}
