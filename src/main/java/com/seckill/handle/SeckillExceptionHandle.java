package com.seckill.handle;

import com.seckill.dto.SeckillResult;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.SeckillException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by miller on 2018/5/31
 */

@ControllerAdvice
public class SeckillExceptionHandle {

    @ExceptionHandler(Exception.class)
    public SeckillResult handleException(Exception e){
        return new SeckillResult(false, SeckillStatEnum.INNER_ERROR.getStateInfo());
    }
}
