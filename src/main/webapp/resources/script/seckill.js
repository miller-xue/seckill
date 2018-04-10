//存放主要交互逻辑js代码
// javaScript 模块化


var seckill ={
    //封装秒杀相关ajax的url
    url: {

    },
    //验证手机号
    validatePhone: function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else {
            return false;
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
            var startTIme = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            if(!seckill.validatePhone(killPhone)){
                //没登陆
                var killPhoneModal = $('#killPhoneModal');
                //显示了弹出层
                killPhoneModal.modal({
                    show: true, //显示弹出层
                    backdrop: false, //禁止位置关闭
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
        }
    }
}