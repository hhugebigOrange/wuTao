<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备变动页面</title>
 <script src="/som/xunwei/js/layui.js"></script>
    <link rel="stylesheet" href="/som/xunwei/css/layui.css">
    <style type="text/css">
        *{margin: 0px auto;padding: 0px;}
        fieldset{width:1080px;}
        form{padding: 10px;}
        .sfont{font-size: small}
        .layui-inline{margin-bottom: 15px;}
        .AddMachine,.remove{display: none;}
    </style>
    <link rel="stylesheet" href="/som/xunwei/css/modules/layer/default/layer.css">
    <script src="/som/xunwei/js/jquery.js"></script>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>变动</legend>
    <form class="layui-form" action="/som/doAddEquipment" method="get">
        <div class="layui-inline"  style="width: 300px;">
            <label class="layui-form-label">变动类型</label>
            <div class="layui-input-block" id="changetype">
                <select name="changetype" lay-verify="required" lay-filter="changetype">
                    <option value="">请选择</option>
                    <option value="0">移动</option>
                    <option value="1">加机</option>
                    <option value="2">变更</option>
                    <option value="3">撤除</option>
                </select>
            </div>
        </div>
        <!--加机时需填写的字段-->
        <div class="AddMachine">
            <div class="layui-inline">
                <label class="layui-form-label">客户名称</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">合同编码</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">机器编码</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">设备品牌</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">设备型号</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">设备幅画</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">设备类型</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">输出规格</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">序列号</label>
                <div class="layui-input-block">
                    <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="">
                </div>
            </div>
        </div>
        <!--撤除时需填写的字段-->
        <div class="layui-inline remove" style="width: 300px;">
            <div class="layui-input-block">
                <select name="way" lay-verify="">
                    <option value="">请选择</option>
                    <option value="0">退回公司</option>
                    <option value="1">报废处理</option>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">区域</label>
            <div class="layui-input-block">
                <input name="clientname" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">位置</label>
            <div class="layui-input-block">
                <input name="Contractcode" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">部门</label>
            <div class="layui-input-block">
                <input name="linkman" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">服务级别</label>
            <div class="layui-input-block">
                <input name="linkman" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">客户联络人</label>
            <div class="layui-input-block">
                <input name="linkman" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label sfont">客户联络电话</label>
            <div class="layui-input-inline">
                <input name="phone" class="layui-input" type="text" autocomplete="off" lay-verify="requiredphone">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">责任工程师</label>
            <div class="layui-input-inline">
                <input name="termofcontract " class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">后备工程师</label>
            <div class="layui-input-block">
                <input name="linkman" class="layui-input" type="text" placeholder="" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">资产编码</label>
            <div class="layui-input-block">
                <input name="linkman" class="layui-input" type="text" placeholder="必填项" autocomplete="off" lay-verify="required">
            </div>
        </div>
        <div class="layui-inline">
            <div class="layui-input-block">
                <button class="layui-btn" lay-filter="demo1" lay-submit="">立即提交</button>
                <button class="layui-btn layui-btn-primary" type="reset">重置</button>
            </div>
        </div>
    </form>
</fieldset>
<script>
    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit;
        form.on('select(changetype)', function(data){
            var changetype=$('#changetype input').val();
            if (changetype=="加机"){
                $(".AddMachine").show();
                $(".remove").hide();
            }
            else if (changetype=="撤除"){
                $(".remove").show();
                $(".AddMachine").hide();
            }
            else {
                $(".AddMachine").hide();
                $(".remove").hide();
            }
        })
    });
</script>
</html>