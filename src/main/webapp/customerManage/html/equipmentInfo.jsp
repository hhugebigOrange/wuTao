<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备管理</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
<style type="text/css">
.layui-inline {
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="title">
		<span>设备管理</span><span class="particulars">设备及资产管理>>设备管理</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/equipmentInfo" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">服务区域：</label>
				<div class="layui-input-block">
					<input type="text" name="serviceArea" placeholder=""
						autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">客户名称：</label>
				<div class="layui-input-block">
					<input type="text" name="custName" placeholder=""
						autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">资产属性：</label>
				<div class="layui-input-block">
					<select name="assetAscription">
						<option value="">请选择</option>
						<option value="迅维">迅维</option>
						<option value="客户">客户</option>
						<option value="其他">其他</option>
					</select>
				</div>
			</div>
			<br>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查询</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="add">新增</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>

		<div class="labnum">
			<label>设备总数：<u>&nbsp&nbsp&nbsp&nbsp<span id="sum">${totalDevices}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>签约设备：<u>&nbsp&nbsp&nbsp&nbsp<span id="sum">${sign}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>未签约设备：<u>&nbsp&nbsp&nbsp&nbsp<span id="sum">${noSign}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
	</div>

	<div>
		<table lay-filter="demo" id="test" lay-data="{id: 'idTest'}">
			<thead>
				<tr>
					<th lay-data="{type:'checkbox'}"></th>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">服务区域</th>
					<th lay-data="{field:'username3'}">客户名称</th>
					<th lay-data="{field:'username4'}">合同编码</th>
					<th lay-data="{field:'username5'}">设备名称</th>
					<th lay-data="{field:'username6'}">机器编码</th>
					<th lay-data="{field:'username7'}">设备序列号</th>
					<th lay-data="{field:'username8'}">设备型号</th>
					<th lay-data="{field:'username9'}">设备位置</th>
					<th lay-data="{field:'username10'}">厂家</th>
					<th lay-data="{field:'username11'}">安装日期</th>
					<th lay-data="{field:'username12'}">服务年限</th>
					<th lay-data="{field:'username13'}">资产属性</th>
					<th lay-data="{field:'username14'}">资产编码</th>
					<th lay-data="{field:'username15'}">备注</th>
					<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">功能项</th>
					<th lay-data="{field:'username16'}">导出二维码</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${devices}" var="device" varStatus="status">
					<tr>
						<td></td>
						<td>${status.index+1}</td>
						<td>${device.serviceArea}</td>
						<td>${device.custArea}</td>
						<td>${device.contractNo}</td>
						<td>${device.devName}</td>
						<td>${device.machCode}</td>
						<td>${device.esNumber}</td>
						<td>${device.unitType}</td>
						<td>${device.location}</td>
						<td>${device.deviceBound}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${device.installedTime}" /></td>
						<td></td>
						<td>${device.assetAttr}</td>
						<td>${device.assetNumber}</td>
						<td></td>
						<td></td>
						<td><a href="/som/xunwei/qrcode/${device.machCode}.png" download="${device.machCode}二维码">导出二维码</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看二维码</a>
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="change">变动</a>
    </script>
	<script>
		// 新增
		$('#add').on(
				'click',
				function() {
					//页面层
					layer.open({
						type : 2,
						title : false,
						area : [ '900px', '600px' ], //宽高
						shadeClose : true, //点击遮罩关闭
						content : [
								'/som/customerManage/html/addEquipment.jsp',
								'yes' ]
					//这里content是一个URL，不让iframe出现滚动条
					});
				})
		// 变动
		$('#change').on(
				'click',
				function() {
					//页面层
					layer.open({
						type : 2,
						title : false,
						area : [ '900px', '600px' ],
						shadeClose : true,
						content : [
								'/som/customerManage/html/equipmentChange.jsp',
								'yes' ]
					});
				})

		layui.use('table', function() {

			var table = layui.table;//加载表格模块

			table.init('demo', {
				height : 500 //设置高度
				,
				toolbar : '#toolbarDemo',
				page : true
			//开启分页
			});

			//监听表格复选框选择
			table.on('checkbox(demo)', function(obj) {
				var checkStatus = table.checkStatus(obj.config.username1);
				switch (obj.event) {
				case 'getCheckData':
					var data = checkStatus.data;
					layer.alert(JSON.stringify(data));
					break;
				case 'getCheckLength':
					var data = checkStatus.data;
					layer.msg('选中了：' + data.length + ' 个');
					break;
				case 'isAll':
					layer.msg(checkStatus.isAll ? '全选' : '未全选');
					break;
				}
				;
			});

			table.on('tool(demo)', function(obj) {
				var data = obj.data;
				if (obj.event === 'detail') {
					layer.open({
						type : 2,
						title : false,
						area : [ '470px', '320px' ],
						shadeClose : true,
						content : [ '/som/qrCode?machod=' + data.username6,
								'no' ]
					});
				}
				if (obj.event === 'change') {
					layer
							.open({
								type : 2,
								title : false,
								area : [ '900px', '600px' ],
								shadeClose : true,
								content : [
										'/som/equipmentChange?machod='
												+ data.username6, 'no' ]
							});
				}
			});
		});
		
		$('#export').on('click', function(){
			location.href = "/som/exportEquipment";
		});
		
	</script>
</body>
</html>