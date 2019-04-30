<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合同管理</title>
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
<style type="text/css">
body {
	width: 3100px;
}

.abovebtn {
	float: left;
	margin-left: 10px;
	margin-top: 14px;
}

.labnum {
	padding-top: 29px;
}

.labnum label {
	margin-left: 20px;
}

.contextmenu {
	display: none;
	position: absolute;
	width: 100px;
	margin: 0;
	padding: 0;
	background: #FFFFFF;
	border-radius: 5px;
	list-style: none;
	box-shadow: 0 15px 35px rgba(50, 50, 90, 0.1), 0 5px 15px
		rgba(0, 0, 0, 0.07);
	overflow: hidden;
	z-index: 999999;
}

.contextmenu li {
	border-left: 3px solid transparent;
	transition: ease .2s;
}

.contextmenu li a {
	display: block;
	padding: 10px;
	color: #B0BEC5;
	text-decoration: none;
	transition: ease .2s;
}

.contextmenu li:hover {
	background: #cde8ff;
	border-left: 3px solid #73befd;
}

.contextmenu li:hover a {
	color: #436EEE;
}
</style>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
</head>
<body>
	<div class="title">
		<span>客户管理</span><span class="particulars">客户管理>>客户管理</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/contractManage" method="get">
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
					<input type="text" name="serviceArea" placeholder=""
						autocomplete="off" class="layui-input">
				</div>
			</div>
			<br>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查询</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>
		<div class="labnum">
			<label>合同总数：<u>&nbsp&nbsp&nbsp&nbsp<span id="wasdue">${totalContracts}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>即将到期合同：<u>&nbsp&nbsp&nbsp&nbsp<span id="aaa">${dueToContracts}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>已到期合同：<u>&nbsp&nbsp&nbsp&nbsp<span id="bbb">${timeContracts}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>续期合同：<u>&nbsp&nbsp&nbsp&nbsp<span id="ccc">123</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
		<div>
			<table lay-filter="demo" id="test" lay-data="{id: 'idTest'}">
				<thead>
					<tr>
						<th lay-data="{field:'username1'}">NO</th>
						<th lay-data="{field:'username2'}">服务区域</th>
						<th lay-data="{field:'username3'}">合同编码</th>
						<th lay-data="{field:'username4'}">合同类型</th>
						<th lay-data="{field:'username5'}">合同性质</th>
						<th lay-data="{field:'username6'}">签约日期</th>
						<th lay-data="{field:'username7'}">到期日期</th>
						<th lay-data="{field:'username8'}">离到期天数</th>
						<th lay-data="{field:'username9'}">期限(月)</th>
						<th lay-data="{field:'username10'}">合同约定到期通知提醒天数</th>
						<th lay-data="{field:'username11'}">合同到期预警</th>
						<th lay-data="{field:'username12'}">外包所服务客户</th>
						<th lay-data="{field:'username13'}">提前终止日</th>
						<th lay-data="{field:'username14'}">备注</th>
						<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">操作项</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${contracts}" var="contract" varStatus="status">
						<tr>
							<td>${status.index+1}</td>
							<td>${contract.childService}</td>
							<td>${contract.contractNo}</td>
							<td>${contract.contractType}</td>
							<td></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${contract.startDate}" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${contract.endDate}" /></td>
							<td></td>
							<td>${contract.contractDeadline}</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">新增</a>
    </script>

		<script type="text/javascript">
			layui.use('table', function() {

				var table = layui.table;//加载表格模块

				table.init('demo', {
					height : 500 //设置高度
					,
					width : 1500,
					toolbar : '#toolbarDemo',
					page : true
				//开启分页
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
				});
			});

			$('#export').on('click', function() {
				location.href = "/som/exportContractManage";

			});
		</script>
</body>
</html>