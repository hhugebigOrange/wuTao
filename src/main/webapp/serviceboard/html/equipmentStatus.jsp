<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备概况</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>设备概况</span><span class="particulars">服务看板>>设备概况</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/equipmentSituation" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">客户名称：</label>
				<div class="layui-input-block">
					<input type="text" name="custName" 
						placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">服务区域：</label>
				<div class="layui-input-block">
					<input type="text" name="serviceArea"  placeholder="" autocomplete="off"
						class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">设备型号：</label>
				<div class="layui-input-block">
					<input type="text" name="unitType" placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查询</button>
				<button class="layui-btn layui-bg-gray">清除</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>
		<div class="labnum">
			<label>数量：<u>&nbsp&nbsp&nbsp&nbsp<span id="wasdue">${totalDevices}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">机器编码</th>
					<th lay-data="{field:'username3'}">设备名称</th>
					<th lay-data="{field:'username4'}">厂家</th>
					<th lay-data="{field:'username5'}">设备型号</th>
					<th lay-data="{field:'username6'}">设备序列号</th>
					<th lay-data="{field:'username7'}">客户名称</th>
					<th lay-data="{field:'username8'}">设备位置</th>
					<th lay-data="{field:'username9'}">安装日期</th>
					<th lay-data="{field:'username10'}">服务年限</th>
					<th lay-data="{field:'username11'}">设备归属</th>
					<th lay-data="{field:'username12'}">服务区域</th>
					<th lay-data="{field:'username13'}">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${devices}" var="device" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${device.machCode}</td>
						<td>${device.devName}</td>
						<td>${device.deviceBrand}</td>
						<td>${device.unitType}</td>
						<td>${device.esNumber}</td>
						<td>${device.custArea}</td>
						<td>${device.department}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${device.installedTime}" /></td>
						<td></td>
						<td>${device.assetAttr}</td>
						<td>${device.serviceArea}</td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script>
		//Demo
		layui.use('form', function() {
			var form = layui.form;

			//监听提交
			form.on('submit(formDemo)', function(data) {
				layer.msg(JSON.stringify(data.field));
				return false;
			});
		});

		layui.use('table', function() {
			var table = layui.table;//加载表格模块
			table.init('demo', {
				height : 500 //设置高度
				,
				page : true
			//开启分页
			});
		});
		
		$('#export').on('click', function() {
			location.href = "/som/exportSituationEquipment";
		});
	</script>
</body>
</html>