<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户满意度</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet"
	href="/som/xunwei/css/modules/laydate/default/laydate.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>客户满意度</span><span class="particulars">服务看板>>客户满意度</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="${ctx}/customerSatisfaction" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">服务区域：</label>
				<div class="layui-input-block">
					<input type="text" name="serviceArea" 
						placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">客户名称：</label>
				<div class="layui-input-block">
					<input type="text" name="custName" 
						placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">客户评价：</label>
				<div class="layui-input-block">
					<select name="workState" lay-verify="required">
						<option value="">请选择</option>
						<option value="满意">满意</option>
						<option value="非常满意">非常满意</option>
						<option value="不满意">不满意</option>
					</select>
				</div>
			</div>
			<div class="layui-form-item" style="margin-top: 15px;">
				<div class="layui-inline date">
					<label class="layui-form-label">日期范围：</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input name="startDate" class="layui-input" type="text"
							placeholder="开始日期" autocomplete="off" lay-verify="date"
							id="date1">
					</div>
					<div class="layui-form-mid">-</div>
					<div class="layui-input-inline" style="width: 100px;">
						<input name="endDate" class="layui-input" type="text"
							placeholder="终止日期" autocomplete="off" lay-verify="date"
							id="date2">
					</div>
				</div>
				<div class="btn">
					<button class="layui-btn layui-bg-gray">查询</button>
					<button class="layui-btn layui-bg-gray">清除</button>
					<button class="layui-btn layui-bg-gray">查看所有</button>
					<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
				</div>
			</div>
		</form>

	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">工单号码</th>
					<th lay-data="{field:'username3'}">客户名称</th>
					<th lay-data="{field:'username4'}">报修人</th>
					<th lay-data="{field:'username5'}">报修电话</th>
					<th lay-data="{field:'username6'}">报修时间</th>
					<th lay-data="{field:'username7'}">完成时间</th>
					<th lay-data="{field:'username8'}">维修工程师</th>
					<th lay-data="{field:'username9'}">服务区域</th>
					<th lay-data="{field:'username10'}">实际得分</th>
					<th lay-data="{field:'username11'}">服务评价</th>
					<th lay-data="{field:'username12'}">投诉与建议内容</th>
					<th lay-data="{field:'username13'}">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${serviceInfos}" var="serviceInfo"
					varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${serviceInfo.orderInfo.woNumber}</td>
						<td>${serviceInfo.orderInfo.custName}</td>
						<td>${serviceInfo.orderInfo.repairMan}</td>
						<td>${serviceInfo.orderInfo.repairService}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.orderInfo.sendTime}" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.probSolve}" /></td>
						<td>${serviceInfo.staffInfo.name}</td>
						<td>${serviceInfo.staffInfo.compName}</td>
						<td>${serviceInfo.custScore}</td>
						<td>${serviceInfo.custSat}</td>
						<td>${serviceInfo.custEva}</td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
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
	})
	</script>
</body>
</html>