<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合同概况</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body style="width: 3000px;">
	<div class="title">
		<span>合同概况</span><span class="particulars">服务看板>>合同概况</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/contractSituation" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">服务区域：</label>
				<div class="layui-input-block">
					<select name="childService" lay-verify="required">
						<option value="">请选择</option>
						<option value="宝安">宝安</option>
						<option value="北京">北京</option>
						<option value="东莞">东莞</option>
					</select>
				</div>
			</div>
			<input type="hidden" name="timeout"/>
			<input type="hidden" name="dueTo"/>
			<div class="btn">
				<button class="layui-btn layui-bg-gray">查询</button>
				<button class="layui-btn layui-bg-gray">清除</button>
				<button class="layui-btn layui-bg-gray">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export">导出</button>
			</div>
		</form>

		<div class="labnum">
			<label>合同总数：<u>&nbsp&nbsp&nbsp&nbsp<span id="sum">${totalContracts}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>即将到期合同：<u>&nbsp&nbsp&nbsp&nbsp<span
					id="AboutToExpire">${dueToContracts}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>已到期合同：<u>&nbsp&nbsp&nbsp&nbsp<span id="WasDue">${timeContracts}</span>&nbsp&nbsp&nbsp&nbsp
			</u></label> <label>续期合同：<u>&nbsp&nbsp&nbsp&nbsp<span id="renewal">0</span>&nbsp&nbsp&nbsp&nbsp
			</u></label>
		</div>
	</div>
	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">NO</th>
					<th lay-data="{field:'username2'}">登记时间</th>
					<th lay-data="{field:'username3'}">经办部门</th>
					<th lay-data="{field:'username4'}">经办人</th>
					<th lay-data="{field:'username5'}">合同保管人</th>
					<th lay-data="{field:'username6'}">合同编码</th>
					<th lay-data="{field:'username7'}">合同名称</th>
					<th lay-data="{field:'username8'}">客户单位名称</th>
					<th lay-data="{field:'username9'}">签约日期</th>
					<th lay-data="{field:'username10'}">到期日期</th>
					<th lay-data="{field:'username11'}">离到期天数</th>
					<th lay-data="{field:'username12'}">期限(月)</th>
					<th lay-data="{field:'username13'}">合同约定到期提醒天数</th>
					<th lay-data="{field:'username14'}">合同到期预警</th>
					<th lay-data="{field:'username15'}">客户名称</th>
					<th lay-data="{field:'username16'}">外包所服务客户</th>
					<th lay-data="{field:'username17'}">合同标的金额</th>
					<th lay-data="{field:'username18'}">合同类型</th>
					<th lay-data="{field:'username19'}">合同性质</th>
					<th lay-data="{field:'username20'}">合同执行情况</th>
					<th lay-data="{field:'username21'}">合同终止情况</th>
					<th lay-data="{field:'username22'}">提前终止日</th>
					<th lay-data="{field:'username23'}">提前终止情况说明</th>
					<th lay-data="{field:'username24'}">移交归档日期</th>
					<th lay-data="{field:'username25'}">接交人</th>
					<th lay-data="{field:'username26'}">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${contracts}" var="contract" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${contract.regTime}" /></td>
						<td>${contract.handlingDepartment}</td>
						<td>${contract.agent}</td>
						<td>${contract.contractHoldman}</td>
						<td>${contract.contractNo}</td>
						<td></td>
						<td>${contract.custName}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${contract.startDate}" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd"
								value="${contract.endDate}" /></td>
						<td></td>
						<td>${contract.contractDeadline}</td>
						<td></td>
						<td></td>
						<td></td>
						<td>${contract.signingCompany}</td>
						<td></td>
						<td>${contract.contractType}</td>
						<td></td>
						<td></td>
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
			location.href = "/som/exportContractSituation";
		});
	</script>
</body>
</html>