<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户汇总kpi柱状图</title>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<script src="/som/xunwei/js/echarts.min.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/jquerysession.js"></script>
</head>
<body>
<form action="/som/exportCustomerKpiSummary" method="post">
    <input type="hidden" value="${StandardRates[0].responseTimeRate}" name="a">
	<input type="hidden" value="${StandardRates[0].arrTimeRate}" name="b">
	<input type="hidden" value="${StandardRates[0].probSolveRate}" name="c">
	<input type="hidden" value="" name="d">
	<input type="submit" value="导出">
</form>
	<div id="container" style="width: 600px; height: 400px;"></div>
	
	<script type="text/javascript">
		var picInfo = "";
		var eChart = echarts.init(document.getElementById("container"));
		var a = $("input[name='a']").val()
		var b = $("input[name='b']").val()
		var c = $("input[name='c']").val()
		var app = {};
		app.title = '堆叠柱状图';
		options = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			animation : false,//关闭动画效果，避免导出的图形不完整
			calculable : false,
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : [ '响应时间', '到底现场时间', '问题解决时间' ]
			} ],
			yAxis : [ {
				type : 'value',
				axisLabel : {
					formatter : '{value} %'
				}
			} ],
			series : [ {
				name : '达标率目标',
				type : 'bar',
				data : [ 95, 95, 95 ]
			}, {
				name : '当月实际达标率',
				type : 'bar',
				data : [ a, b, c ]
			} ]
		};
		
		
		//setOptins
		eChart.setOption(options);
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
</body>
</html>