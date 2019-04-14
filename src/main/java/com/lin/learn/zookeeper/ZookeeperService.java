package com.lin.learn.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class ZookeeperService {

    private ZooKeeper zooKeeper;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public ZooKeeper connect(String host) throws IOException, InterruptedException {

        zooKeeper = new ZooKeeper(host, 30000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        return zooKeeper;
    }

    public static void main(String[] args) {

        ZookeeperService zookeeperService = new ZookeeperService();
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = zookeeperService.connect("127.0.0.1:2181");

            zooKeeper.create(
                    "/java_test",
                    "java_data".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);

            while (true) {
                byte[] b = null;
                b = zooKeeper.getData("/java_test", new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if(watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                            System.out.println("path /java_test data has been changed...");
                        }
                    }
                }, null);
                String data = new String(b, StandardCharsets.UTF_8);
                System.out.println(data);

                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            int version = zooKeeper.exists("/java_test", true).getVersion();
//            zooKeeper.setData("/java_test", "new_java_data".getBytes(), version);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(zooKeeper != null) {
                    zooKeeper.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
