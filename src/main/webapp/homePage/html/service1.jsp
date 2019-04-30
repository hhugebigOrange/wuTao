<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>总部客服</title>
<link rel="stylesheet" href="/som/xunwei/css/loginhomepage.css">
<link rel="stylesheet" href="/som/xunwei/css/layui.css">
<script src="/som/xunwei/js/jquery.js"></script>
<script src="/som/xunwei/js/layui.js"></script>
<style type="text/css">
</style>
</head>
<body style="margin: 10px;">
<div class="up">
    <div class="bigpic"><img src="/som/xunwei/images/biging.jpg" width="950" height="400px"/>
        <div class="bigpic-font"><p>${slogan}</p></div></div>
    <div class="left-o">
        <!--待受理客户事故及需求数量-->
        <div class="bghbpink">
            <p class="describe">待受理客户事故及需求数量</p>
            <p class="content">
            <a href="/som/business" style="color: white;">3</a>
            </p>
        </div>
        <!--工单生成-->
        <div class="bghbgreen">
            <p>
            <a href="/som/orderManage" style="color: white;">工单生成</a>
            </p>
        </div>
    </div>
</div>
<div class="layui-clear">
    <!--工单概况-->
    <dl class="contract-two">
        <dd class="title">工单概况</dd>
        <dd class="subordinate">
            <dl>
                <dd><span class="describe">本月工单</span></dd>
                <dd><span class="content">
                <a href="/som/orderSituation${params}" style="color: #4cbff6">${total}</a>
                </span></dd>
            </dl>
            <dl>
                <dd><span class="describe">未完成</span></dd>
                <dd><span class="content">
                <a href="/som/orderSituation${params1}" style="color: #4cbff6">${unFinished}</a>
                </span></dd>
            </dl>
            <dl>
                <dd><span class="describe">超时工单</span></dd>
                <dd><span class="content">?</span></dd>
            </dl>
        </dd>
    </dl>
    <!--客户满意度-->
    <dl class="contract-two">
        <dd class="title">客户满意度</dd>
        <dd class="subordinate">
            <dl>
                <dd><span class="describe">用户评价</span></dd>
                <dd><span class="content">
                <a href="/som/customerSatisfaction" style="color: #4cbff6">${evaNumber}</a>
                </span></dd>
            </dl>
            <dl>
                <dd><span class="describe">客户投诉</span></dd>
                <dd><span class="content">6</span></dd>
            </dl>
            <dl>
                <dd><span class="describe">客户表扬</span></dd>
                <dd><span class="content">17</span></dd>
            </dl>
        </dd>
    </dl>
</div>
</body>
</html>