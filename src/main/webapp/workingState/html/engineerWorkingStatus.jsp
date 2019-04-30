<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工程师工作状态</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>工程师工作状态</span><span class="particulars">在岗管理>>工程师工作状态</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/engineerWorkingStatus"
			method="get">
			<div class="layui-inline">
				<label class="layui-form-label">职位</label>
				<div class="layui-input-block">
					<select name="post">
						<option value="">请选择</option>
						<option value="运维经理">运维经理</option>
						<option value="客户主管">客户主管</option>
						<option value="技术主管">技术主管</option>
						<option value="工程师">工程师</option>
						<option value="驻现场人员">驻现场人员</option>
						<option value="客服助理">客服助理</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">姓名：</label>
				<div class="layui-input-block">
					<input type="text" name="name" 
						placeholder="" autocomplete="off" class="layui-input">
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
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>
		<div class="labnum">
			<label>工程师：<u>&nbsp&nbsp&nbsp&nbsp<span id="Engineer">${persons[2]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> 
			<label>技术主管：<u>&nbsp&nbsp&nbsp&nbsp<span
					id="ChiefTechnologyOfficer">${persons[0]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> 
			<label>运维经理：<u>&nbsp&nbsp&nbsp&nbsp<span
					id="operationsManager">${persons[1]}</span>&nbsp&nbsp&nbsp&nbsp
			</u>
			</label>
		</div>
	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">姓名</th>
					<th lay-data="{field:'username3'}">职位</th>
					<th lay-data="{field:'username4'}">工作状态</th>
					<th lay-data="{field:'username5'}">电话</th>
					<th lay-data="{field:'username6'}">备注</th>
					<th lay-data="{field:'username7'}">功能项</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${engineers}" var="engineer" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${engineer.name}</td>
						<td>${engineer.post}</td>
						<td>${engineer.workCond}</td>
						<td>${engineer.phone}</td>
						<td>${engineer.remark}</td>
						<td><a href="${ctx}/updateEngineer?id=${engineer.staffId}">修改:-</a><a
							href="#">-:删除</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script>
		$('#export').on('click', function() {
			location.href = "/som/exportEngineers";
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
		
		
	</script>
</body>
</html>