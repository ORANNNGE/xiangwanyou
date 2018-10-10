<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>消息通知</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;padding-bottom: 4rem;">
		<div class="notify">
				<c:forEach var="item" items="${items}">
					<div class="notify-list clearfix">
						<a class="circle"></a>
						<div class="notify-most">
							<h2>【${item.tasks.name} ${item.tasks.phase}】</h2>
							<p><fmt:formatDate value="${item.updateDate}" pattern="YYYY-MM-dd HH:mm"/></p>
						</div>
						<c:if test="${item.state == '3' }">
							<span>已通过</span>
						</c:if>
						<c:if test="${item.state == '4' }">
							<span>未通过</span>
						</c:if>
					</div>
				</c:forEach>
		</div>
	</body>
</html>