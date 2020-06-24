package com.djangson.common.executor;


import com.djangson.common.util.LoggerUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 普通任务线程池
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/4/24 22:35
 */
public class CommonExecutor {

    // 任务延迟执行线程池
    private ThreadPoolExecutor executor;

    public CommonExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
        this.executor.allowCoreThreadTimeOut(true);
    }

    /**
     * 添加任务到任务池
     * @param executorTask
     */
    public void addTask(ExecutorTask executorTask) {
        executor.execute(executorTask);
    }

    /**
     * 移除任务池任务
     * @param executorTask
     */
    public boolean removeTask(ExecutorTask executorTask) {
        return executor.remove(executorTask);
    }

    /**
     * 查询剩余任务数量
     */
    public int getTaskCount() {
        return executor.getQueue().size();
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        this.executor.shutdown();
    }

    /**
     * 任务基类
     */
    public abstract static class ExecutorTask implements Runnable {

        protected abstract void execute();

        protected void afterExecute() {
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (Exception e) {
                LoggerUtil.error("线程池任务执行失败！", e);
            } finally {
                this.afterExecute();
            }
        }
    }
}
