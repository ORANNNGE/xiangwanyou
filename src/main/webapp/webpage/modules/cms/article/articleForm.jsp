<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>文章管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctxStatic}/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript">

        $(document).ready(function () {
            $("#title").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    var docContent = UE.getEditor('editor').getContent();
                    $('#content').val(docContent);
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
        function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            var docContent = UE.getEditor('editor').getContent();
            $.ajax({
                url: ctx + '/cms/sensitive/changeList',
                data: {
                    docContent: docContent
                },
                async: false,
                type: 'post',
                success: function (result) {
                    if (!result.success) {
                        alert(result.msg);
                        return false;
                    }
                }
            })
            $("#inputForm").validate({
                submitHandler: function (form) {
                    $('#content').val(docContent);
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
        }
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a class="panelButton" href="${ctx}/cms/article/index"><i class="ti-angle-left"></i> 返回</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <form:form id="inputForm" modelAttribute="article" action="${ctx}/cms/article/save" method="post"
                               class="form-horizontal">
                        <form:hidden path="id"/>
                        <sys:message content="${message}"/>

                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>标题：</label>
                            <div class="col-sm-10">
                                <form:input path="title" class="form-control required"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">颜色：</label>
                            <div class="col-sm-10">
                                <form:select path="color" class="form-control ">
                                    <form:option value="" label="默认"/>
                                    <form:options items="${fns:getDictList('color')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"></form:options>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label"><font color="red">*</font>所属栏目：</label>
                            <div class="col-sm-10">
                                <sys:treeselect id="category"
                                                name="category.id" value="${article.category.id}"
                                                labelName="category.name" labelValue="${article.category.name}"
                                                title="栏目" url="/cms/category/treeData"
                                                cssClass="required form-control"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">缩略图：</label>
                            <div class="col-sm-10">
                                <form:hidden id="image" path="image" htmlEscape="false" maxlength="255"
                                             class="form-control"/>
                                <sys:ckfinder input="image" type="images" uploadPath="/cms/article"
                                              selectMultiple="false"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">关键字：</label>
                            <div class="col-sm-4">
                                <form:input path="keywords" htmlEscape="false" class="form-control "/>
                            </div>
                            <label class="col-sm-2 control-label">外部链接：</label>
                            <div class="col-sm-4"><form:input path="link"
                                                              htmlEscape="false" maxlength="200"
                                                              class=" form-control"/>
                                <span
                                        class="help-inline">填写http开头的完整URL地址</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">文章摘要：</label>
                            <div class="col-sm-10">
                                <form:textarea
                                        path="description" htmlEscape="false" rows="4" maxlength="200"
                                        class=" form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">来源：</label>
                            <div class="col-sm-10">
                                <form:input path="copyfrom" htmlEscape="false" class="form-control "/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">是否置顶：</label>
                            <div class="col-sm-4">
                                <form:select path="stick" class="form-control ">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('yes_no')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                            <label class="col-sm-2 control-label">标签：</label>
                            <div class="col-sm-4">
                                <form:select path="tags" class="form-control">
                                    <form:options items="${fns:getDictList('cms_label')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"></form:options>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">内容：</label>
                            <div class="col-sm-10">
                                <form:hidden path="content" htmlEscape="true"/>
                                   <script id="editor" type="text/plain" style="width:100%;height:500px;"></script>
                            </div>
                        </div>
                        <c:if test="${fns:hasPermission('cms:article:edit') || isAdd}">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <div class="form-group text-center">
                                    <div>
                                        <button onclick="doSubmit()" class="btn btn-primary btn-block btn-lg btn-parsley"
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
<script>
    var ue = UE.getEditor('editor');
    $(function () {
        var content = $('#content').val();
        //判断ueditor 编辑器是否创建成功
        ue.addListener("ready", function () {
            // editor准备好之后才可以使用
            ue.setContent(content);

        });
    });
</script>
</body>
</html>