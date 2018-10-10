<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>首页</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
	</head>
	<body style="background-color: #eee;padding-bottom: 4rem;">
		<div class="hdbanner" id="hdbanner">
			<div class="bd">
				<ul>
				<c:forEach items="${notices }" var="item">
	                <li><img src="../${item.picture}" style="width: 100%;" /></li>
				</c:forEach>
	            </ul>
			</div>
            <div class="hd">
            	<ul class="bannerdots">
	                <li class="on"></li>
	                <li></li>
	                <li></li>
	            </ul>
            </div>
        </div>
        <div class="notice clearfix">
        	<img src="../webpage/modules/game/fore/images/notes.png" />
	        <div class="txtMarquee-top">
				<div class="bd">
					<ul>
					<c:forEach items="${informs}" var="item">
						<li><a  target="_blank">${item.content}</a></li>
					</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div class="data clearfix">
			<div class="data-list">
				<p>人气</p>
				<p>${stat.popularity }</p>
			</div>
			<div class="data-list">
				<p>已发布</p>
				<p>${stat.pulished }</p>
			</div>
			<div class="data-list">
				<p>已完成</p>
				<p>${stat.done }</p>
			</div>
		</div>
		<div class="todayTask">
			<div class="todayTask-title clearfix">
				<div class="todayTask-list active">
					<img src="../webpage/modules/game/fore/images/today.png" />
					<p>今日任务</p>
				</div>
				<div class="todayTask-list">
					<img src="../webpage/modules/game/fore/images/index_05.png" />
					<p>限时任务</p>
				</div>
				<div class="todayTask-list">
					<img src="../webpage/modules/game/fore/images/index_05.png" />
					<p>所有任务</p>
				</div>
			</div>
			<div class="todayTask-content">
				<div class="todayTask-tab1 todayTask-tab" id="aaa">
				<c:forEach items="${tasks}" var="item" varStatus="count">
					<div class="todayTask-tabs clearfix" onclick="location.href='getTasksDetails?tasksid=${item.id}'" data-state="1">
						<img src="../${item.icon}" />
						<div class="todayTask-right">
							<h2>${item.name}${item.phase }</h2>
							<p class="clearfix">
								<span>已抢：${item.numRemain}/${item.numTotal}</span>
								<span><img src="../webpage/modules/game/fore/images/hot.png"/>${item.numRemain}</span>
							</p>
							<p class="clearfix">
								<img src="../webpage/modules/game/fore/images/gold.png" />
								<span>${item.reward}</span>
								<span id="tasksState">任务进行中</span>
							</p>
							<input class="createDate"  value="${item.createDate}"  type="hidden"/>
						</div>
					</div>
				</c:forEach>
				</div>
			</div>
		</div>
		<footer class="clearfix">
			<div class="footer-list">
				<img src="../webpage/modules/game/fore/images/footer-tast2.png" />
				<p>任务</p>
			</div>
			<div class="footer-list"  onclick="location.href='../fore/shop'">
				<img src="../webpage/modules/game/fore/images/footerMall1.png" />
				<p>商城</p>
			</div>
			<div class="footer-list" onclick="location.href='../fore/personal'">
				<img src="../webpage/modules/game/fore/images/footer-personal1.png" />
				<p>我的</p>
			</div>
		</footer>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.2.2.1.min.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/TouchSlide.1.1.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.SuperSlide.2.1.1.js" ></script>
		<script type="text/javascript">
		TouchSlide({  
			slideCell:"#hdbanner",
			titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
			mainCell:".bd ul", 
			effect:"leftLoop", 
			autoPage:true,//自动分页
			autoPlay:true //自动播放
		});
		</script>
		<script>
			jQuery(".txtMarquee-top").slide({mainCell:".bd ul",autoPlay:true,effect:"topMarquee",vis:1,interTime:50,trigger:"click"});
		</script>
		<script type="text/javascript">
			$(function(){
				$(".todayTask-tab1").show();
				var tasks = $(".todayTask-tabs");
				for (var i = 0; i < tasks.length; i++) {
					if(tasks.eq(i).attr('data-state') == '0'){
						tasks.eq(i).hide();
					}
				}
				$(".todayTask-list").click(function(){
					tasks.show();
					$(this).addClass("active");
					$(this).siblings().removeClass("active");
				})
				$(".todayTask-list").eq(0).click(function(){
					for (var i = 0; i < tasks.length; i++) {
						if(tasks.eq(i).attr('data-state') == '0'){
							tasks.eq(i).hide();
						}
					}
				})
				$(".todayTask-list").eq(1).click(function(){
					tasks.hide();
				})
				$(".todayTask-list").eq(2).click(function(){
					
				})
			})
			//任务剩余时间
			function aaaa(){
				//找到隐域里的createDate
				$("#aaa div input").each(function () {
					//输出createDate
					console.log($(this).val());
					//从隐域获取任务创建时间，类型是java Date()
					var createDate = $(this).val();
					console.log("timeLeft:"+createDate);
					//转换为js的Date()
					var jsTimeLeft = new Date(createDate);
					var now = new Date();
					console.log(now.getTime());
					var value = jsTimeLeft.getTime()+10*3600*1000-now.getTime();
					console.log(value);
					
			        var days=Math.floor(value/(24*3600*1000))
			        //计算出小时数
			        var leave1=value%(24*3600*1000)    //计算天数后剩余的毫秒数
			        var hours=Math.floor(leave1/(3600*1000))
			        //计算相差分钟数
			        var leave2=leave1%(3600*1000)        //计算小时数后剩余的毫秒数
			        var minutes=Math.floor(leave2/(60*1000))
			        //任务剩余时间为0时，按钮改为已结
			        if(!(days>=0 && hours>=0 && minutes>=0)){
			        	days = 0;
			        	hours = 0;
			        	minutes = 0;
						//找到自定义属性的那一层的div,并赋值				
						$(this).parent().parent("div").attr("data-state","0");
						$(this).parent().find('p').eq(1).find('span').eq(1).text('已结束');
			        }
			        //拼接剩余时间并显示到对应的元素上
			        /* var jsTimeleftValue = "剩余"+days+"天"+hours+"小时"+minutes+"分";
			        console.log("剩余"+days+"天"+hours+"小时"+minutes+"分");
			        $('#jsTimeleftValue').text(jsTimeleftValue);  */
				})
			}
			var a = aaaa();
   			setInterval(aaaa,60000);
		</script>
	</body>
</html>
