<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>故障分析页</title>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>故障分析</span><span class="particulars">服务管理>>故障分析</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/failureAnalysis" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">客户名称</label>
				<div class="layui-input-inline">
					<input name="custName" class="layui-input" id="test6" type="text" placeholder="">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">服务区域</label>
				<div class="layui-input-inline">
					<input name="serviceArea" class="layui-input" id="test6" type="text" placeholder="">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机器编码</label>
				<div class="layui-input-inline">
					<input name="machCode" class="layui-input" id="test6" type="text" placeholder="">
				</div>
			</div>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查看</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>

	</div>
	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">序号</th>
					<th lay-data="{field:'username2'}">客户名称</th>
					<th lay-data="{field:'username3'}">服务区域</th>
					<th lay-data="{field:'username4'}">机器编码</th>
					<th lay-data="{field:'username6'}">故障类型</th>
					<th lay-data="{field:'username8'}">停机时间(h/小时)</th>
					<th lay-data="{field:'username9'}">创建时间</th>
					<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">设备故障履历</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${serviceInfos}" var="serviceInfo"
					varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${serviceInfo.orderInfo.custName}</td>
						<td>${serviceInfo.staffInfo.compName}</td>
						<td>${serviceInfo.orderInfo.machCode}</td>
						<td>${serviceInfo.orderInfo.faultType}</td>
						<td>${serviceInfo.downTime}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.orderInfo.sendTime}" /></td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">设备故障履历</a>
    </script>
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
		
		layui.use('laydate', function() {
			var laydate = layui.laydate;
			//日期范围
			layui.use('laydate', function() {
				var laydate = layui.laydate;

			})
		})
		layui.use('table', function() {
			var table = layui.table;//加载表格模块
			table.init('demo', {
				height : 500 //设置高度
				,
				page : true
			//开启分页
			});
			table.on('tool(demo)', function(obj) {
				var data = obj.data;
				if (obj.event === 'detail') {
					layer.open({
						type : 2,
						title : false,
						area : [ '900px', '600px' ],
						shadeClose : true,
						content : [ '/som/faultRecord?machCode='+ data.username4,
								'no' ]
					});
				}
			});
		});
		
		$('#export').on('click', function() {
			location.href = "/som/exportFaiure";
		});
	</script>
</body>
</html>