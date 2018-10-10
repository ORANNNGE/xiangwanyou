<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="UTF-8">
		<title>账户明细</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;padding-bottom: 4rem;">
		<div class="notify">
		<c:forEach items="${items }" var="item">
			<div class="notify-list clearfix">
				<a class="circle"></a>
				<div class="notify-most">
					<h2>【${item.tasks.name}】</h2>
					<p>${item.tasks.phase}</p>
					<p><fmt:formatDate value="${item.createDate}" pattern="YYYY-MM-dd HH:mm"/></p>
				</div>
				<span>+${item.tasks.reward }</span>
			</div>
		</c:forEach>
		</div>
	</body>
</html>