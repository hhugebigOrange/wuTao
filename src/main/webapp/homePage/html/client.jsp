<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/layui.js"></script>
<link rel="stylesheet" href="/som/xunwei/css/loginhomepage.css">
</head>
<body>
	<div class="upleft">
		<!--故障报修及服务需求-->
		<dl class="bg-btn">
			<dd class="bg-blue">
				<a href="/som/serviceboard/html/faultRepair.jsp">故障报修及服务需求</a>
			</dd>
			<dd class="bg-green">
				<a href="/som/serviceboard/html/advice.jsp">反馈与建议</a>
			</dd>
			<dd class="bg-pink">
				<a href="/som/xunwei/html/Statement/KPI/client.html">KPI报告</a>
			</dd>
		</dl>
		<dl class="layui-clear"></dl>
		<!--合同管理-->
		<dl class="contract">
			<dd class="title">
				<a href="/som/contractSituation">合同管理</a>
			</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">主合同号</span>
					</dd>
					<dd>
						<span class="content">MPS0019890</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">开始日期</span>
					</dd>
					<dd>
						<span class="content">2016/7/1</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">结束日期</span>
					</dd>
					<dd>
						<span class="content">2019/6/30</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">合同总期数</span>
					</dd>
					<dd>
						<span class="content">60</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">已执行期数</span>
					</dd>
					<dd>
						<span class="content">27</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">未执行期数</span>
					</dd>
					<dd>
						<span class="content">33</span>
					</dd>
				</dl>
			</dd>
		</dl>
	</div>
	<!--讯维解决方案介绍-->
	<div class="solution">
		<a href="#">讯维解决方案介绍</a>
	</div>
	<div class="layui-clear"></div>
	<div class="down">
		<dl class="contract-three">
			<dd class="title">
				<a href="#">设备及资产</a>
			</dd>
			<dd class="subordinate">
				<dl>
					<dd>
						<span class="describe">合同设备数</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/equipmentSituation${params8}" style="color: #4cbff6">${totalDev}</a>
						</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">&nbsp</span>
					</dd>
					<dd>
						<span class="content">&nbsp</span>
					</dd>
				</dl>
				<dl>
					<dd>
						<span class="describe">客户资产</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/equipmentSituation${params9}" style="color: #4cbff6">${asset}</a>
						</span>
					</dd>
				</dl>
			</dd>
		</dl>
		<dl class="contract-three">
			<dd class="title">
				<a href="#">工单概况</a>
			</dd>
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
						<span class="content">？</span>
					</dd>
				</dl>
			</dd>
		</dl>
		<dl class="contract-three">
			<dd class="title">
				<a href="#">用户满意度</a>
			</dd>
			<dd class="satisfaction">
				<dl style="width: 120px;">
					<dd>
						<span class="describe">总评价数</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager${params2}" style="color: #4cbff6">${evaNumber}</a>
						</span>
					</dd>
				</dl>
				<dl class="star">
					<dd>
						<span class="describe">星级</span>
					</dd>
				</dl>
				<dl class="star">
					<dd>
						<span class="describe">5</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager${params3}" style="color: #4cbff6">${starLevel[4]}</a>
						</span>
					</dd>
				</dl>
				<dl class="star">
					<dd>
						<span class="describe">4</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager${params4}" style="color: #4cbff6">${starLevel[3]}</a>
						</span>
					</dd>
				</dl>
				<dl class="star">
					<dd>
						<span class="describe">3</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager${params5}" style="color: #4cbff6">${starLevel[2]}</a>
						</span>
					</dd>
				</dl>
				<dl class="star">
					<dd>
						<span class="describe">2</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager${params6}" style="color: #4cbff6">${starLevel[1]}</a>
						</span>
					</dd>
				</dl>
				<dl class="star">
					<dd>
						<span class="describe">1</span>
					</dd>
					<dd>
						<span class="content">
						<a href="/som/customerSatisfactionManager${params7}" style="color: #4cbff6">${starLevel[0]}</a>
						</span>
					</dd>
				</dl>
		</dl>
	</div>
</body>
</html>