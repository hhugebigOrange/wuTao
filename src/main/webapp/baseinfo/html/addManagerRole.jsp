<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
<style type="text/css">
* {
	margin: 0px auto;
	padding: 0px;
}

fieldset {
	width: 400px;
}

form {
	padding: 10px;
}
</style>
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>新增用户信息</legend>
		<form class="layui-form" action="/som/doAddManager" method="get">
			<div class="layui-form-item">
				<label class="layui-form-label">姓名</label>
				<div class="layui-input-block">
					<input name="staffName" class="layui-input" type="text"
						placeholder="请输入名称" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">所在分公司</label>
				<div class="layui-input-block">
					<input name="comperName" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">角色</label>
				<div class="layui-input-block">
					<select name="role" lay-verify="required">
						<option value="">请选择</option>
						<option value="1">运维经理</option>
						<option value="2">客服主管</option>
						<option value="3">客服调度员</option>
						<option value="4">技术主管</option>
						<option value="5">工程师</option>
						<option value="6">驻现场人员</option>
						<option value="7">客户助理</option>
					</select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">注册手机号</label>
				<div class="layui-input-inline">
					<input name="phoneNumber" class="layui-input" type="text"
						autocomplete="off" lay-verify="requiredphone">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">创建时间</label>
				<div class="layui-input-inline">
					<input name="createData" class="layui-input" type="date"
						placeholder="" autocomplete="off" lay-verify="date" id="date">
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
        //自定义验证规则
        laydate.render({
           elem: '#date' //指定元素
        });
        
        form.verify({
            requiredphone: [/^1(?:3\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\d|9\d)\d{8}$/, '请填写正确的电话号码']
        });
        //监听提交
    });
	</script>
</body>
</html>