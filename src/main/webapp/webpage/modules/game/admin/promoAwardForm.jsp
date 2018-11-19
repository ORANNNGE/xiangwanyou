<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>推广业绩管理</title>
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
					jp.post("${ctx}/game/admin/promoAward/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="promoAward" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">用户：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/game/admin/users/data" id="users" name="users.id" value="${promoAward.users.id}" labelName="users.realName" labelValue="${promoAward.users.realName}"
							 title="选择用户" cssClass="form-control required" fieldLabels="用户|手机号" fieldKeys="realName|phoneNum" searchLabels="用户|手机号" searchKeys="realName|phoneNum" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">推荐人：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/game/admin/users/data" id="referrer" name="referrer.id" value="${promoAward.referrer.id}" labelName="referrer.realName" labelValue="${promoAward.referrer.realName}"
							 title="选择推荐人" cssClass="form-control required" fieldLabels="用户|手机号" fieldKeys="realName|phoneNum" searchLabels="用户|手机号" searchKeys="realName|phoneNum" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">奖励：</label></td>
					<td class="width-35">
						<form:input path="award" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">奖励类型：</label></td>
					<td class="width-35">
						<form:radiobuttons path="type" items="${fns:getDictList('award_type')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>