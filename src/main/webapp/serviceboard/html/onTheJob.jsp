<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在岗人数</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body style="width: 2500px;">
	<div class="title">
		<span>在岗人数</span><span class="particulars">服务看板>>在岗人数</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/onTheJob" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">分公司名称:</label>
				<div class="layui-input-block">
					<select name="serviceArea" lay-verify="required">
						<option value="">请选择</option>
						<option value="宝安分公司">宝安分公司</option>
						<option value="上海分公司">上海分公司</option>
						<option value="东莞分公司">东莞分公司</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">岗位：</label>
				<div class="layui-input-block">
					<select name="role" lay-verify="required">
						<option value="">请选择</option>
						<option value="运维经理">运维经理</option>
						<option value="客服主管">客服主管</option>
						<option value="技术主管">技术主管</option>
						<option value="工程师">工程师</option>
						<option value="驻现场人员">驻现场人员</option>
						<option value="客服助理">客服助理</option>
					</select>
				</div>
			</div>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查询</button>
				<button type="reset" class="layui-btn layui-bg-gray">清除</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>

		<div class="labnum">
			<label>在岗总人数：<u>&nbsp&nbsp&nbsp&nbsp<span id="OnGuard">${totalPersons}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>客服人数：<u>&nbsp&nbsp&nbsp&nbsp<span id="Service">${persons[6]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>工程师：<u>&nbsp&nbsp&nbsp&nbsp<span id="Engineer">${persons[4]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>客服主管：<u>&nbsp&nbsp&nbsp&nbsp<span
					id="CustomerServiceSupervisor">${persons[1]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>技术主管：<u>&nbsp&nbsp&nbsp&nbsp<span
					id="ChiefTechnologyOfficer">${persons[3]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>运维经理：<u>&nbsp&nbsp&nbsp&nbsp<span
					id="operationsManager">${persons[0]}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">姓名</th>
					<th lay-data="{field:'username3'}">联系电话</th>
					<th lay-data="{field:'username4'}">分公司名称</th>
					<th lay-data="{field:'username5'}">岗位</th>
					<th lay-data="{field:'username6'}">工作状态</th>
					<th lay-data="{field:'username7'}">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${staffs}" var="staff" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${staff.name}</td>
						<td>${staff.phone}</td>
						<td>${staff.compName}</td>
						<td>${staff.post}</td>
						<td>${staff.workCond}</td>
						<td>${staff.remark}</td>
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
			location.href = "/som/exportOnTheJob";
		});
	</script>
</body>
</html>