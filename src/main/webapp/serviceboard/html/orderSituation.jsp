<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工单概况</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>工单概况</span><span class="particulars">服务看板>>工单概况</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="${ctx}/orderSituation" method="get">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">客户名称：</label>
					<div class="layui-input-block">
						<input type="text" name="custName" placeholder=""
							autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">服务区域：</label>
					<div class="layui-input-block">
						<input type="text" name="serviceArea" placeholder="例如：BA（宝安分公司）"
							autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">日期：</label>
					<div class="layui-input-block">
						<input type="date" name="startDate" placeholder="起始日期" style="width: 200px"
							autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">——</label>
					<div class="layui-input-block">
						<input type="date" name="endDate" placeholder="结束日期" style="width: 200px"
							autocomplete="off" class="layui-input">
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">工单状态：</label>
					<div class="layui-input-block">
						<select name="workState" lay-verify="required">
							<option value="">请选择</option>
							<option value="待处理">待处理</option>
							<option value="已处理">已受理</option>
							<option value="处理中">处理中</option>
							<option value="二次上门">二次上门</option>
							<option value="维修完成">维修完成</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">故障类型：</label>
					<div class="layui-input-block">
						<select name="faultType" lay-verify="required">
							<option value="">请选择</option>
							<option value="事故类">事故类</option>
							<option value="需求类">需求类</option>
							<option value="事件类">事件类</option>
						</select>
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

		<div class="labnum">
			<label>工单总数：<u>&nbsp&nbsp&nbsp&nbsp<span id="wasdue">${totalOrders}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>未完成工单：<u>&nbsp&nbsp&nbsp&nbsp<span id="renewal">${ncpOrder}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>已完成工单：<u>&nbsp&nbsp&nbsp&nbsp<span id="renewa2">${cpOrder}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}" >工单号</th>
					<th lay-data="{field:'username3'}">设备序列号</th>
					<th lay-data="{field:'username4'}">客户名称</th>
					<th lay-data="{field:'username5'}">设备名称</th>
					<th lay-data="{field:'username6'}">设备编码</th>
					<th lay-data="{field:'username7'}">报修人</th>
					<th lay-data="{field:'username8'}">报修电话</th>
					<th lay-data="{field:'username9'}">客户地址</th>
					<th lay-data="{field:'username10'}">故障类型</th>
					<th lay-data="{field:'username11'}">处理状态</th>
					<th lay-data="{field:'username12'}">处理措施</th>
					<th lay-data="{field:'username13'}">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orders}" var="order" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${order.woNumber}</td>
						<td>${order.esNumber}</td>
						<td>${order.custName}</td>
						<td>${order.devName}</td>
						<td>${order.machCode}</td>
						<td>${order.repairMan}</td>
						<td>${order.repairService}</td>
						<td>${order.custAddr}</td>
						<td>${order.faultType}</td>
						<td>${order.woStatus}</td>
						<td>${order.treatmentMeasure}</td>
						<td>${order.remark}</td>
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
			location.href = "/som/exportOrderSituation";
		});
	</script>
</body>
</html>