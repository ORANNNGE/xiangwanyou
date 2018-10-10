<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>商城</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;">
		<div class="shop clearfix">
		<c:forEach items="${goods}" var="goods">
			<div class="shop-list">
				<img src="../${goods.picture}" />
				<p>${goods.name }</p>
			</div>
		</c:forEach>
		</div>
		<footer class="clearfix">
			<div class="footer-list" onclick="location.href='../fore/index'">
				<img src="../webpage/modules/game/fore/images/foot-tast1.png" />
				<p>任务</p>
			</div>
			<div class="footer-list">
				<img src="../webpage/modules/game/fore/images/footerMall2.png" />
				<p>商城</p>
			</div>
			<div class="footer-list" onclick="location.href='../fore/personal'">
				<img src="../webpage/modules/game/fore/images/footer-personal1.png" />
				<p>我的</p>
			</div>
		</footer>
	</body>
</html>