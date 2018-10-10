<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>余额提现</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;">
		<div class="balance">
			<div class="balance-title clearfix">
				<div class="balance-list active"><span>提现</span></div>
				<div class="balance-list"><span>提现记录</span></div>
			</div>
			<div class="balance-most">
				<div class="balance-tab1 balance-tab withdraw">
					<div class="withdraw-most">
						<form id="addWithdraw">
							<p>
								<span>提现金额：</span>
								<input type="number"  name="withdrawNum" id="withdrawNum" />
							</p>
							<p>
								<span>备注：</span>
								<input type="text" name="remarks" id="remarks"/>
							</p>
						</form>
							<div>您的余额<font color="#ff1111" id="balance">${balance}</font>元 最低提现<font color="#ff1111">20</font></div>
							<div>到账支付宝昵称：<font color="#ff1111">${alipayName }</font></div>
							<div >到账支付宝账号：<font color="#ff1111">${alipayAccount }</font></div>
							
							<div onclick="withdrawInfo()"><font color="#0000ff">提现说明></font></div>
							<!-- <div>100以上需额外支付2%手续费</div> -->
					</div>
					<button onclick="addWithdraw()">提交</button>
				</div>
				<div class="balance-tab2 balance-tab">
					<c:forEach var="item" items="${withdraws}">
						<div class="notify-list clearfix">
							<div class="notify-most">
							<c:if test="${item.state == '1'}">
								<h2>【提现】提现中</h2>
								<p><fmt:formatDate value="${item.createDate }" pattern="YYYY-MM-dd HH:mm:ss"/></p>
							</c:if>
							<c:if test="${item.state == '2'}">
								<h2>【提现】提现成功</h2>
								<p><fmt:formatDate value="${item.updateDate }" pattern="YYYY-MM-dd HH:mm:ss"/></p>
							</c:if>
							<c:if test="${item.state == '3'}">
								<h2>【提现】提现失败</h2>
								<p><fmt:formatDate value="${item.updateDate }" pattern="YYYY-MM-dd HH:mm:ss"/></p>
							</c:if>
							</div>
							<span>￥${item.sum}</span>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.2.2.1.min.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js" ></script>
		<script type="text/javascript">
			$(function(){
				$(".balance-tab1").show();
				$(".balance-list").click(function(){
					$(this).addClass("active");
					$(this).siblings().removeClass("active");
				})
				$(".balance-list").eq(0).click(function(){
					$(".balance-tab1").show();
					$(".balance-tab1").siblings().hide();
				})
				$(".balance-list").eq(1).click(function(){
					$(".balance-tab2").show();
					$(".balance-tab2").siblings().hide();
				})
			})
		</script>
		<script type="text/javascript">
			
			function addWithdraw(){
				var balance = parseFloat($('#balance').html());
				console.log("balance:"+balance);
				var userId = "${sessionScope.userId}";
				var withdrawNum = parseFloat($('#withdrawNum').val());
				var data = $('#addWithdraw').serialize();
				console.log("data"+data);
				console.log("userId"+userId);
				console.log("withdrawNum"+withdrawNum);	
				console.log("balance"+balance);	
				if(userId == "" || userId == 0) return;
				if(withdrawNum > balance){
					layer.msg('余额不足');
					return;
				}else if(withdrawNum < 20 || withdrawNum == 0 || isNaN(withdrawNum)){
					layer.msg('最少提现20！');
					return;
				}else if(withdrawNum >=100){
					if(withdrawNum*1.02>balance){
						layer.msg("余额不足");
						return;
					}
					balance = balance-withdrawNum*1.02;
					$("#balance").html(balance);
					layer.open({
						title: '提现说明'
						,content: '你提现超过100，系统自动扣除2%手续费'
						,closeBtn: 0
						,yes: function(index, layero) {
				        		console.log("........");
				        		window.location.reload();
				        	}
						}); 
				}else{
					if(withdrawNum+2 > balance){
						layer.msg("余额不足");
						return;
					}
					balance = balance - withdrawNum -2;
					$("#balance").html(balance);
					layer.open({
						title: '提现说明'
						,content: '系统自动扣除2元手续费'
						,closeBtn: 0
		                ,yes: function(index, layero) {
		                 window.location.reload();
		                }
						});
				}
				$.ajax({
	    			type:"post",
	    			dataType: "json",
	    			url:"addWithdraw",
	    			data:data,
	    			success:function(result){
	    				if(result == 3){
		    				layer.msg("最低提现20");
	    				}
	    				if(result == 2){
		    				layer.msg("余额不足");
	    				}
	    				if(result == 1){
		    				window.location.reload();
	    				}
	    			},
	    			error: function(){
	    			}
	    		})
			}
			
			function withdrawInfo(){
				layer.open({
					  title: '提现说明'
					  ,content: '提现手续费：单笔2元，超过100按2%收取'
					}); 
			}
		</script>
	</body>
</html>