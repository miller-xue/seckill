package com.seckill.service.impl;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecuteion;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillExpection;
import com.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillServiceImpl;

    @Test
    public void getSeckillList() {
        List<Seckill> seckills  = seckillServiceImpl.getSeckillList();
        logger.info("list={}",seckills);
    }

    @Test
    public void getById() {
        System.out.println(seckillServiceImpl.getById(1001));
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer = seckillServiceImpl.exportSeckillUrl(1000);
        System.out.println(exposer);
    }

    @Test
    public void executeSeckill() {
        Exposer exposer = seckillServiceImpl.exportSeckillUrl(1001);

        try {
            SeckillExecuteion seckillExecuteion =  seckillServiceImpl.executeSeckill(1001,13022996276l,exposer.getMd5());
            System.out.println(seckillExecuteion);
        }
        catch (SeckillCloseException e1){
            System.out.println(e1.getMessage());
        }
        catch (RepeatKillException e2){
            System.out.println(e2.getMessage());
        }
        finally {
        }
    }

    @Test
    public void executeSeckillProcedure(){
        long seckillId = 1001;
        long phone = 13122989865L;
        Exposer exposer = seckillServiceImpl.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecuteion executeion = seckillServiceImpl.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(executeion.getStateInfo());
        }
    }
}