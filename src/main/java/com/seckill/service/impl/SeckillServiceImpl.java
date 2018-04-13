package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecuteion;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillExpection;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);

    //注入service依赖

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SuccessKilledDao successKilledDao;


    //md5盐值字符串,用户混淆MD5
    private final String slat = "dwadbwakdbk2dbKJB@EdhwiuahduiwahduiwaKJdb2kjDBKJd2DBKJ";


    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,10);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //  优化点: 缓存优化,一致性在超时的基础上
        //1.访问Redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null){    //没有秒杀信息
                return new Exposer(false,seckillId);
            }else {
                //3:放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() //不在秒杀时间内w
                || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }

        String md5 = this.getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }


    private String getMD5(long seckillId){
        String base = seckillId + "/" +slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }


    /**
     * 使用注解控制事务方法的优点
     * 1:开发团队达成一致约定,明确标注事务方法的编程风格。
     * 2:保证事务方法的执行时间尽可能短,不要穿插其他的网络操作 RPC/HTTP请求 或者剥离到事务方法外部
     * 3:不是所有的方法都需要事务,如只有一条修改操作,只读操作不需要事务控制(2条以上才需要事务)
     */
    @Transactional
    public SeckillExecuteion executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillExpection, RepeatKillException, SeckillCloseException {
        if(md5 == null ||  !md5.equals(getMD5(seckillId)) ){
            throw new SeckillCloseException("seckill data rewrite");
        }
        //减库存
        try {
            Date nowDate = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId,nowDate);
            //执行秒杀逻辑
            if(updateCount <= 0){
                //没有更新到记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            }else{
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount <= 0){
                    throw new RepeatKillException("seckill repeatd");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecuteion(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }
        catch (SeckillCloseException e1 ){
            throw e1;
        }
        catch (RepeatKillException e2 ){
            throw e2;
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            //所有编译期异常,转化为运行时异常
            throw new SeckillExpection("seckill inner error"+e.getMessage());
        }

    }
}
