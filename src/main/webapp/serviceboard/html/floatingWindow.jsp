<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:set var="No" value="1"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>浮动窗口</title>
</head>
<body>
	<h1>当前共有${orderNumber}条未处理工单</h1>
	<table>
		<c:forEach items="${orderInfos}" var="order" varStatus="status">
			<tr>
			    <td>${status.index+1}.</td>
				<td>客户名称:${order.custName },</td>
				<td>机器编码:${order.machCode },</td>
				<td>服务类别:${order.faultType },</td>
				<td>
				  <form action="${ctx}/sendOrder" method="get">
				  <input type="hidden" value="${order.woNumber}" name="woNumber">
				  <input type="submit" value="派单"/>
				  </form>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>