package com.lin.learn.slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterWorker extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(MasterWorker.class);

    private boolean stop = true;

    public MasterWorker () {
        this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("{} throws exception, {}", t.getName(), e);
            }
        });
    }

    public void stopWorker() {
        this.stop = true;
    }

    public void reRunWorker() {
        this.stop = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    if(stop) {
                        wait();
                        stop = false;
                        continue;
                    }
                }
                logger.info("master task is running...");
                Thread.sleep(1000L);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                break;
            }
        }
    }
}
