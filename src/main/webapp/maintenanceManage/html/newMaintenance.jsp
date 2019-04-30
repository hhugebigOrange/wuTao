<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
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
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>新增客户信息</legend>
		<form class="layui-form" action="/som/addMaintenance" method="get">
			<div class="layui-form-item">
				<label class="layui-form-label">合同编码</label>
				<div class="layui-input-block">
					<input name="contractCode" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">客户名称</label>
				<div class="layui-input-block">
					<input name="custName" class="layui-input" type="text"
						placeholder="请输入名称" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">联系人</label>
				<div class="layui-input-block">
					<input name="repairMan" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">联系电话</label>
				<div class="layui-input-inline">
					<input name="repairService" class="layui-input" type="text"
						autocomplete="off" lay-verify="requiredphone">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">责任工程师</label>
				<div class="layui-input-inline">
					<input name="staffId" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">后备工程师</label>
				<div class="layui-input-inline">
					<input name="reservEngineer" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">机器编码</label>
				<div class="layui-input-inline">
					<input name="machCode" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">保养频率</label>
				<div class="layui-input-inline">
					<select name="mainFrequency" lay-verify="required">
						<option value="">请选择</option>
						<option value="日">日</option>
						<option value="周">周</option>
						<option value="月">月</option>
						<option value="季度">季度</option>
						<option value="半年">半年</option>
						<option value="年">年</option>
					</select>
				</div>
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
		layui
				.use(
						[ 'form', 'layedit', 'laydate' ],
						function() {
							var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;

							//常规用法
							laydate.render({
								elem : '#test1',
							});
							laydate.render({
								elem : '#test2'
							});

							//自定义验证规则
							form
									.verify({
										requiredphone : [
												/^1(?:3\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\d|9\d)\d{8}$/,
												'请填写正确的电话号码' ]
									});
						});
	</script>
</body>
</html>