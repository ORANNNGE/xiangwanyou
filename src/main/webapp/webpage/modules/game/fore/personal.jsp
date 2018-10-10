<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>个人中心</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;padding-bottom:5rem;">
		<div class="personal">
			<!-- session中的userId为空，显示登录 -->
			<c:if test="${empty sessionScope.userId}">
				<div class="per-title clearfix" onclick="location.href='../page/login'">
					<p></p>
					<img src="../webpage/modules/game/fore/images/logo.png" />
					<div class="per-title-right">
						<h2>登录</h2>
					</div>
				</div>
			</c:if>
			<!-- session 中的userId不为空，显示手机号 -->
			<c:if test="${not empty sessionScope.userId}">
				<div class="per-title clearfix">
					<p></p>
					<img src="../webpage/modules/game/fore/images/logo.png" />
					<div class="per-title-right">
					<h2>${sessionScope.phoneNum}</h2>
					<c:if test="${sessionScope.usersStatus == '1' }">
					<h2>用户编号：${sessionScope.userId}</h2>
					<h2>VIP期限 ：${expireDate}</h2>
					</c:if>
				</div>
				</div>
			</c:if>
			<div class="personal-list clearfix" onclick="selectPassedTasks()">
				<img src="../webpage/modules/game/fore/images/personal-money.png" />
				<span>账户余额${balance}</span>
				<a href="#">&gt;</a>
			</div>
			<div class="personal-list clearfix" onclick="withdraw()">
				<img src="../webpage/modules/game/fore/images/personal-withdraw.png" />
				<span>余额提现</span>
				<a href="#">&gt;</a>
			</div>
			<div class="personal-list clearfix" onclick="taskView()">
				<img src="../webpage/modules/game/fore/images/personal-tast.png" />
				<span>任务查看</span>
				<a href="#">&gt;</a>
			</div>
			<div class="personal-list clearfix" onclick="selectPassedAndNot()">
				<img src="../webpage/modules/game/fore/images/peraonal-notes.png" />
				<span>通知</span>
				<a href="#">&gt;</a>
			</div>
			<div class="personal-list clearfix" onclick="changePassword()" id="changePassword">
				<img src="../webpage/modules/game/fore/images/personal-changePassword.png" />
				<span >修改密码</span>
				<a href="#">&gt;</a>
			</div>
			<button class="logOut" onclick="location.href='../fore/logout'" id="logout">退出登录</button>
		</div>
		<footer class="clearfix">
			<div class="footer-list" onclick="location.href='../fore/index'">
				<img src="../webpage/modules/game/fore/images/foot-tast1.png" />
				<p>任务</p>
			</div>
			<div class="footer-list" onclick="location.href='../fore/shop'">
				<img src="../webpage/modules/game/fore/images/footerMall1.png" />
				<p>商城</p>
			</div>
			<div class="footer-list">
				<img src="../webpage/modules/game/fore/images/footer-personal2.png" />
				<p>我的</p>
			</div>
		</footer>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.2.2.1.min.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js" ></script>
		<script type="text/javascript">
			//验证是不是VIP
			function validateVIP(open){
				var status = "${sessionScope.usersStatus}";
				console.log("status"+status);
				if(status == ''){
					layer.msg('请登录');
				}else if(status == '0'){
					layer.msg('你还不是会员');
				}else if(status = '1'){
					location.href=open;
				}
			}
			//进入提现页面前验证用户会员状态
			function withdraw(){
				validateVIP('withdraw');
			}
			//进入任务管理页面前验证用户会员状态
			function taskView(){
				validateVIP('taskView');
			}
			//进入修改密码页面前验证用户会员状态
			function changePassword(){
				validateVIP('../page/changePassword');
			}
			//进入账户余额页面前验证用户会员状态
			function selectPassedTasks(){
				validateVIP('selectPassedTasks');
			}
			//进入通知页面前验证用户会员状态
			function selectPassedAndNot(){
				validateVIP('selectPassedAndNot');
			}
			//用户为登录，隐藏退出按钮
			$(function(){
				var status = "${sessionScope.usersStatus}";
				if(status == ''){
					$('#logout').hide();
				}
			})
		</script>
	</body>
</html>
