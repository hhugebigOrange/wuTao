<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加行程</title>
 <script src="/som/xunwei/js/layui.js"></script>
 <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <style type="text/css">
        *{margin: 0px auto;padding: 0px;}
        fieldset{width:400px;}
        form{padding: 10px;}
    </style>
    <link rel="stylesheet" href="/som/xunwei/css/modules/layer/default/layer.css">
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>新增客户信息</legend>

<form class="layui-form" action="/som/doAddWorkingCalendar" method="get">
    <div class="layui-form-item">
        <label class="layui-form-label">员工编码：</label>
        <div class="layui-input-block">
            <input name="StaffCode" class="layui-input" type="text" placeholder="请输入员工编码" autocomplete="off" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">在岗状态：</label>
        <div class="layui-input-block">
            <select name="jobState" lay-verify="required">
                <option value="">请选择</option>
                <option value="休假">休假</option>
                <option value="培训">培训</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">起始日期</label>
        <div class="layui-input-block">
            <input name="startDate" class="layui-input" type="date" placeholder="" autocomplete="off" lay-verify="date" id="date1">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">终止日期</label>
        <div class="layui-input-block">
            <input name="endDate" class="layui-input" type="date" placeholder="" autocomplete="off" lay-verify="date" id="date2">
        </div>
    </div>
    <div class="layui-form-item">
    <label class="layui-form-label">事由原因：</label>
    <div class="layui-input-block">
        <input name="reson" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
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
            ,laydate=layui.data;
        laydate.render({
            elem:"#date1"
        })
        laydate.render({
            elem:"#date2"
        })
        //表单初始赋值
        // form.val('example', {
        //     "username": "贤心" // "name": "value"
        // })

    });
</script>
</body>
</html>