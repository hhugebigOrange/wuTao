<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加用户页面</title>
</head>
<body>
<div>
		<form action="/som/doAddCustomer">
			<table>
				<tr>
					<th>客户名称</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<th>合同编码</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<th>联系人</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<th>联系电话</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<th>合同期限</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<th>合同起始日</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<th>合同终止日</th>
					<td><input type="text" name="customerName"></td>
				</tr>
				<tr>
					<td><input type="submit" name="提交"></td>
				</tr>
			</table>

		</form>
	</div>
</body>
</html>