<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增设备页面</title>
<style type="text/css">
* {
	margin: 0px auto;
	padding: 0px;
}

fieldset {
	width: 700px;
}

form {
	padding: 10px;
	width: 1100px;
	margin: 50px;
}

.layui-inline {
	margin-bottom: 15px;
}

.sfont {
	font-size: small
}
</style>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
</head>
<body>
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>新增设备</legend>
		<form class="layui-form" action="/som/doAddEquipment" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">设备名称</label>
				<div class="layui-input-block">
					<input name="devName" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机器编码</label>
				<div class="layui-input-inline">
					<input name="machCode" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">设备品牌</label>
				<div class="layui-input-block">
					<input name="deviceBrand" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">设备型号</label>
				<div class="layui-input-block">
					<input name="unitType" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">设备幅面</label>
				<div class="layui-input-block">
					<input name="deviceBound" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">设备类型</label>
				<div class="layui-input-block">
					<input name="deviceType" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">输出规格</label>
				<div class="layui-input-block">
					<input name="outputSpec" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">设备序列号</label>
				<div class="layui-input-block">
					<input name="esNumber" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">客户区域</label>
				<div class="layui-input-block">
					<input name="custArea" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">服务区域</label>
				<div class="layui-input-block">
					<input name="serviceArea" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">部门</label>
				<div class="layui-input-block">
					<input name="department" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">服务级别</label>
				<div class="layui-input-block">
					<input name="serviceLevel" class="layui-input" type="text"
						placeholder="" autocomplete="off" lay-verify="">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">客户联络人</label>
				<div class="layui-input-block">
					<input name="custLinkman" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label sfont">客户联络电话</label>
				<div class="layui-input-block">
					<input name="linkmanPhone" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">责任工程师</label>
				<div class="layui-input-block">
					<input name="responsibleEngineer" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">后备工程师</label>
				<div class="layui-input-block">
					<input name="reserveEnginner" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label sfont">初始黑白读数</label>
				<div class="layui-input-block">
					<input name="bwReader" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label sfont">初始彩色读数</label>
				<div class="layui-input-block">
					<input name="colorReader" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">装机时间</label>
				<div class="layui-input-block">
					<input name="installedTime" class="layui-input" type="date"
						placeholder="必填" autocomplete="off" lay-verify="date">
				</div>
			</div>
			<br>
			<div class="layui-inline"
				style="margin-left: 90px; margin-right: 75px;">
				<h2>设备资产属性:</h2>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">资产属性</label>
				<div class="layui-input-block">
					<select name="assetAttr" lay-verify="required">
						<option value="">请选择</option>
						<option value="讯维">讯维</option>
						<option value="客户">客户</option>
						<option value="其他">其他</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">资产编码</label>
				<div class="layui-input-block">
					<input name="assetNumber" class="layui-input" type="text"
						placeholder="必填" autocomplete="off" lay-verify="required">
				</div>
			</div>
			<br>
			<div class="layui-inline">
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