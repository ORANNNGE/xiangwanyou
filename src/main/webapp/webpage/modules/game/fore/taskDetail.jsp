<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>任务详情</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/base.css" />
		<link rel="stylesheet" href="../webpage/modules/game/fore/css/style.css" />
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.min.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.ui.widget.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.iframe-transport.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.fileupload.js" ></script>
	</head>
	<body style="background-color: #eee;">
		<div class="taskDetail">
			<div class="taskDetail-title">
				<img src="../${tasks.icon}" />
				<span>${tasks.name}${tasks.phase} </span>
			</div>
			<div class="taskDetail-most">
				<p class="p-title">任务编号：${tasks.code}</p>
				<p class="p-title">已抢<i>${tasks.numRemain }</i>/${tasks.numTotal}份，<i>￥${tasks.reward }</i>/份</p>
				<div class="taskDetail-content">
					<p>任务为长期任务，定时更新</p>
					<p>请用户不要下载好删除</p>
					<p>方便接下来的任务</p>
				</div>
				<!-- 任务详情 -->
				<p id="details"></p>
				<a href="${tasks.download}">点击下载游戏</a>
				
				<p class="p-bot" style="color: #ff1111;text-align: right;" id="jsTimeleftValue"></p>
				<input type="hidden" value="${tasks.createDate}" id="timeLeft">
			</div>
			<div class="taskDetail-commit-content">
				<span id="showCommitContent">提交内容 :</span>
			</div>
			<p class="rob" data-type="0">抢任务</p>
			<div class="onload">
				<form id="taskCommit">
					<div class="onload-img" onclick="show()">
						<img src="../webpage/modules/game/fore/images/pic.png" id="uploadIcon"/>
						<p>上传图片</p>
						<input type="file" name="files[]" id="fileupload" data-url="getImg" accept="image/*" multiple/>
						<input type="hidden" name="picture" id="filePaths"/>
						<input type="hidden" name="picture" id="usersTasksId"/>
					</div>
					<textarea name="commitContent" id="commitContent">请输入文字</textarea>
					<span class="ensure" onclick="taskCommit()">确认</span>
				</form>
			</div>
		</div>
		<footer class="clearfix">
			<div class="footer-list" onclick="location.href='index'">
				<img src="../webpage/modules/game/fore/images/footer-tast2.png" />
				<p>任务</p>
			</div>
			<div class="footer-list" onclick="location.href='../fore/shop'">
				<img src="../webpage/modules/game/fore/images/footerMall1.png" />
				<p>商城</p>
			</div>
			<div class="footer-list" onclick="location.href='../fore/personal'">
				<img src="../webpage/modules/game/fore/images/footer-personal1.png" />
				<p>我的</p>
			</div>
		</footer>
		<!-- <script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.2.2.1.min.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/layer.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.ui.widget.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.iframe-transport.js" ></script>
		<script type="text/javascript" src="../webpage/modules/game/fore/js/jquery.fileupload.js" ></script> -->
		
		<script type="text/javascript">
            function show(){
                document.getElementById("fileupload").click();    
            }
        </script>
        <script type="text/javascript">
        	//根据参数名获取url里的参数
        	function getQueryVariable(variable){
        		var query = window.location.search.substring(1);
        		var vars = query.split('&');
        		for(var i=0;i<vars.length;i++){
        			var pair = vars[i].split('=');
        				if(pair[0] == variable){return pair[1]}
        		}
        	}
        	//转义字符串里的标签
	        function HTMLDecode(text) { 
				 var temp = document.createElement("div"); 
				 temp.innerHTML = text; 
				 var output = temp.innerText || temp.textContent; 
				 temp = null; 
				 return output; 
				}
	        (function(){
	        	console.log("*****")
	        	document.getElementById("details").innerHTML=HTMLDecode("${tasks.details}");
	        })()
        </script>
		<script type="text/javascript">
				//上传图片
				$('#fileupload').fileupload({
		            dataType: 'json',   
		            maxFileSize: 2097152*3,  
		            done: function (e, data) {
		            	console.log(123456);
		            	var uploadPath = data.result.data;
		            	console.log("done-data:"+uploadPath);
		            	$("#uploadIcon").attr("src","../"+uploadPath);
		             	/* uploadPath = uploadPath.replace("\\", "\\\\");
		            	var filePaths = $("#filePaths").val(); */
		           	 	$("#filePaths").val(uploadPath);
		            	console.log("filePaths-----"+$("#filePaths").val(uploadPath));
		            }
		        })
				//提交任务模块
				function taskCommit(){
					var commitContent = $('#commitContent').val();
					var imgUrl = $("#filePaths").val();
					var usersTasksId = '${item.id}';
					$.ajax({
						type:'post',
						dataType:'json',
						url:"../fore/commitTask",
						data:{
							'commitContent':commitContent,
							'usersTasksId':usersTasksId,
							'imgUrl':imgUrl
							},
						success:function(result){
							if(result == 0){
								layer.msg("登录已过期，请重新登录")
								return;	
							}
							$(".onload").hide();
							$(".rob").attr("data-type","2");
							$(".rob").text("已提交");
							$(".rob").css("backgroundColor","#666");
							var content = "回复内容："+commitContent;
							$("#showCommitContent").text(content);
						}
					})
				}
	    		/* $.ajax({
	    			type:"post",
	    			dataType: "json",
	    			url:"../fore/submitTask",
	    			data:data,
	    			success:function(result){
	    				console.log("submit result:***"+result)
	    				
	    			},
	    			error: function(){
	    				layer.msg("异常");
	    			}
	    		}) */
				
	    	/* 抢任务模块 */
	    	//根据用户任务完成状态，显示该任务是否可抢、是否可提交
			var tasksStatus = "${item.state}";
			console.log("tasksStatus:"+tasksStatus);
			if(tasksStatus == '1'){
				$(".rob").text("提交任务");
				$(".rob").attr("data-type","1");
			}
			if(tasksStatus == '2'){
				$(".onload").hide();
				$(".rob").attr("data-type","2");
				$(".rob").text("已提交");
				$(".rob").css("backgroundColor","#666");
				var content = "回复内容："+"${item.remarks}";
				$("#showCommitContent").text(content);
			}
			if(tasksStatus == '3'){
				$(".onload").hide();
				$(".rob").attr("data-type","2");
				$(".rob").text("已提交");
				$(".rob").css("backgroundColor","#666");
			}
			if(tasksStatus == '4'){
				$(".onload").hide();
				$(".rob").attr("data-type","2");
				$(".rob").text("已提交");
				$(".rob").css("backgroundColor","#666");
			}
			$(function(){
				$(".rob").click(function(){
					var usersStatus = "${sessionScope.usersStatus}";
					console.log("usersStatus:"+usersStatus);
					if(usersStatus != 1 && usersStatus != "") {
						layer.msg('你还不是会员，请联系客服');
						return;
					}
					if( usersStatus == "") {
						layer.msg('你还不是会员，请联系客服');
						return;
					}
					
					var type=$(".rob").attr("data-type");
					console.log("data-type:"+type);
					//若任务未领取
					if(type === "0"){
						//抢任务，"0"代表任务未领取
						//获取tasksid
						var tasksid = getQueryVariable('tasksid');
						//新增UsersTasksItem
						$.ajax({
							type:'post',
							url:'../fore/addUsersTasksItem',
							data:{
								'tasksid':tasksid,
							},
							success:function(result){
								console.log(result);
								if(result == '1'){
									layer.msg('任务已结束');
									console.log('任务已结束');
									return;
								}
								//抢任务成功，设置按钮为提交任务
								console.log("result:"+result);
								$('#usersTasksId').val(result);
								console.log("抢任务成功");
								layer.msg('抢任务成功');
								$(".rob").text("提交任务");
								$(".rob").attr("data-type","1");
							}
						})
					}
					//设置提交框可见
					if(type === "1"){
						$(".onload").show();
					}
				})
				/* $(".ensure").click(function(){
					console.log("content:"+content);
					$(".onload").hide();
					$(".rob").attr("data-type","2");
					$(".rob").text("已提交");
					$(".rob").css("backgroundColor","#666");
				}) */
			})
			//任务剩余时间
			function aaaa(){
				//从model中获取任务创建时间，类型是java Date()
				var timeLeft = "${tasks.createDate}";
				console.log("timeLeft:"+timeLeft);
				//转换为js的Date()
				var jsTimeLeft = new Date(timeLeft);
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
		        //任务剩余时间为0时，按钮改为已结束
		        if(!(days>=0 && hours>=0 && minutes>=0)){
		        	days = 0;
		        	hours = 0;
		        	minutes = 0;
		        	$(".onload").hide();
					$(".rob").attr("data-type","3");
					$(".rob").text("已结束");
					$(".rob").css("backgroundColor","#666");
		        }
		        //拼接剩余时间并显示到对应的元素上
		        var jsTimeleftValue = "剩余"+days+"天"+hours+"小时"+minutes+"分";
		        console.log("剩余"+days+"天"+hours+"小时"+minutes+"分");
		        $('#jsTimeleftValue').text(jsTimeleftValue);
			}
			
			var a = aaaa();
   			setInterval(aaaa,60000);
		 </script>
	</body>
</html>
