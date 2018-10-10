<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>微信账号管理</title>
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
                        <a class="panelButton" href="${ctx}/weixin/wxAccount"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="col-sm-8">
                        <form:form id="inputForm" modelAttribute="wxAccount" action="${ctx}/weixin/wxAccount/save"
                                   method="post" class="form-horizontal">
                            <form:hidden path="id"/>
                            <sys:message content="${message}"/>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>公众号名称：</label>
                                <div class="col-sm-10">
                                    <form:input path="name" htmlEscape="false" class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>公众号原始ID：</label>
                                <div class="col-sm-10">
                                    <form:input path="account" htmlEscape="false" class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">URL：</label>
                                <div class="col-sm-10">
                                    <form:input path="url" htmlEscape="false" readonly="true" class="form-control "/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Token：</label>
                                <div class="col-sm-10">
                                    <form:input path="token" htmlEscape="false" readonly="true" class="form-control "/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>AppID：</label>
                                <div class="col-sm-10">
                                    <form:input path="appid" htmlEscape="false" class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>AppSecret：</label>
                                <div class="col-sm-10">
                                    <form:input path="appsecret" htmlEscape="false" class="form-control required"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label"><font color="red">*</font>消息条数：</label>
                                <div class="col-sm-10">
                                    <form:input path="msgCount" htmlEscape="false"
                                                class="form-control required digits"/>
                                </div>
                            </div>
                            <c:if test="${fns:hasPermission('weixin:wxAccount:edit') || isAdd}">
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
                    <div class="col-sm-4">
                        <div class="col-sm-12">
                            <img id="url_img" style="width: 100%;max-width:200px;margin-top: 50px;">
                            <p style="margin:20px 0 0 20px;">公众号二维码</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $("#url_img").attr("src", "http://open.weixin.qq.com/qr/code?username=${wxAccount.account}");
</script>
</body>
</html>