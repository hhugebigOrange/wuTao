<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域管理</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet"
	href="/som/xunwei/css/modules/laydate/default/laydate.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>系统管理</span><span class="particulars">系统管理>>区域管理</span>
	</div>
	<div class="abovebtn">
		<div class="btn">
			<button class="layui-btn layui-bg-gray">新增</button>
			<button class="layui-btn layui-bg-gray">查看所有</button>
		</div>
	</div>
	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">序号</th>
					<th lay-data="{field:'username2'}">服务区域</th>
					<th lay-data="{field:'username3'}">缩写</th>
					<th lay-data="{field:'username4'}">区域地址</th>
					<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">操作项</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${compInfos}" var="compInfo" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${compInfo.compName}</td>
						<td>${compInfo.abbreviation}</td>
						<td>${compInfo.compLocation}</td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">编辑</a>
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="change">删除</a>
    </script>
	<script>
	$(function(){
		layui.use('laydate', function() {
			var laydate = layui.laydate;
			laydate.render({
				elem : '#date1'
			})
			laydate.render({
				elem : '#date2'
			})
		});
		
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
			location.href = "/som/exportCustSatisfaction";
		});
		
		table.on('tool(demo)', function(obj) {
			var data = obj.data;
			if (obj.event === 'detail') {
				layer.open({
					type : 2,
					title : false,
					area : [ '470px', '320px' ],
					shadeClose : true,
					content : [ '/som/qrCode?machod=' + data.username3,
							'no' ]
				});
			}
			if (obj.event === 'change') {
				layer.open({
					type : 2,
					title : false,
					area : [ '900px', '600px' ],
					shadeClose : true,
					content : [ '/som/equipmentChange?machod='+ data.username1,
							'no' ]
				});
			}
		});
	})
	</script>
</body>
</html>