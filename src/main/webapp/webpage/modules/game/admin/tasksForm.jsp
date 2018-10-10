<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>任务管理</title>
	<meta name="decorator" content="ani"/>
	<!-- SUMMERNOTE -->
	<%@include file="/webpage/include/summernote.jsp" %>
	<script type="text/javascript">
		var validateForm;
		var $table; // 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  jp.loading();
					$("input[name='details']").val($('#details').summernote('code'));//取富文本的值
			  $("#inputForm").submit();
			  return true;
		  }

		  return false;
		}

		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/game/admin/tasks/save",$('#inputForm').serialize(),function(data){
						if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    	jp.close($topIndex);//关闭dialog

	                    }else{
            	  			jp.error(data.msg);
	                    }
					})
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
				//富文本初始化
			$('#details').summernote({
				height: 300,
                lang: 'zh-CN'
            });
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="tasks" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">任务名：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">任务编号：</label></td>
					<td class="width-35">
						<form:input path="code" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">第几期：</label></td>
					<td class="width-35">
						<form:input path="phase" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">任务详情：</label></td>
					<td class="width-35">
                        <input type="hidden" name="details"/>
						<div id="details">
                          ${fns:unescapeHtml(tasks.details)}
                        </div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">任务图标：</label></td>
					<td class="width-35">
						<form:hidden id="icon" path="icon" htmlEscape="false" maxlength="255" class="form-control"/>
						<sys:ckfinder input="icon" type="files" uploadPath="/game/admin/tasks" selectMultiple="true"/>
					</td>
					<td class="width-15 active"><label class="pull-right">下载链接：</label></td>
					<td class="width-35">
						<form:input path="download" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">可领任务总数量：</label></td>
					<td class="width-35">
						<form:input path="numTotal" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">剩余任务数量：</label></td>
					<td class="width-35">
						<form:input path="numRemain" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">奖励金：</label></td>
					<td class="width-35">
						<form:input path="reward" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>