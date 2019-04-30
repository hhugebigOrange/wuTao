<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运维总监登陆后首页</title>
<link rel="stylesheet" href="/som/xunwei/css/major.css" type="text/css"></link>
<link rel="stylesheet" href="/som/xunwei/css/layui.css" type="text/css"></link>
<script src="/som/xunwei/js/jquery.js" type="text/javascript"></script>
<script src="/som/xunwei/js/layui.js" type="text/javascript"></script>
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
			<!--月度报告-->
			<div class="pie">
				<p><a href="/som/xunwei/html/Statement/KPI/Headofficeoperation.html">月度报告</a></p>
				<img src="/som/xunwei/images/pic.png" height="280" width="325" />
			</div>
		</div>
	</div>
	<div class="layui-clear">
		<!--合同管理-->
		<dl class="contract-three">
			<dd class="title">合同管理</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">合同总数</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/contractSituation" style="color: #4cbff6">${totalContract}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">已到期</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/contractSituation?timeout=1" style="color: #4cbff6">${timeContracts }</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">一年内到期</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/contractSituation?dueTo=1" style="color: #4cbff6">${dueToContracts}</a>
						</span>
					</dd>
				</dl>
			</dd>
		</dl>
		<!--工单概况-->
		<dl class="contract-three">
			<dd class="title">工单概况</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">本月工单</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/searchOrder${params}" style="color: #4cbff6">${total}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">未完成</span>
					</dd>
					<dd>
	                    <span class="content">
						<a href="/som/searchOrder${params1}" style="color: #4cbff6">${unFinished}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">超时工单</span>
					</dd>
					<dd>
						<span class="content">?</span>
					</dd>
				</dl>
			</dd>
		</dl>
		<!--客户满意度-->
		<dl class="contract-three">
			<dd class="title">客户满意度</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">用户评价</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager" style="color: #4cbff6">${evaNumber}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">客户投诉</span>
					</dd>
					<dd>
						<span class="content">6</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">客户表扬</span>
					</dd>
					<dd>
						<span class="content">17</span>
					</dd>
				</dl>
			</dd>
		</dl>
	</div>
</body>
</html>