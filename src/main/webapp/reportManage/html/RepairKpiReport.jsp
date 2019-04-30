<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>故障报修汇总饼图</title>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<script src="/som/xunwei/js/echarts.min.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/jquerysession.js"></script>
</head>
<body>
<form action="/som/exportRepairReport" method="post">
    <input type="hidden" value="${percentage['事件类']}" name="a">
	<input type="hidden" value="${percentage['事故类']}" name="b">
	<input type="hidden" value="${percentage['需求类']}" name="c">
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
		var total=a+b+c;
		var app = {};
		app.title = '故障报修汇总饼图';
		options = {
			    title : {
			        text: '故障报修汇总饼图',
			        x:'center'
			    },
			    animation : false,//关闭动画效果，避免导出的图形不完整
				calculable : false,
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        left: 'left',
			        data: ['事件类','事故类','需求类']
			    },
			    series : [
			        {
			            name: '访问来源',
			            type: 'pie',
			            radius : '55%',
			            center: ['50%', '60%'],
			            data:[
			                {value:a, name:'事件类'},
			                {value:b, name:'事故类'},
			                {value:c, name:'需求类'},
			            ],
			            itemStyle: {
			               normal:{ 
			                     label:{ 
			                       show: true, 
			                       formatter: '{b} : {c} ({d}%)' 
			                     }, 
			                     labelLine :{show:true} 
			                   },
			            	emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
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