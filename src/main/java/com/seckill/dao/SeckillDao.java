package com.seckill.dao;

import com.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /**
     * 减库存
     *
     * @param seckillId
     * @param killTime
     * @return 影响行数 > 1 表示更新的记录行数
     */
    int reduceNumber(long seckillId, Date killTime);


    /**
     * 根据Id查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询商品列表
     * @param offet
     * @param limit
     * @return
     */
    List<Seckill> queryAll(int offet, int limit);
}
