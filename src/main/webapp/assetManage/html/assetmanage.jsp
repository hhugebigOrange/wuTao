<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>资产管理</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>

<body>
	<div class="title">
		<p>资产管理</p>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/assetManage" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">服务区域</label>
				<div class="layui-input-block">
					<input type="text" name="serviceArea" placeholder=""
						autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">客户名称</label>
				<div class="layui-input-block">
					<input type="text" name="custName" 
						placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">资产属性</label>
				<div class="layui-input-block">
					<select name="assetAttr">
						<option value="">请选择</option>
						<option value="迅维">迅维</option>
						<option value="客户">客户</option>
						<option value="其他">其他</option>
					</select>
				</div>
			</div>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查询</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>

		<div class="labnum">
			<label>资产总数量：<u>&nbsp&nbsp&nbsp&nbsp<span>${totalDevices}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
			<label>签约资产数：<u>&nbsp&nbsp&nbsp&nbsp<span>${sign}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
			<label>未签约资产数：<u>&nbsp&nbsp&nbsp&nbsp<span>${noSign}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
	</div>

	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'no', width:70, sort: true}" rowspan="2">序号</th>
					<th lay-data="{field:'khmc', width:90}" rowspan="2">服务区域</th>
					<th lay-data="{field:'zcsx', width:90}" rowspan="2">客户名称</th>
					<th lay-data="{field:'bgbf', width:90}" rowspan="2">资产属性</th>
					<th lay-data="{field:'bgr', width:80}" rowspan="2">保管部门</th>
					<th lay-data="{field:'zcbh', width:90}" rowspan="2">保管人</th>
					<th lay-data="{field:'zclb', width:90}" rowspan="2">设备名称</th>
					<th lay-data="{field:'zcmc', width:90}" rowspan="2">资产编码</th>
					<th lay-data="{field:'xhgg', width:90}" rowspan="2">资产类别</th>
					<th lay-data="{field:'asdas', width:90}" rowspan="2">型号规格</th>
					<th lay-data="{field:'dw', width:70}" rowspan="2">单位</th>
					<th lay-data="{align:'center'}" colspan="5">账面数</th>
					<th lay-data="{field:'sps', width:80}" rowspan="2">实盘数</th>
					<th lay-data="{field:'username', width:80}" rowspan="2">实盘数</th>
					<th lay-data="{align:'center'}" colspan="2">盘亏、馈差额</th>
					<th lay-data="{field:'bz', width:70}" rowspan="2">备注</th>
					<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}" rowspan="2">功能项</th>
				</tr>
				<tr>
					<th lay-data="{field:'province', width:70}">数量</th>
					<th lay-data="{field:'city', width:70}">单价</th>
					<th lay-data="{field:'yz', width:70}">原值</th>
					<th lay-data="{field:'ljzj', width:90}">累计折旧</th>
					<th lay-data="{field:'jz', width:70}">净值</th>
					<th lay-data="{field:'sl', width:70}">数量</th>
					<th lay-data="{field:'je', width:70}">金额</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${assetNumbers}" var="assetNumber" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${assetNumber.device.serviceArea}</td>
						<td>${assetNumber.device.custArea}</td>
						<td>${assetNumber.device.assetAttr}</td>
						<td>${assetNumber.device.holdDepartment}</td>
						<td>${assetNumber.device.holdMan}</td>
						<td>${assetNumber.device.devName}</td>
						<td>${assetNumber.device.assetNumber}</td>
						<td></td>
						<td>${assetNumber.device.unitType}</td>
						<td></td>
						<td>${assetNumber.quantity}</td>
						<td>${assetNumber.unitPirce}</td>
						<td>${assetNumber.origValue}</td>
						<td>${assetNumber.accDep}</td>
						<td>${assetNumber.netValue}</td>
						<td>${assetNumber.realNumber}</td>
						<td>${assetNumber.money}</td>
						<td></td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">编辑</a>
    </script>
    
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

		$('#export').on('click', function() {
			location.href = "/som/exportAssetManage";
		});
		
	</script>
</body>
</html>