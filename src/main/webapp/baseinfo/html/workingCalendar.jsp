<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作日历</title>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet"
	href="/som/xunwei/css/modules/laydate/default/laydate.css">
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>工作日历</span><span class="particulars">基本信息>>工作日历</span>
	</div>
	<form class="layui-form" action="/som/workingCalendar" method="get"
		style="float: left; margin-right: 20px;">
		<div class="layui-inline">
			<label class="layui-form-label">服务区域</label>
			<div class="layui-input-block">
				<input name="serviceArea" class="layui-input" type="text"
					placeholder="" autocomplete="off" lay-verify="title">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">工程师</label>
			<div class="layui-input-block">
				<input name="engineer" class="layui-input" type="text"
					placeholder="" autocomplete="off" lay-verify="title">
			</div>
		</div>
		<div class="layui-inline">
			<button class="layui-btn" lay-filter="demo1" lay-submit=""
				style="margin-left: 20px;">查询</button>
		</div>
	</form>
	<button class="layui-btn" id="add">新增行程</button>
	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">服务区域</th>
					<th lay-data="{field:'username3'}">员工编码</th>
					<th lay-data="{field:'username4'}">工程师</th>
					<th lay-data="{field:'username5'}">电话</th>
					<th lay-data="{field:'username6'}">工作状态</th>
					<th lay-data="{field:'username7'}">起始日</th>
					<th lay-data="{field:'username8'}">结束日</th>
					<th lay-data="{field:'username9'}">事由原因</th>
					<th lay-data="{field:'username10'}">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${staffInfos}" var="staff" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${staff.compName}</td>
						<td>${staff.staffId}</td>
						<td>${staff.name}</td>
						<td>${staff.phone}</td>
						<td>${staff.workCond}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${staff.startDate}" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${staff.endDate}" /></td>
						<td>${staff.reson}</td>
						<td>${staff.remark}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script>
		//年月选择器
		layui.use('laydate', function() {
			var laydate = layui.laydate;
			laydate.render({
				elem : '#test3',
				type : 'month'
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

		$('#add').on('click', function() {
			//页面层
			layer.open({
				type : 2,
				title : false,
				area : [ '420px', '530px' ], //宽高
				shadeClose : true, //点击遮罩关闭
				content : [ '/som/baseinfo/html/addWorkingCalendar.jsp', 'no' ]
			//这里content是一个URL，不让iframe出现滚动条
			});
		})
	</script>
</body>
</html>