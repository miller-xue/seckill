package com.seckill.dao;

import com.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {

    /**
     * 插入购买明细,可过滤重复(联合主键)
     * @param seckillId
     * @param phone
     * @return 插入的行数
     */
    int insertSuccessKilledDao(long seckillId , long phone);



    SuccessKilled queryByIdWithSeckill(long seckillId);
}
