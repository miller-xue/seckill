package com.seckill.entity;

import java.util.Date;

/**
 * 秒杀成功明细
 */
public class SuccessKilled {
    /**
     * 秒杀商品ID
     */
    private long seckillId;

    /**
     * 秒杀用户手机
     */
    private long userPhone;

    /**
     * 秒杀状态
     */
    private short state;

    /**
     * 秒杀时间
     */
    private Date createTime;

    /**
     * 变通 多对一
     */
    private Seckill seckill;


    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
