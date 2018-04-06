package com.seckill.exception;

/**
 * 秒杀相关业务异常
 */
public class SeckillExpection extends RuntimeException {
    public SeckillExpection(String message) {
        super(message);
    }

    public SeckillExpection(String message, Throwable cause) {
        super(message, cause);
    }
}
