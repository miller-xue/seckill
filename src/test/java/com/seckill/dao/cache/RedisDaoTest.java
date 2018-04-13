package com.seckill.dao.cache;

import com.seckill.dao.SeckillDao;
import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by miller on 2018/4/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class RedisDaoTest {
    private long id = 1001;

    @Autowired
    private SeckillDao seckillDao;


    @Autowired
    private RedisDao redisDao;

    @Test
    public void testRedis()throws Exception{
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null) {
            seckill = seckillDao.queryById(id);
        }
        String result = redisDao.putSeckill(seckill);
        System.out.println(result);

        System.out.println(redisDao.getSeckill(id));

    }
}