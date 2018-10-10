<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改密码</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #fff;padding-bottom: 4rem;">
		<div class="changePassword">
			<div class="telphone">
				<input type="password" placeholder="请输入旧密码" id="oldPassword" onchange="validatePassword()"/>
			</div>
			<div class="telphone">
				<input type="password" placeholder="请输入新密码" id="newPassword"/>
			</div>
			<div class="telphone">
				<input type="password" placeholder="请再次输入新密码" id="repeatNewPassword"/>
			</div>
			<button onclick="updatePassword()">提交</button>
		</div>
		<script src="../webpage/modules/game/fore/js/jquery.min.js"></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js"></script>
		<script type="text/javascript">
			//验证旧密码
			function validatePassword(){
				var bool = true;
				var oldPassword = $('#oldPassword').val();
				$.ajax({
					type:"post",
					dataType:"json",
					data:{"password":oldPassword},
					url:"../fore/validatePassword",
					async:false,
					success:function(result){
						console.log(result);
						if(result == '2'){
							layer.msg('密码错误');
							bool = false;
						}
					}
				})
				console.log(bool);
				return bool;
			}
		//提交更改密码
		function updatePassword(){
			var newPassword = $('#newPassword').val();
			//密码验证失败
			if(!validatePassword){
				layer.msg("密码错误");
				return;
			}
			$.ajax({
				type:"post",
				dataType:"json",
				data:{"password":newPassword},
				url:"../fore/updatePassword",
				async:false,
				success:function(result){
					console.log(result);
					if(result == '1'){
						layer.msg('修改成功');
						window.location.href="../fore/logout";
					}
				}
			})
		}
		</script>
	</body>
</html>