<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>栏目管理管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
					form.submit();
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
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/cms/category"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="category" action="${ctx}/cms/category/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>归属机构:</label>
					<div class="col-sm-10">
						<sys:treeselect id="office" name="office.id" value="${category.office.id}" labelName="office.name" labelValue="${category.office.name}"
										title="部门" url="/sys/office/treeData?type=2" cssClass="form-control " allowClear="true" notAllowSelectParent="true"/>

					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>上级栏目：</label>
					<div class="col-sm-10">
						<sys:treeselect id="category"
										name="parent.id" value="${category.parent.id}"
										labelName="parent.name" labelValue="${category.parent.name}"
										title="栏目" url="/cms/category/treeData" extId="${category.id}"
										cssClass="required form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>栏目名称：</label>
					<div class="col-sm-10">
						<form:input path="name"
									htmlEscape="false" maxlength="50" class="required form-control"/>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>栏目模块：</label>
					<div class="col-sm-10">
						<form:select path="module" class="form-control ">
							<form:option value="" label="=请选择="/>
							<form:options items="${fns:getDictList('cms_module')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">栏目图片：</label>
					<div class="col-sm-10">
						<form:hidden id="image" path="image" htmlEscape="false" maxlength="255" class="form-control"/>
						<sys:ckfinder input="image" type="images" uploadPath="/cms/category" selectMultiple="false"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">链接：</label>
					<div class="col-sm-10">
						<form:input path="href" htmlEscape="false" class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">目标：</label>
					<div class="col-sm-10">
						<form:input path="target" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">描述：</label>
					<div class="col-sm-10">
						<form:input path="description" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">关键字：</label>
					<div class="col-sm-10">
						<form:input path="keywords" htmlEscape="false"   class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>排序：</label>
					<div class="col-sm-10">
						<form:input path="sort" htmlEscape="false"  class="form-control number required "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>是否在导航中显示：</label>
					<div class="col-sm-10">
						<form:select path="inMenu" class="form-control ">
							<form:option value="" label="=请选择="/>
							<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>是否在分类页中显示列表：</label>
					<div class="col-sm-10">
						<form:select path="inList" class="form-control ">
							<form:option value="" label="=请选择="/>
							<form:options items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('cms:category:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>