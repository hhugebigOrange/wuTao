<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增区域信息</title>
  <script src="/som/xunwei/js/layui.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <style type="text/css">
        *{margin: 0px auto;padding: 0px;}
        fieldset{width:1000px;}
        form{padding: 10px;}
        .layui-inline{margin-bottom: 10px;width: 300px;text-align: center}
    </style>
    <link rel="stylesheet" href="/som/xunwei/css/modules/layer/default/layer.css">
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>新增合同信息</legend>

    <form class="layui-form" action="">
        <div class="layui-inline">
            <label class="layui-form-label">服务区域</label>
            <div class="layui-input-block">
                <input name="mainService" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">缩写</label>
            <div class="layui-input-block">
                <input name="childService" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">区域地址</label>
            <div class="layui-input-block">
                <input name="custName" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block">
                <button class="layui-btn" lay-filter="demo1" lay-submit="">提交</button>
            </div>
        </div>
    </form>
</fieldset>
</body>
</html>