<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>站点信息管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.loading();
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
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
                        <a class="panelButton" href="${ctx}/cms/site"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="site" action="${ctx}/cms/site/save" method="post"
                               class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>站点名称：</label>
                            <div class="col-sm-10">
                                <form:input path="name" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>站点标题：</label>
                            <div class="col-sm-10">
                                <form:input path="title" htmlEscape="false" class="form-control required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">站点Logo：</label>
                            <div class="col-sm-10">
                                <form:hidden id="logo" path="logo" htmlEscape="false" maxlength="255"
                                             class="form-control"/>
                                <sys:ckfinder input="logo" type="images" uploadPath="/cms/site" selectMultiple="false"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">站点域名：</label>
                            <div class="col-sm-10">
                                <form:input path="domain" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">描述：</label>
                            <div class="col-sm-10">
                                <form:input path="description" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">关键字：</label>
                            <div class="col-sm-10">
                                <form:input path="keywords" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">主题：</label>
                            <div class="col-sm-10">
                                <form:input path="theme" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">版权信息：</label>
                            <div class="col-sm-10">
                                <form:input path="copyright" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>
                        <c:if test="${fns:hasPermission('cms:site:edit') || isAdd}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <button class="btn btn-primary btn-block btn-lg btn-parsley"
                                                data-loading-text="正在提交...">提 交
                                        </button>
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