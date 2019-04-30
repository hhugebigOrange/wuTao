<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保养计划</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>保养计划</span><span class="particulars">保养管理>>保养计划</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/maintenancePlan" method="get">
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
					<select name="compName">
						<option value="">请选择</option>
						<option value="宝安分公司">宝安分公司</option>
						<option value="北京分公司">北京分公司</option>
						<option value="东莞分公司">东莞分公司</option>
						<option value="佛山赛盟">佛山赛盟</option>
						<option value="乐派数码">乐派数码</option>
						<option value="番禺分公司">番禺分公司</option>
						<option value="上海分公司">上海分公司</option>
						<option value="深圳分公司">深圳分公司</option>
						<option value="顺德分公司">顺德分公司</option>
						<option value="苏州分公司">苏州分公司</option>
						<option value="中山分公司">中山分公司</option>
						<option value="行业客户部">行业客户部</option>
						<option value="系统销售部">系统销售部</option>
					</select>
				</div>
			</div>

			<div class="btn">
				<button type="button" class="layui-btn layui-bg-gray" id="add">新增</button>
				<button class="layui-btn layui-bg-gray">查询</button>
				<button type="button" class="layui-btn layui-bg-gray">清除</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>
	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">客户名称</th>
					<th lay-data="{field:'username3'}">服务区域</th>
					<th lay-data="{field:'username4'}">合同号码</th>
					<th lay-data="{field:'username5'}">机器编码</th>
					<th lay-data="{field:'username6'}">联系人</th>
					<th lay-data="{field:'username7'}">联系电话</th>
					<th lay-data="{field:'username8'}">保养频率</th>
					<th lay-data="{field:'username9'}">责任工程师</th>
					<th lay-data="{field:'username10'}">后备工程师</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${maintenances}" var="maintenance"
					varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${maintenance.custName}</td>
						<td>${maintenance.compName}</td>
						<td>${maintenance.contractCode}</td>
						<td>${maintenance.machCode}</td>
						<td>${maintenance.repairMan}</td>
						<td>${maintenance.repairService}</td>
						<td>${maintenance.mainFrequency}</td>
						<td>${maintenance.staffId}</td>
						<td>${maintenance.reservEngineer}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script>
		layui.use('table', function() {
			var table = layui.table;//加载表格模块
			table.init('demo', {
				height : 500 //设置高度
				,
				page : true
			//开启分页
			});
		});

		$('#add')
				.on(
						'click',
						function() {
							//页面层
							layer
									.open({
										type : 2,
										title : false,
										area : [ '450px', '560px' ], //宽高
										shadeClose : true, //点击遮罩关闭
										content : [
												'/som/maintenanceManage/html/newMaintenance.jsp',
												'no' ]
									//这里content是一个URL，不让iframe出现滚动条
									});
						})

		$('#export').on('click', function() {
			location.href = "/som/exportmaintenanceMapper";
		});
	</script>
</body>
</html>