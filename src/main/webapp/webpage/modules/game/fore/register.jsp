<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>注册账户</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #fff;">
		<div class="register">
		<form id="register" name="register">
			<div class="register-list clearfix">
				<span>手机号</span>
				<div class="register-right">
					<input type="text" placeholder="请输入手机号" onchange="validatePhoneNum()" id="phoneNum" name="phoneNum"/>
				</div>
			</div>
			<div class="register-list clearfix">
				<span>密码</span>
				<div class="register-right">
					<input type="password" placeholder="请输入密码" name="password"/>
				</div>
			</div>
			<div class="register-list clearfix">
				<span>确认密码</span>
				<div class="register-right">
					<input type="password" placeholder="请再次输入密码" id="ensurePassword" onchange="ensurePassword()"/>
				</div>
			</div>
			<div class="register-list clearfix">
				<span>姓名</span>
				<div class="register-right">
					<input type="text" placeholder="请输入姓名" name="realName" id="realName"/>
				</div>
			</div>
			<div class="register-list clearfix">
				<span>性别</span>
				<div class="register-right">
					<input name="sex" type="radio"  value="1"/>男
					<input name="sex" type="radio" style="margin-left: 2rem;" value="2"/>女
				</div>
			</div>
			<div class="register-list clearfix">
				<span>身份证号</span>
				<div class="register-right">
					<input name="idcard" type="text" placeholder="请输入身份证号" />
				</div>
			</div>
			<div class="register-list clearfix">
				<span>支付宝名</span>
				<div class="register-right">
					<input name="alipayName" type="text" placeholder="请输入支付宝名" />
				</div>
			</div>
			<div class="register-list clearfix">
				<span>支付宝号</span>
				<div class="register-right">
					<input name="alipayAccount" type="text" placeholder="请输入支付宝账号" />
				</div>
			</div>
			<p>请选择区域</p>
			<div class="register-list clearfix">
				<div data-toggle="distpicker">
					<div class="form-group">
					<label class="sr-only" for="province1"></label>
					<select class="form-control" id="province1" ></select>
					</div>
					<div class="form-group">
					<label class="sr-only" for="city1"></label>
					<select class="form-control" id="city1" ></select>
					</div>
					<div class="form-group">
					<label class="sr-only" for="district1"></label>
					<select class="form-control" id="district1" ></select>
					</div>
				</div>
			</div>
			<div class="register-list clearfix">
				<span>手机类型</span>
				<div class="register-right">
					<select style="width: 95%;" id="phoneOS" name="phoneOS">
						<option value="1">ios</option>
						<option value="0">安卓</option>
					</select>
				</div>				
			</div>
			<div class="register-lists clearfix">
				<div class="register-rights">
					<input type="text" placeholder="请输入验证码" id="verifyCode"/>
				</div>
				<span onclick="sendVerifyCode()">获取验证码</span>
			</div>
		</form>
			<button onclick="foreRegister()" >提交</button>
		</div>
		
		<script src="../webpage/modules/game/fore/js/jquery.min.js"></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js" ></script>
		<script src="../webpage/modules/game/fore/js/distpicker.data.js"></script>
	    <script src="../webpage/modules/game/fore/js/distpicker.js"></script>
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
						if(result == "0"){
		    				layer.msg("用户已存在");
						}						
					},
					error:function(){
						layer.msg("异常");
					}
				})
			}
	    	//验证重复密码
	    	function ensurePassword(){
	    		
	    	}
	    	//发送验证码
	    	function sendVerifyCode(){
	    		var phoneNum = $('#phoneNum').val();
	    		console.log("sendVerifyCode phoneNum:"+phoneNum);
	    		$.ajax({
	    			type:'post',
	    			dataType:"json",
	    			url:"../fore/sendVerifyCode",
	    			data:{'phoneNum':phoneNum},
	    			success:function(result){
	    				layer.msg("发送成功，请注意查收");
	    			},
	    			error: function(){
	    				layer.msg("异常");
	    			}
	    		})
	    	}
	    	//注册
	    	function foreRegister(){
	    		//获取area并拼接
	    		var area = 
	    			$('#province1').children("option:selected").text()+"/"+
	    			$('#city1').children("option:selected").text()+"/"+
	    			$('#district1').children("option:selected").text();
	    		console.log('area:*****'+area);
	    		//获取phoneOS
	    		var phoneOS = $('#phoneOS option:selected').val();
	    		var verifyCode = $('#verifyCode').val();
	    		console.log('phoneOS:*****'+phoneOS);
	    		//拼接请求数据
	    		var data = 
	    			$.param({'area':area})+'&'+
	    			$.param({'verifyCode':verifyCode})+'&'+
	    			$('#register').serialize();
	    		console.log("data:*****"+data);
	    		$.ajax({
	    			type:"post",
	    			dataType: "json",
	    			url:"../fore/register",
	    			data:data,
	    			success:function(result){
	    				console.log("register result:***"+result)
	    				if(result == "1"){
	    					//注册成功，跳转到登录页
	    					console.log("success");
	    					window.location.href='login';
	    				}
	    				if(result == "0"){
	    					layer.msg("注册失败，请检查你输入的信息");
	    				}
	    				if(result == "2"){
	    					layer.msg("验证码错误，请在60秒后重试");
	    				}
	    			},
	    			error: function(){
	    				layer.msg("异常");
	    			}
	    		})
	    	}
	    </script>
	</body>
</html>
