package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by miller on 2018/4/13
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    private final static String BASE_SECKILL_KEY = "seckill:";

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {
        //Redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = BASE_SECKILL_KEY + seckillId;
                //并没有实现内部序列化操作
                //get -->byte[] 反序列化 ->Object()
                //采用自定义的序列化的方式
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    Seckill seckill = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        // set Object(Seckill) -> 序列化 --> bytes[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = BASE_SECKILL_KEY+seckill.getSeckillId();
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;
               String result =  jedis.setex(key.getBytes(),timeout,bytes);
               return result;
            }finally {
                jedis.close();
            }

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
