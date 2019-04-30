<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工修改</title>
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

	<div>
		<form action="${ctx}/doUpdateEngineer" method="get">
			<table>
				<tr>
					<th>员工编码为：${updateStaff.staffId}，请完成修改信息，点击提交</th>
				</tr>
				<tr>
					<td>姓名：<input type="text" value="${updateStaff.name}" name="name"></td>
					<td>电话：<input type="text" value="${updateStaff.phone}" name="phone"/></td>
					<td>岗位：<input type="text" value="${updateStaff.post}" name="post"/></td>
					<td><input type="hidden" value="${updateStaff.staffId}" name="id"/></td>
				</tr>
				<tr>
					<td><input type="submit" value="提交" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>