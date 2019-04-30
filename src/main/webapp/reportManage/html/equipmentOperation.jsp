<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<script src="/som/xunwei/js/echarts.min.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/jquerysession.js"></script>
<title>设备运转状态表</title>
</head>
<body>
	<form action="/som/exportEquipmentOperation" method="post">
		<input type="hidden" value="${operationRate}" name="a"> 
		<input type="hidden" value="${custName}" name="b">
		<input type="hidden" value="" name="d">
		<input type="submit" value="导出">
	</form>
	<div id="container" style="width: 600px; height: 400px;"></div>
</body>
<script type="text/javascript">
	var picInfo = "";
	var eChart = echarts.init(document.getElementById("container"));
	var a = $("input[name='a']").val()
	var b = $("input[name='b']").val()
	var app = {};
	option = {
		title : {
			text : '设备运转率表:' + b,
			x : 'center'
		},
		animation : false,//关闭动画效果，避免导出的图形不完整
		calculable : false,
		tooltip : {
			formatter : "{a} <br/>{b} : {c}%"
		},
		toolbox : {
			feature : {
				restore : {},
				saveAsImage : {}
			}
		},
		series : [ {
			name : '业务指标',
			type : 'gauge',
			detail : {
				formatter : '{value}%'
			},
			data : [ {
				value : a,
				name : '平均运转率'
			} ]
		} ]
	};
	eChart.setOption(option);
	var baseCanvas = $("#container").find("canvas").first()[0];
	var width = baseCanvas.width;
	var height = baseCanvas.height;
	var ctx = baseCanvas.getContext("2d");
	//遍历，将后续的画布添加到在第一个上
	$("#container").find("canvas").each(function(i, canvasObj) {
		if (i > 0) {
			var canvasTmp = $(canvasObj)[0];
			ctx.drawImage(canvasTmp, 0, 0, width, height);
		}
	});
	picInfo = baseCanvas.toDataURL();
	//echarts自带的获取图形的base64方法,使用全局变量接收
	//picInfo=eChart.getDataURL('png');
	$("input[name='d']").attr("value", picInfo);
</script>
</html>