<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<div class="title">
	<span>用户管理</span><span class="particulars">基本信息>>用户管理</span>
</div>
<div class="abovebtn">
	<form class="layui-form" action="/som/userManageInfo" method="get">
	    <div class="layui-inline">
			<label class="layui-form-label">服务区域</label>
			<div class="layui-input-block">
				<input type="text" name="branceName" placeholder=""
					autocomplete="off" class="layui-input">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">姓名</label>
			<div class="layui-input-block">
				<input type="text" name="name" placeholder="" autocomplete="off"
					class="layui-input">
			</div>
		</div>
		<div class="layui-inline">
			<label class="layui-form-label">角色：</label>
			<div class="layui-input-block">
				<select name="role" lay-verify="required">
					<option value="">请选择</option>
					<option value="运维经理">运维经理</option>
					<option value="技术经理">技术经理</option>
					<option value="客户经理">客户经理</option>
					<option value="客服">客服</option>
					<option value="工程师">工程师</option>
					<option value="驻现场人员">驻现场人员</option>
				</select>
			</div>
		</div>
		<div class="btn">
			<button class="layui-btn layui-bg-gray">查询</button>
			<button class="layui-btn layui-bg-gray">查看所有</button>
			<button type="button" class="layui-btn layui-bg-gray" id="add">新增</button>
			<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
		</div>
	</form>
	<div class="labnum">
		<label>总数：<u>&nbsp&nbsp&nbsp&nbsp<span id="sum">${total}</span>&nbsp&nbsp&nbsp&nbsp
		</u></label>
	</div>
</div>
<div>
	<table lay-filter="demo">
		<thead>
			<tr>
				<th lay-data="{field:'username1'}">NO</th>
				<th lay-data="{field:'username2'}">员工编号</th>
				<th lay-data="{field:'username3'}">服务区域</th>
				<th lay-data="{field:'username4'}">姓名</th>
				<th lay-data="{field:'username5'}">电话</th>
				<th lay-data="{field:'username6'}">角色</th>
				<th lay-data="{field:'username7'}">是否涉密</th>
				<th lay-data="{field:'username8'}">涉密等级</th>
				<th lay-data="{field:'username9'}">创建时间</th>
				<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">功能项</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${staffInfos}" var="staff" varStatus="status">
				<tr>
					<td>${status.index+1}</td>
					<td>${staff.staffId}</td>
					<td>${staff.compName}</td>
					<td>${staff.name}</td>
					<td>${staff.phone}</td>
					<td>${staff.post}</td>
					<td>${staff.secret}</td>
					<td>${staff.secretLevel}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd"
							value="${staff.createDate}" /></td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">修改</a>
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="change">删除</a>
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

	$('#add').on('click', function() {
		//页面层
		layer.open({
			type : 2,
			title : false,
			area : [ '420px', '500px' ], //宽高
			shadeClose : true, //点击遮罩关闭
			content : [ '/som/baseinfo/html/addManagerRole.jsp', 'yes' ]
		//这里content是一个URL，不让iframe出现滚动条
		});
	})

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
		location.href = "/som/exportStaff";
	});
</script>
</body>
</html>