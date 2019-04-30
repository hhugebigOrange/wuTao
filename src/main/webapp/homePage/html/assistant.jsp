<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运维助理登陆后首页（角色：分、子公司运维助理）</title>
<link rel="stylesheet" href="/som/xunwei/css/loginhomepage.css">
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/layui.js"></script>
<style type="text/css">
</style>
</head>
<body style="margin: 10px;">
	<div class="up">
		<div class="bigpic">
			<img src="/som/xunwei/images/biging.jpg" width="950" height="400px" />
			<div class="bigpic-font">
				<p>${slogan}</p>
			</div>
		</div>
		<div class="left">
			<!--账单管理-->
			<div class="bghbpink bg-font">
				<span> <a href="/som/billManagement" style="color: white;">账单管理</a>
				</span>
			</div>
			<!--工单生成-->
			<div class="bghbgreen bg-font">
				<span><a href="/som/newOrder" style="color: white;">工单生成</a></span>
			</div>
		</div>
	</div>
	<div class="layui-clear">
		<!--工单概况-->
		<dl class="contract-two">
			<dd class="title">工单概况</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">本月工单</span>
					</dd>
					<dd>
						<span class="content"> <a
							href="/som/orderSituation${params}" style="color: #4cbff6">${total}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">未完成</span>
					</dd>
					<dd>
						<span class="content"> <a
							href="/som/orderSituation${params1}" style="color: #4cbff6">${unFinished}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">超时工单</span>
					</dd>
					<dd>
						<span class="content">15</span>
					</dd>
				</dl>
			</dd>
		</dl>
		<!--客户满意度-->
		<dl class="contract-two">
			<dd class="title">客户满意度</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">用户评价</span>
					</dd>
					<dd>
						<span class="content"> <a href="/som/customerSatisfaction"
							style="color: #4cbff6">${evaNumber}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">客户投诉</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfaction?three=3"
							style="color: #4cbff6">${three}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">客户表扬</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfaction?five=5"
							style="color: #4cbff6">${five}</a>
						</span>
					</dd>
				</dl>
			</dd>
		</dl>
	</div>
	
	<script type="text/javascript">
	function attention() {
	     event.stopPropagation();
	     // code
	     layer.open({
				type : 2,
				title : false,
				area : [ '420px', '800px' ], //宽高
				shadeClose : true, //点击遮罩关闭
				content : [ "/som/newOrder", 'yes' ]
			//这里content是一个URL，不让iframe出现滚动条
			});
	}
	</script>
</body>
</html>