<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工单管理</title>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<link rel="stylesheet"
	href="/som/xunwei/css/modules/layer/default/layer.css">
<link rel="stylesheet"
	href="/som/xunwei/css/modules/laydate/default/laydate.css">
<link rel="stylesheet" href="/som/xunwei/css/body.css">
</head>
<body>
	<div class="title">
		<span>工单管理</span><span class="particulars">服务管理>>工单管理</span>
	</div>
	<div class="abovebtn">
		<form class="layui-form" action="/som/orderManage" method="get">
			<div class="layui-inline">
				<label class="layui-form-label">客户名称：</label>
				<div class="layui-input-block">
					<input type="text" name="custName" 
						placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机器编码：</label>
				<div class="layui-input-block">
					<input type="text" name="machCode" 
						placeholder="" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">工单类型：</label>
				<div class="layui-input-block">
					<select name=faultType >
						<option value="">请选择</option>
						<option value="需求类">需求类</option>
						<option value="事故类">事故类</option>
						<option value="事件类">事件类</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">日期</label>
				
				<div class="layui-input-inline">
					<input name="startDate" class="layui-input" type="text" 
						placeholder="起始日期" autocomplete="off" 
						id="contractPeriod">
				</div>
				<div class="layui-input-inline">
					<input name="endDate" class="layui-input" type="text" 
						placeholder="终止日期" autocomplete="off" 
						id="contractPeriod1">
				</div>
			</div>
			<div class="btn">
				<button type="submit" class="layui-btn layui-bg-gray">查询</button>
				<button type="button" class="layui-btn layui-bg-gray" id="add">新增</button>
				<button class="layui-btn layui-bg-gray" id="selectAll">查看所有</button>
				<button type="button" class="layui-btn layui-bg-gray" id="export" style="width: 130px;">导出</button>
			</div>
		</form>
	</div>
	<div>
		<table lay-filter="demo">
			<thead>
				<tr>
					<th lay-data="{field:'username1'}">序号</th>
					<th lay-data="{field:'username2'}">客户名称</th>
					<th lay-data="{field:'username3'}">工单号</th>
					<th lay-data="{field:'username4'}">设备名称</th>
					<th lay-data="{field:'username5'}">机器编码</th>
					<th lay-data="{field:'username6'}">设备序列号</th>
					<th lay-data="{field:'username7'}">设备型号</th>
					<th lay-data="{field:'username8'}">设备位置</th>
					<th lay-data="{field:'username9'}">报修人姓名</th>
					<th lay-data="{field:'username10'}">联系电话</th>
					<th lay-data="{field:'username11'}">报修时间</th>
					<th lay-data="{field:'username12'}">指定工程师</th>
					<th lay-data="{field:'username13'}">服务类型</th>
					<th lay-data="{field:'username14'}">服务类别</th>
					<th lay-data="{field:'username15'}">工单状态</th>
					<th lay-data="{field:'username16'}">合同类型</th>
					<th lay-data="{field:'username17'}">派单时间</th>
					<th lay-data="{field:'username18'}">到达现场时间</th>
					<th lay-data="{field:'username19'}">到达现场耗时</th>
					<th lay-data="{field:'username20'}">问题解决时间</th>
					<th lay-data="{field:'username21'}">维修结果</th>
					<th lay-data="{field:'username22'}">备注</th>
					<th lay-data="{width:178, align:'center', toolbar: '#barDemo'}">操作项</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${serviceInfos}" var="serviceInfo" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${serviceInfo.orderInfo.custName}</td>
						<td>${serviceInfo.orderInfo.woNumber}</td>
						<td>${serviceInfo.orderInfo.devName}</td>
						<td>${serviceInfo.orderInfo.machCode}</td>
						<td>${serviceInfo.orderInfo.esNumber}</td>
						<td>${serviceInfo.device.unitType}</td>
						<td>${serviceInfo.device.location}</td>
						<td>${serviceInfo.orderInfo.repairMan}</td>
						<td>${serviceInfo.orderInfo.repairService}</td>
							<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.orderInfo.repairTime}" /></td>
						<td>${serviceInfo.staffInfo.name}</td>
						<td>${serviceInfo.orderInfo.faultType}</td>
						<td>${serviceInfo.orderInfo.accidentType}</td>	
						<td>${serviceInfo.orderInfo.woStatus}</td>
						<td></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.orderInfo.sendTime}" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.arrTime}" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${serviceInfo.probSolve}" /></td>
						<td>${serviceInfo.orderInfo.treatmentMeasure}</td>
						<td></td>
						<td></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/html" id="barDemo">
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">受理</a>
       <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="export">导出工单</a>
    </script>
	<script type="text/javascript">
		layui.use('laydate', function() {
			var laydate = layui.laydate;
			//合同期限
			laydate.render({
				elem : "#contractPeriod"
			})

			laydate.render({
				elem : "#contractPeriod1"
			})
		})

		layui.use('table', function() {
			var table = layui.table;//加载表格模块
			table.init('demo', {
				height : 500 //设置高度
				,
				page : true
			//开启分页
			});
			
			table.on('tool(demo)', function(obj) {
				var data = obj.data;
				if (obj.event === 'export') {
					layer.open({
						type : 2,
						title : false,
						area : [ '900px', '600px' ],
						shadeClose : true,
						content : [ '/som/getOrer?woNumber='+ data.username2,
								'no' ]
					});
				}
			});
		});

		$('#selectAll').on('click', function() {
			location.href = "/som/orderManage";
		});

		$('#export').on('click', function() {
			location.href = "/som/exportOrder";
		});
		
		 $('#add').on('click', function(){
		        //页面层
		        layer.open({
		            type:2,
		            title:false,
		            area: ['420px', '800px'], //宽高
		            shadeClose: true, //点击遮罩关闭
		            content:["/som/newOrder", 'yes'] //这里content是一个URL，不让iframe出现滚动条
		        });
		    })
		
	</script>
</body>
</html>