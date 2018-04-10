//存放主要交互逻辑js代码
// javaScript 模块化


var seckill ={
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        }
    },
    handleSeckillKill:function (seckillId) {
        //获取秒杀地址‘
        //执行秒杀操作
        //设置秒杀成功
    },
    //验证手机号
    validatePhone: function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else {
            return false;
        }
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        //1.时间的判断
        var seckillBox = $('#seckill-box')
        if(nowTime > endTime){
            //秒杀结束
            seckillBox.html('秒杀结束');
        }else if(nowTime < startTime){
            //秒杀未开始
            var killTime = new Date(startTime+1000);
            seckillBox.countdown(killTime,function (event) {
                var formt = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(formt);
            }).on('finish.countdown',function () {
                //获取秒杀地址，控制显示逻辑.执行秒杀
                seckill.handleSeckillKill(seckillId);
            })
        }else {
            //秒杀进行中
            seckill.handleSeckillKill(seckillId);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init : function (params) {
            //手机验证和登陆 , 即时交互
            //规划我们的交互流程

            //在cookie查找手机号
            var killPhone = $.cookie('killPhone');
            if(!seckill.validatePhone(killPhone)){
                //没登陆
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({ //显示了弹出层
                    show: true, //显示
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if(seckill.validatePhone(inputPhone)){
                        //电话写入Cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    }else {
                        $('#killPhoneMessage').hide().html('<label class="label-danger">手机号错误!</label>').show(300);
                    }
                });

            }
            //已经登陆;
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(),{},function (result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countDown(seckillId,nowTime,startTime,endTime);
                }else{
                    console.log('result',result);
                }
            })
        }
    }
}