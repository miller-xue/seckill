package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {

    /**
     * 插入购买明细,可过滤重复(联合主键)
     * @param seckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId ,@Param("userPhone") long userPhone);



    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
