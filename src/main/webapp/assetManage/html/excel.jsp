<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>excel导入</title>
<script src="/som/xunwei/js/layui.js"></script>
<script src="/som/xunwei/js/jquery.js"></script>
</head>
<body>
<form enctype="multipart/form-data" id="batchUpload"  action="user/upload" method="post" class="form-horizontal">    
    <button class="btn btn-success btn-xs" id="uploadEventBtn" style="height:26px;"  type="button" >选择文件</button>  
    <input type="file" name="file"  style="width:0px;height:0px;" id="uploadEventFile">  
    <input id="uploadEventPath"  disabled="disabled"  type="text" placeholder="请选择excel表" style="border: 1px solid #e6e6e6; height: 26px;width: 200px;">                                           
</form>  
<button type="button" class="btn btn-success btn-sm"  onclick="user.uploadBtn()">上传</button> 
<script type="text/javascript">
var User = function(){  
	    this.init = function(){  
	          
	        //模拟上传excel  
	         $("#uploadEventBtn").unbind("click").bind("click",function(){  
	             $("#uploadEventFile").click();  
	         });  
	         $("#uploadEventFile").bind("change",function(){  
	             $("#uploadEventPath").attr("value",$("#uploadEventFile").val());  
	         });  
	          
	    };  
	    //点击上传按钮  
	    this.uploadBtn = function(){  
	        var uploadEventFile = $("#uploadEventFile").val();  
	        if(uploadEventFile == ''){  
	            alert("请选择excel,再上传");  
	        }else if(uploadEventFile.lastIndexOf(".xls")<0){//可判断以.xls和.xlsx结尾的excel  
	            alert("只能上传Excel文件");  
	        }else{  
	            var url =  '/user/upload/';  
	            var formData = new FormData($('form')[0]);  
	            user.sendAjaxRequest(url,'POST',formData);  
	        }  
	    };  
	      
	    this.sendAjaxRequest = function(url,type,data){  
	        $.ajax({  
	            url : url,  
	            type : type,  
	            data : data,  
	            success : function(result) {  
	                alert( result);  
	            },  
	            error : function() {  
	                alert( "excel上传失败");  
	            },  
	            cache : false,  
	            contentType : false,  
	            processData : false  
	        });  
	    };  
	}  
	var user;  
	$(function(){  
	    user = new User();  
	    user.init();  
	});
</script>
</body>
</html>