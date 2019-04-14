package com.lin.learn.mysql;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class TestService {

    @Resource
    private TestMapper mapper;

    public void increaseCount(int id) {

        while (true) {
            try {
                TestEntity testEntity = mapper.getById(id);
                testEntity.setCount(testEntity.getCount() + 1);
                int updateCount = mapper.update(testEntity);
                if(updateCount > 0) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Transactional
    public void increaseNoSafe(int id) {

        try {
            TestEntity testEntity = mapper.getById(id);
            testEntity.setCount(testEntity.getCount() + 1);
            mapper.updateNoSafe(testEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
