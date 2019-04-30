<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工单分析图表</title>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<script src="/som/xunwei/js/echarts.min.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/jquerysession.js"></script>
</head>
<body>
	<form action="/som/exportWorkAnalysisTable" method="post">
		<input type="hidden" value="${percentages['1小时以内']}" name="a">
		<input type="hidden" value="${percentages['1~2小时']}" name="b">
		<input type="hidden" value="${percentages['2~3小时']}" name="c">
		<input type="hidden" value="${percentages['3~4小时']}" name="d">
		<input type="hidden" value="${percentages['超过4小时']}" name="e">

		<input type="hidden" value="${percentages['1个工作日内']}" name="a2">
		<input type="hidden" value="${percentages['1~2个工作日']}" name="b2">
		<input type="hidden" value="${percentages['2个工作日以上']}" name="c2">
		<input type="hidden" value="${percentages['4小时内完成']}" name="d2">

		<input type="hidden" value="${x}" name="a3"> 
		<input type="hidden" value="${y}" name="b3"> 
		<input type="hidden" value="" name="f1"> 
		<input type="hidden" value="" name="f2"> 
		<input type="hidden" value="" name="f3"> 
		<input type="submit" value="导出">

	</form>
	<div id="container" style="width: 600px; height: 400px;"></div>
	<div id="container1" style="width: 600px; height: 400px;"></div>
	<div id="container2" style="width: 600px; height: 400px;"></div>

	<script type="text/javascript">
		var picInfo = "";
		var eChart = echarts.init(document.getElementById("container"));
		var a = $("input[name='a']").val()
		var b = $("input[name='b']").val()
		var c = $("input[name='c']").val()
		var d = $("input[name='d']").val()
		var e = $("input[name='e']").val()
		var app = {};
		app.title = '到达现场时间占比';
		options = {
			title : {
				text : '到达现场时间占比',
				x : 'center'
			},
			animation : false,//关闭动画效果，避免导出的图形不完整
			calculable : false,
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [ '1小时以内', '1~2小时', '2~3小时', '3~4小时', '超过4小时' ]
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ {
					value : a,
					name : '1小时以内'
				}, {
					value : b,
					name : '1~2小时'
				}, {
					value : c,
					name : '2~3小时'
				}, {
					value : d,
					name : '3~4小时'
				}, {
					value : e,
					name : '超过4小时'
				}, ],
				itemStyle : {
					normal : {
						label : {
							show : true,
							formatter : '{b} : {c} ({d}%)'
						},
						labelLine : {
							show : true
						}
					},
					emphasis : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
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
		$("input[name='f1']").attr("value", picInfo);
	</script>

	<script type="text/javascript">
		var picInfo = "";
		var eChart = echarts.init(document.getElementById("container1"));
		var a = $("input[name='a2']").val()
		var b = $("input[name='b2']").val()
		var c = $("input[name='c2']").val()
		var d = $("input[name='d2']").val()
		var app = {};
		app.title = '问题解决时间占比';
		options = {
			title : {
				text : '问题解决时间占比',
				x : 'center'
			},
			animation : false,//关闭动画效果，避免导出的图形不完整
			calculable : false,
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
			},
			legend : {
				orient : 'vertical',
				left : 'left',
				data : [ '1个工作日内', '1~2个工作日', '2个工作日以上', '4小时内完成' ]
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '60%' ],
				data : [ {
					value : a,
					name : '1个工作日内'
				}, {
					value : b,
					name : '1~2个工作日'
				}, {
					value : c,
					name : '2个工作日以上'
				}, {
					value : d,
					name : '4小时内完成'
				}, ],
				itemStyle : {
					normal : {
						label : {
							show : true,
							formatter : '{b} : {c} ({d}%)'
						},
						labelLine : {
							show : true
						}
					},
					emphasis : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			} ]
		};
		//setOptins
		eChart.setOption(options);
		var baseCanvas = $("#container1").find("canvas").first()[0];
		var width = baseCanvas.width;
		var height = baseCanvas.height;
		var ctx = baseCanvas.getContext("2d");
		
		//遍历，将后续的画布添加到在第一个上
		$("#container1").find("canvas").each(function(i, canvasObj) {
			if (i > 0) {
				var canvasTmp = $(canvasObj)[0];
				ctx.drawImage(canvasTmp, 0, 0, width, height);
			}
		});
		picInfo = baseCanvas.toDataURL();
		//echarts自带的获取图形的base64方法,使用全局变量接收
		//picInfo=eChart.getDataURL('png');
		$("input[name='f2']").attr("value", picInfo);
	</script>

	<script type="text/javascript">
		var picInfo = "";
		var eChart = echarts.init(document.getElementById("container2"));
		app.title = '工程师到达现场平均用时';
		var a=$("input[name='a3']").val()
		var b=$("input[name='b3']").val()
		var names = a.split(",");
	    var nums =  b.split(","); 
		options = {
			title : {
					text : '工程师到达现场平均用时',
					x : 'center'
			},
			animation : false,//关闭动画效果，避免导出的图形不完整
			calculable : false,
			xAxis : {
				type : 'category',
				data : names
			},
			yAxis : {
				type : 'value'
			},
			
			series : [ {
				data : nums,
				type : 'bar',
				itemStyle: {
					normal: {
						label: {
							show: true, //开启显示
							position: 'top', //在上方显示
							textStyle: { //数值样式
								color: 'black',
								fontSize: 16
							}
						}
					}
				}
			} ]
		};
		
		//setOptins
		eChart.setOption(options);
		var baseCanvas = $("#container2").find("canvas").first()[0];
		var width = baseCanvas.width;
		var height = baseCanvas.height;
		var ctx = baseCanvas.getContext("2d");
		//遍历，将后续的画布添加到在第一个上
		$("#container2").find("canvas").each(function(i, canvasObj) {
			if (i > 0) {
				var canvasTmp = $(canvasObj)[0];
				ctx.drawImage(canvasTmp, 0, 0, width, height);
			}
		});
		picInfo = baseCanvas.toDataURL();
		//echarts自带的获取图形的base64方法,使用全局变量接收
		//picInfo=eChart.getDataURL('png');
		$("input[name='f3']").attr("value", picInfo);
	</script>
</body>
</html>