package com.seckill.entity;

import java.util.Date;

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
