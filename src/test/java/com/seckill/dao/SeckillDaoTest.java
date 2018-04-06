package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;


/**
 * 配置Spring和Junit整合,Junit启动时加载springIOC容器
 */

@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class SeckillDaoTest {

    //注入Dao实体类依赖

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() {
        long id = 1001;
        Seckill s =  seckillDao.queryById(id);
        System.out.println(s.toString());
    }

    //java没有保存形参的记录:queryAll(offset,limit) --> queryAll(arg0,arg1)
    @Test
    public void queryAll() {
       List<Seckill> list =  seckillDao.queryAll(0,10);
        for (Seckill s: list) {
            System.out.println(s.toString());
        }
    }

    @Test
    public void reduceNumber() {
        int updateCount = seckillDao.reduceNumber(1001,new Date());
        System.out.println(updateCount);
    }
}