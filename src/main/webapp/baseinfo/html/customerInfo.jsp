<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户管理</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">

</head>
<body>
	<div class="title">
		<span>客户信息</span><span class="particulars">基本信息>>客户信息</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/customerInfo" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">客户名称：</label>
				<div class="layui-input-block">
					<input type="text" name="customerName" required
						lay-verify="required" placeholder="" autocomplete="off"
						class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<div class="layui-input-block">
					<button class="layui-btn layui-bg-gray" lay-filter="demo1"
						lay-submit="">查询</button>
				</div>
			</div>
			<div class="btn">
				<button type="reset" class="layui-btn layui-bg-gray">清除</button>
				<button class="layui-btn layui-bg-gray" id="selectAll">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>
		<div style="position: relative; left: 80%">
			<label>客户总数：<u>&nbsp&nbsp&nbsp&nbsp<span id="wasdue">${total}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
		<div>
			<table lay-filter="demo">
				<thead>
					<tr>
						<th lay-data="{field:'username1'}">NO</th>
						<th lay-data="{field:'username2'}">客户名称</th>
						<th lay-data="{field:'username3'}">客户地址</th>
						<th lay-data="{field:'username4'}">联系人</th>
						<th lay-data="{field:'username5'}">联系电话</th>
						<th lay-data="{field:'username6'}">备注</th>
						<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">功能项</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${custInfos}" var="custInfo" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${custInfo.custName}</td>
							<td>${custInfo.custAddr}</td>
							<td>${custInfo.linkman}</td>
							<td>${custInfo.phone}</td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">修改</a>
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="change">删除</a>
    </script>
	<script type="text/javascript">
		layui.use('table', function() {
			var table = layui.table;//加载表格模块
			table.init('demo', {
				height : 500 //设置高度
				,
				page : true
			//开启分页
			});
		});

		$('#selectAll').on('click', function() {
			location.href = "/som/allCustomerInfo";
		});

		$('#export').on('click', function() {
			location.href = "/som/exportCustomerInfo";
		});
	</script>
</body>
</html>