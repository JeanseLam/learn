package com.lin.learn.slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlaveWorker extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(SlaveWorker.class);

    private boolean stop = true;

    public SlaveWorker () {
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
                logger.info("slave worker is running...");
                Thread.sleep(1000L);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                break;
            }
        }
    }
}
