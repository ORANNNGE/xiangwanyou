<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户任务管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $table; // 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }

		  return false;
		}

		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/game/admin/usersTasksItem/save",$('#inputForm').serialize(),function(data){
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
			
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="usersTasksItem" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">用户：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/game/admin/users/data" id="users" name="users.id" value="${usersTasksItem.users.id}" labelName="users.phoneNum" labelValue="${usersTasksItem.users.phoneNum}"
							 title="选择用户" cssClass="form-control required" fieldLabels="手机号|姓名" fieldKeys="phoneNum|realName" searchLabels="手机号|姓名" searchKeys="phoneNumphoneNum|realName" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">任务：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/game/admin/tasks/data" id="tasks" name="tasks.id" value="${usersTasksItem.tasks.id}" labelName="tasks.name" labelValue="${usersTasksItem.tasks.name}"
							 title="选择任务" cssClass="form-control required" fieldLabels="任务名|第几期" fieldKeys="name|phase" searchLabels="任务名|第几期" searchKeys="name|phase" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">提交任务备注：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">提交图片：</label></td>
					<td class="width-35">
						<form:hidden id="picture" path="picture" htmlEscape="false" maxlength="255" class="form-control"/>
						<sys:ckfinder input="picture" type="files" uploadPath="/game/admin/usersTasksItem" selectMultiple="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">任务状态：</label></td>
					<td class="width-35">
						<form:radiobuttons path="state" items="${fns:getDictList('tasks_state')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>