<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>二维码图片</title>
 <style type="text/css">
        *{margin: 0px;padding: 0px;}
        .bg-707070{background-color: #707070;width:450px;padding: 10px;height:300px;}
        .nav{border-radius: 10px;background-color: white;padding:20px;height: 260px;font-weight: bolder;}
        .left{float: left;}
        .logo-fontone{font-size:18px;color: #3F3F3F}
        .code{position: relative;top:-20px;font-size:30px;}
        .font-big{font-size: 25px;}
        .qrcode{float: right}
        .top-10{margin-top: 10px;}
        .tel{margin-left:107px;}
    </style>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    
</head>
<body>
 <div class="bg-707070">
        <div class="nav">
            <div class="left">
                <img src="/som/xunwei/images/logo-black.png" height="50" />
                <p class="logo-fontone">您身边的文印管理服务专家</p>
                <p class="font-big top-10">
                    <span>机器<br>编码</span>
                    <span class="code">${machod}</span></p>
            </div>
            <div class="qrcode">
                <img src="/som/xunwei/qrcode/${machod}.png" height="100" width="100"/>
                <p>微信报修请扫码</p>
            </div>
            <p class="layui-clear"></p>
            <p class="font-big top-10">
                <span>服务电话</span>
                <span>020-85566766</span><br>
                <span class="tel">020-38255590</span></p>
            <p class="font-big">公司名称 广州乐派数码科技有限公司</p>
        </div>
    </div>
</body>
</html>