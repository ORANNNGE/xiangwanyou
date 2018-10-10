<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>修改密码</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #fff;padding-bottom: 4rem;">
		<div class="changePassword">
			<div class="telphone">
				<input type="text" placeholder="请输入手机号" id="phoneNum" onchange="validatePhoneNum()"/>
			</div>
			<div class="verification">
				<input type="text" placeholder="请输入验证码" id="verifyCode"/>
				<span onclick="sendVerifyCode()">获取验证码</span>
			</div>
			<div class="telphone">
				<input type="password" placeholder="请输入新密码" id="newPassword"/>
			</div>
			<div class="telphone">
				<input type="password" placeholder="请再次输入新密码" id="repeatNewPassword"/>
			</div>
			<button onclick="forgotPassword()">提交</button>
		</div>
	</body>
	<script src="../webpage/modules/game/fore/js/jquery.min.js"></script>
	<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js"></script>
	<script type="text/javascript">
		//手机号验证
		function validatePhoneNum(){
			var phoneNum = $('#phoneNum').val();
			console.log(phoneNum);
			$.ajax({
				type:"post",
				dataType: "json",
				url:"../fore/validatePhoneNum",
				data:{"phoneNum":phoneNum},
				success: function(result){
					console.log("result"+result);
					if(result == "1"){
	    				layer.msg("用户不存在");
	    				return;
					}						
				},
				error:function(){
					layer.msg("异常");
				}
			})
		}
		//发送验证码
		function sendVerifyCode(){
			var phoneNum = $("#phoneNum").val();
			var bool = false;
			$.ajax({
				type:"post",
				dataType:"json",
				data:{"phoneNum":phoneNum},
				url:"../fore/sendVerifyCode",
				async:false,
				success:function(result){
					if(result == '1'){
						layer.msg("发送成功，请注意查收");
						bool = true;
					}
				}
			})
			return bool;
		}
		//提交手机号、验证码、和新密码
		function forgotPassword(){
			var phoneNum = $("#phoneNum").val();
			var newPassword = $("#newPassword").val();
			var verifyCode = $("#verifyCode").val();
			var data = {
					"phoneNum":phoneNum,
					"password":newPassword,
					"verifyCode":verifyCode,
					};
			$.ajax({
				type:"post",
				dataType:"json",
				data:data,
				url:"../fore/forgotPassword",
				async:false,
				success:function(result){
					if(result == '0'){
						layer.msg('手机号和验证码不一致');
						return;
					}
					if(result == '1'){
						layer.msg('修改成功');
						window.location.href='../page/login';
					}
					if(result == '2'){
						layer.msg('验证码错误');
						return;
					}
					if(result == '3'){
						layer.msg('用户不存在');
						return;
					}
				}
			})
			
		}
	</script>
</html>