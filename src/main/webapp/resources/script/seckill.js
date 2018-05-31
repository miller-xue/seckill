//存放主要交互逻辑js代码
// javaScript 模块化


var seckill ={
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/'+seckillId+'/exposer'
        },
        execution: function (seckillId, md5) {
            return '/seckill/ ' + seckillId + '/' + md5 + '/execution'
        }
    },
    /**
     * 开启秒杀
     * @param seckillId 秒杀商品id
     * @param node 秒杀按钮挂载点
     */
    handleSeckillKill: function (seckillId,node) {
        // 1.挂载btn
        node.hide().html('<button class="btn btn-primary btn-1g" id="killBtn">开始秒杀</button>');
        // 2.获取秒杀地址‘
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回掉函数中执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀,获取秒杀地址
                    var killUrl = seckill.URL.execution(seckillId,exposer['md5']);
                    console.log('killUrl',killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click',function () {
                        //绑定执行post请求
                        $(this).addClass('disabled');
                        //发生秒杀请求
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+' </span>')
                            }else {
                                console.log('result',result);
                            }
                        })
                    })
                    node.show();
                }else { //未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill,countDown(seckillId,now,start,end);
                }
            }else {
                console.log('result',result);
            }
        });
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
            }).on('finish.countdown',function () { //完成倒计时, 获取秒杀地址，控制显示逻辑.执行秒杀
                seckill.handleSeckillKill(seckillId,seckillBox);
            })
        }else {
            //秒杀进行中
            seckill.handleSeckillKill(seckillId,seckillBox);
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
            if(seckill.validatePhone(killPhone) == false){ //没登陆
                // 1.显示弹出层
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true, //显示
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false
                });
                // 2.给确定按钮绑定点击时间
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
                return;
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