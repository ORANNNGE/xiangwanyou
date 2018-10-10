<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>注册</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body>
		<div class="login">
			<img src="../webpage/modules/game/fore/images/logo.png" class="logo" />
			<form  method="post" id="loginForm">
				<div class="user clearfix">
				<img src="../webpage/modules/game/fore/images/user.png" style="width: 12%;" />
				<input type="text" placeholder="手机号" name="phoneNum"/>
			</div>
			<div class="user clearfix">
				<img src="../webpage/modules/game/fore/images/password.png" style="width: 10%;" />
				<input type="password" placeholder="密码" name="password"/>
				<img src="../webpage/modules/game/fore/images/eyes.png" style="float:right;width: 10%;margin-top: 0.3rem;" />
			</div>
			<span onclick="login()">登录</span>
			<div class="login-other clearfix">
				<a href="register">注册新用户</a>
				<a href="../page/forgotPassword">找回密码</a>
			</div>
			</form>
			<!-- <div class="user clearfix">
				<img src="../webpage/modules/game/fore/images/user.png" style="width: 12%;" />
				<input type="text" placeholder="用户名" />
			</div>
			<div class="user clearfix">
				<img src="../webpage/modules/game/fore/images/password.png" style="width: 10%;" />
				<input type="password" placeholder="密码" />
				<img src="../webpage/modules/game/fore/images/eyes.png" style="float:right;width: 10%;margin-top: 0.3rem;" />
			</div>
			<span>登录</span>
			<div class="login-other clearfix">
				<a href="#">注册新用户</a>
				<a href="#">找回密码</a>
			</div> -->
		</div>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.2.2.1.min.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/TouchSlide.1.1.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.SuperSlide.2.1.1.js" ></script>
		<script type="text/javascript">
		function login() {
            $.ajax({
            //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "../fore/login" ,//url
                data: $('#loginForm').serialize(),
                success: function (result) {
                    console.log(result);//打印服务端返回的数据(调试用)
                    if (result == '1') {
                        console.log("登录成功");
                    	window.location.href='../fore/index';
                    }
                    if (result == '2') {
                        layer.msg("密码错误");
                    }
                    if (result == '3') {
                        layer.msg("用户名错误");
                    }
                },
                error : function() {
                    layer.msg("异常！");
                }
            });
        }
		</script>
	</body>
</html>
