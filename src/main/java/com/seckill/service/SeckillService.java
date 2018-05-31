package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecuteion;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口:站在"使用者"角度设计接口
 * 三个方面:方法定义力度,参数,返回类型(return 类型/异常)
 */
public interface SeckillService {

    /**
     * 查询所有秒杀纪录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址,
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecuteion executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀操作 by 存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecuteion executeSeckillProcedure(long seckillId, long userPhone, String md5);
}


