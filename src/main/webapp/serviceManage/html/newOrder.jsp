<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加工单页面</title>
 <script src="/som/xunwei/js/layui.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <style type="text/css">
        *{margin: 0px auto;padding: 0px;}
        fieldset{width:400px;}
        form{padding: 10px;}
        .malfunction{display: none}
    </style>
    <link rel="stylesheet" href="/som/xunwei/css/modules/layer/default/layer.css">
    <script src="/som/xunwei/js/jquery.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/modules/laydate/default/laydate.css">
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>新增工单信息</legend>

    <form class="layui-form" action="/som/doNewOrder" method="get">
        <!--不用填-->
        <div class="layui-form-item">
            <label class="layui-form-label">工单号</label>
            <div class="layui-input-block">
                <input name="woNumber" value="${orderNumber}" readonly="readonly" class="layui-input" type="text" placeholder="${orderNumber}" autocomplete="off">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">设备序列号</label>
            <div class="layui-input-block">
                <input name="esNumber" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">客户名称</label>
            <div class="layui-input-block">
                <input name="custName" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">设备名称</label>
            <div class="layui-input-block">
                <input name="devName" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">机器编码</label>
            <div class="layui-input-block">
                <input name="machCode" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">客户地址</label>
            <div class="layui-input-block">
                <input name="custAddr" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
            </div>
        </div>
         <div class="layui-form-item">
            <label class="layui-form-label">报修人</label>
            <div class="layui-input-block">
                <input name="repairMan" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
            </div>
        </div>
         <div class="layui-form-item">
            <label class="layui-form-label">报修电话</label>
            <div class="layui-input-block">
                <input name="repairService" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">报修类型：</label>
            <div class="layui-input-block">
                <select name="repairType" lay-verify="required">
                    <option value="">请选择</option>
                    <option value="微信报修">微信报修</option>
                    <option value="电话报修">电话报修</option>
                    <option value="PC报修">PC报修</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">服务类型：</label>
            <div class="layui-input-block" id="serve">
                <select name="serve" lay-verify="required" lay-filter="serve">
                    <option value="">请选择</option>
                    <option value="事故类">事故类</option>
                    <option value="需求类">需求类</option>
                    <option value="事件类">事件类</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item malfunction">
            <label class="layui-form-label">故障类型：</label>
            <div class="layui-input-block">
                <select name="accidentType" lay-verify="required">
                    <option value="">请选择</option>
                    <option value="软件故障">软件故障</option>
                    <option value="硬件故障">硬件故障</option>
                    <option value="耗材断供">耗材断供</option>
                </select>
            </div>
        </div>
        <!--不用填-->
        <div class="layui-form-item">
            <label class="layui-form-label">工单状态</label>
            <div class="layui-input-block">
                <input name="woStatus" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <!--不用填-->
        <div class="layui-form-item">
        <label class="layui-form-label">处理措施</label>
        <div class="layui-input-block">
            <input name="treatmentMeasure" class="layui-input" type="text" placeholder="请输入名称" autocomplete="off" lay-verify="required">
        </div>
    </div>
        <div class="layui-form-item">
            <label class="layui-form-label">报修时间</label>
            <div class="layui-input-inline">
                <input name="repairTime" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="date" id="date">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-filter="demo1" lay-submit="">立即提交</button>
                <button class="layui-btn layui-btn-primary" type="reset">重置</button>
            </div>
        </div>
    </form>
</fieldset>
<script>
layui.use(['form', 'layedit', 'laydate'], function(){
    var form = layui.form
        ,layer = layui.layer
        ,layedit = layui.layedit
        ,laydate = layui.laydate;

    laydate.render({
        elem:"#date"
    })
    
    form.on('select(serve)',function (date) {
        var serve=$("#serve input").val();
        if (serve=="事故类"||serve=="需求类"){
            $(".malfunction").show();
        }else {
            $(".malfunction").hide();
        }
    })
});
</script>
</body>
</html>