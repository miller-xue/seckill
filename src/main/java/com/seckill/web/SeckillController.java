package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecuteion;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") //url:模块
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;


    @RequestMapping(value="/list",method = RequestMethod.GET)
    public String list(Model model){
        //list.jsp + model = ModelAndView
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value="/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(Model model,@PathVariable("seckillId") Long seckillId){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer",
                    method = RequestMethod.POST,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable  Long seckillId){
        SeckillResult<Exposer> result;
        try {
            //对秒杀接口的数据校验已经在Service层做了
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }


        @RequestMapping(value = "/{seckillId}/{md5}/execution",
                    method = RequestMethod.POST,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecuteion> execute(@PathVariable("seckillId") Long seckillId,
                                                    @PathVariable("md5") String md5,
                                                    @CookieValue(value = "killPhone",required = false) Long phone){
        if(phone == null){
            return new SeckillResult<SeckillExecuteion>(false,"未注册");
        }
        SeckillExecuteion executeion ;
        try {
            executeion = seckillService.executeSeckill(seckillId,phone,md5);
//            return new SeckillResult<SeckillExecuteion>(true,executeion);
        }
        catch (SeckillCloseException e){
            logger.error(e.getMessage(),e);
            executeion = new SeckillExecuteion(seckillId, SeckillStatEnum.END);
        }
        catch (RepeatKillException e){
            logger.error(e.getMessage(),e);
            executeion = new SeckillExecuteion(seckillId, SeckillStatEnum.REPEAT_KILL);
        }
        catch (Exception e) {
            logger.error(e.getMessage(),e);
            executeion = new SeckillExecuteion(seckillId, SeckillStatEnum.INNER_ERROR);
        }
        return new SeckillResult<SeckillExecuteion>(true,executeion);
    }

    /**
     * 获得系统当前时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        return new SeckillResult<Long>(true,new Date().getTime());
    }
}
