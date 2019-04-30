<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户满意度评价页面</title>
<script type="text/javascript">
	window.onload = function() {
		var tfrow = document.getElementById('tfhover').rows.length;
		var tbRow = [];
		for (var i = 1; i < tfrow; i++) {
			tbRow[i] = document.getElementById('tfhover').rows[i];
			tbRow[i].onmouseover = function() {
				this.style.backgroundColor = '#ffffff';
			};
			tbRow[i].onmouseout = function() {
				this.style.backgroundColor = '#bedda7';
			};
		}
	};
</script>

<style type="text/css">
table.tftable {
	font-size: 12px;
	color: #333333;
	width: 100%;
	border-width: 1px;
	border-color: #9dcc7a;
	border-collapse: collapse;
}

table.tftable th {
	font-size: 12px;
	background-color: #abd28e;
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #9dcc7a;
	text-align: left;
}

table.tftable tr {
	background-color: #bedda7;
}

table.tftable td {
	font-size: 12px;
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #9dcc7a;
}
</style>
</head>
<body>
	<!-- 搜索区域 -->
	<div>
		<form action="${ctx}/doAddCustSatisfa" method="get">
			<table>
				<tr>
					<td>工单号</td>
					<td><input type="text" name="woNumber" /></td>
				</tr>
				<tr>
					<td>星评点击</td>
					<td><select name="custScore">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4" selected="selected">4</option>
							<option value="5">5</option>
					</select></td>
				</tr>
				<tr>
					<td>${msg}</td>
				</tr>
			</table>
			<div>
				<textarea rows="20" cols="50" name="custEva" /></textarea>
			</div>
			<input type="submit" value="提交">
		</form>
	</div>
</body>
</html>