<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>任务记录</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;">
		<div class="taskView">
			<div class="taskView-title clearfix">
				<div class="taskView-list active" >任务进行中</div>
				<div class="taskView-list" >任务待审核</div>
				<div class="taskView-list" >已通过审核</div>
				<div class="taskView-list" >审核未通过</div>
			</div>
			<div class="taskView-most">
				
				<div class="taskView-tab taskView-tab" onclick="taskDetail()">
				<c:forEach var="item" items="${items}">
					<div class="taskView-tabs clearfix" data-state="${item.state}" onclick="location.href='getTasksDetails?tasksid=${item.tasks.id}'" >
						<div class="taskView-left">
							<h2>【${item.tasks.name}  ${item.tasks.phase}】</h2>
							<p><fmt:formatDate value="${item.createDate}" pattern="YYYY-MM-dd HH:mm"/></p>
						</div>
						<span>${item.tasks.reward}</span>
					</div>
				</c:forEach>
				</div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.2.2.1.min.js" ></script>
		<script type="text/javascript">
			$(function(){
				$('.taskView-tabs').show();
				var taskView = $('.taskView-tabs');
				console.log(taskView.size());
				$(".taskView-tab").show();	
				
				for(var i=0;i<taskView.size();i++){
					if(taskView.eq(i).attr("data-state") != '1'){
						taskView.eq(i).hide();
					}
				}
				
				$(".taskView-list").click(function(){
					$(this).addClass("active");
					$(this).siblings().removeClass("active");
				})
					$(".taskView-list").eq(0).click(function(){
						//$(".taskView-tab1").show();
						//$(".taskView-tab1").siblings().hide();
						$('.taskView-tabs').show();
						for(var i=0;i<taskView.size();i++){
							if(taskView.eq(i).attr("data-state") != '1'){
								taskView.eq(i).hide();
							}
						}
					})
					$(".taskView-list").eq(1).click(function(){
						//$(".taskView-tab2").show();
						//$(".taskView-tab2").siblings().hide();
						$('.taskView-tabs').show();
						for(var i=0;i<taskView.size();i++){
							if(taskView.eq(i).attr("data-state") != '2'){
								taskView.eq(i).hide();
							}
						}
						
					})
					$(".taskView-list").eq(2).click(function(){
						//$(".taskView-tab3").show();
						//$(".taskView-tab3").siblings().hide();
						$('.taskView-tabs').show();
						for(var i=0;i<taskView.size();i++){
							if(taskView.eq(i).attr("data-state") != '3'){
								taskView.eq(i).hide();
							}
						}
					})
					$(".taskView-list").eq(3).click(function(){
						//$(".taskView-tab4").show();
						//$(".taskView-tab4").siblings().hide();
						$('.taskView-tabs').show();
						for(var i=0;i<taskView.size();i++){
							if(taskView.eq(i).attr("data-state") != '4'){
								taskView.eq(i).hide();
							}
						}
					})
			})
		</script>
	</body>
</html>
