<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <title>口号修改</title>
    <script src="/som/xunwei/js/layui.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <style type="text/css">
        *{margin: 0px auto;padding: 0px;}
        .title{color:#4cbff6;font-size:18px;width:1200px;border-bottom: 2px solid #cfe2ef;padding: 10px;margin-bottom: 20px;}
        .title .particulars{float: right;color: #cccccc;font-size: small;margin-top: 10px;}
        form{padding: 10px;width: 400px;}
        .people{width: 400px;text-align: center;margin-left: 40px}
        .people img{width: 125px;}
    </style>
    <script src="/som/xunwei/js/layui.js"></script>
    <script src="/som/xunwei/js/jquery.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <link rel="stylesheet" href="/som/xunwei/css/modules/layer/default/layer.css">
</head>
<body>
<div class="title">
    <span>口号修改</span><span class="particulars">系统管理>>口号修改</span>
</div>
    <form class="layui-form" action="/som/changeSlogan" method="get">
        <div class="layui-form-item">
            <label class="layui-form-label">输入口号</label>
            <div class="layui-input-block">
                <input name="slogan" class="layui-input" type="text" placeholder="请输入新口号" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-filter="demo1" lay-submit="">立即提交</button>
                <button class="layui-btn layui-btn-primary" type="reset">重置</button>
            </div>
        </div>
    </form>
</body>
</html>