package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int insertCount = successKilledDao.insertSuccessKilled(1001,1302299323);
        System.out.println(insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled  successKilled = successKilledDao.queryByIdWithSeckill(1001,1302299323);

        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}