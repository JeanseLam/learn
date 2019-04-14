package com.lin.learn;

import com.lin.learn.mysql.TestService;
import com.lin.learn.rocketmq.TransactionConsumer;
import com.lin.learn.rocketmq.TransactionProducer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.lin.learn.mysql")
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
//        AppContext appContext = applicationContext.getBean(AppContext.class);
//        appContext.startup();

//        TestService service = applicationContext.getBean(TestService.class);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i = 0; i < 10; i++) {
//                    service.increaseNoSafe(3);
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i = 0; i < 10; i++) {
//                    service.increaseNoSafe(3);
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i = 0; i < 10; i++) {
//                    service.increaseNoSafe(3);
//                }
//            }
//        }).start();

        try {
            TransactionProducer producer = applicationContext.getBean(TransactionProducer.class);
            producer.sendValueToOtherAccount(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
