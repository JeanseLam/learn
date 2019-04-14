package com.lin.learn.slave;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
public class AppContext {

    private static final Logger logger = LoggerFactory.getLogger(AppContext.class);

    private ZooKeeper zooKeeper;

    private static final String MY_HOST = "10.192.1.14";

    private boolean isMaster = false;

    private final MasterWorker masterWorker = new MasterWorker();
    private final SlaveWorker slaveWorker = new SlaveWorker();

    public void startup() {

        // 连接zookeeper服务器
        try {
            CountDownLatch zookeeperConnectCountDownLatch = new CountDownLatch(1);
            this.zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        zookeeperConnectCountDownLatch.countDown();
                    }
                }
            });

            zookeeperConnectCountDownLatch.await();
            logger.info("connect zookeeper server success.");
        } catch (IOException|InterruptedException e) {
            logger.error(e.getMessage(), e);
            return;
        }

        // 初始化master和slave节点的工作线程
        masterWorker.start();
        slaveWorker.start();

        // 竞争成为master节点或监听master节点
        applyToBeMasterNodeOrWatchMaster();

    }


    private void applyToBeMasterNodeOrWatchMaster() {
        try {
            // 成功成为master节点
            this.zooKeeper.create("/master", MY_HOST.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("create master node success, this instance will be a master, master node:{}", MY_HOST);
            isMaster = true;

            slaveWorker.stopWorker();
            masterWorker.reRunWorker();
            synchronized (masterWorker) {
                masterWorker.notify();
            }

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);

        } catch (KeeperException ke) {
            logger.error(ke.getMessage(), ke);
            logger.info("create master node fail, this instance will be a slave, slave node:{}", MY_HOST);
        }

        // 竞争master节点失败，监听master节点，如果master节点挂掉，暂停salve节点工作线程重新竞争master节点
        if(!isMaster) {
            try {
                masterWorker.stopWorker();
                slaveWorker.reRunWorker();
                synchronized (slaveWorker) {
                    slaveWorker.notify();
                }

                byte[] nodeData = this.zooKeeper.getData("/master", new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if(watchedEvent.getType() == Event.EventType.NodeDeleted) {
                            slaveWorker.stopWorker();
                            masterWorker.stopWorker();
                            applyToBeMasterNodeOrWatchMaster();
                        }
                    }
                }, null);

                logger.info("follow master node:{}", new String(nodeData, "UTF-8"));

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
