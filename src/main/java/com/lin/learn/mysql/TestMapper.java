package com.lin.learn.mysql;

import org.apache.ibatis.annotations.*;

public interface TestMapper {

    @Select("select * from `test` where `id` = #{id} for update")
    @Results({
            @Result(column = "id", property = "id", javaType = Integer.class),
            @Result(column = "count", property = "count", javaType = Integer.class),
            @Result(column = "version", property = "version", javaType = Integer.class)
    })
    public TestEntity getById(@Param("id") int id);


    @Update("update `test` set `count` = #{count}, `version` = #{version} + 1 where `id` = #{id} and `version` = #{version}")
    public int update(TestEntity testEntity);

    @Update("update `test` set `count` = #{count} where `id` = #{id}")
    public void updateNoSafe(TestEntity testEntity);
}
