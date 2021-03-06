<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增合同</title>
    <script src="/som/xunwei/js/layui.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <style type="text/css">
        *{margin: 0px auto;padding: 0px;}
        fieldset{width:1000px;}
        form{padding: 10px;}
        .layui-inline{margin-bottom: 10px;width: 300px;text-align: center}
    </style>
    <link rel="stylesheet" href="/som/xunwei/css/modules/layer/default/layer.css">
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>新增合同信息</legend>

    <form class="layui-form" action="">
        <div class="layui-inline">
            <label class="layui-form-label">主服务区</label>
            <div class="layui-input-block">
                <input name="mainService" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">分服务区</label>
            <div class="layui-input-block">
                <input name="childService" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">客户名称</label>
            <div class="layui-input-block">
                <input name="custName" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">合同编码</label>
            <div class="layui-input-block">
                <input name="contractNo" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">登记时间</label>
            <div class="layui-input-inline">
                <input name="regTime" class="layui-input" type="text" placeholder="" lay-verify="date">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">经办部门</label>
            <div class="layui-input-block">
                <input name="handlingDepartment" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">经办人</label>
            <div class="layui-input-block">
                <input name="agent" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">合同保管人</label>
            <div class="layui-input-block">
                <input name="contractHoldman" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">合同开始日期</label>
            <div class="layui-input-inline">
                <input name="startDate" class="layui-input" type="text" placeholder="" lay-verify="date">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">合同结束日期</label>
            <div class="layui-input-inline">
                <input name="endDate" class="layui-input" type="text" placeholder="" lay-verify="date">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">合同期限</label>
            <div class="layui-input-inline">
                <input name="contractDeadline" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">合同种类</label>
            <div class="layui-input-inline">
                <input name="contractType" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">签约公司</label>
            <div class="layui-input-inline">
                <input name="signingCompany " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">客户联系人</label>
            <div class="layui-input-inline">
                <input name="custLinkman" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">联系电话</label>
            <div class="layui-input-inline">
                <input name="linkmanPhone" class="layui-input" type="text" autocomplete="off" lay-verify="phone">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">开户行</label>
            <div class="layui-input-inline">
                <input name="openingBank" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">账号</label>
            <div class="layui-input-inline">
                <input name="bankAccount" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">纳税人识别号</label>
            <div class="layui-input-inline">
                <input name="taxIden" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">地址</label>
            <div class="layui-input-inline">
                <input name="address" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">资产归属</label>
            <div class="layui-input-block">
                <select name="assetAscription" lay-verify="required">
                    <option value="">请选择</option>
                    <option value="0">讯维</option>
                    <option value="1">客户</option>
                    <option value="2">其他</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">资产编号</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">资产类别</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">规格型号</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">设备名称</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">设备数量</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline"><h2>服务水准协议KPI指标:</h2></div><br>
        <div class="layui-inline">
            <label class="layui-form-label">KPI—1</label>
            <div class="layui-input-inline">
                <input name="kpi1" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">KPI—2</label>
            <div class="layui-input-inline">
                <input name="kpi2" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">KPI—3</label>
            <div class="layui-input-inline">
                <input name="kpi3" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">KPI—</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
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
        //监听提交
        form.on('submit(demo1)', function(data){
            layer.alert(JSON.stringify(data.field), {title: '最终的提交信息' })
            return false;
        });
    });
</script>
</body>
</html>