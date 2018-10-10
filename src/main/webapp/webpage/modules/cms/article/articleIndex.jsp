<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>文章管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <link href="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.css" rel="stylesheet" type="text/css"/>
    <script src="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.js" type="text/javascript"></script>
    <%@include file="articleIndex.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">文章管理列表</h3>
        </div>

        <div class="panel-body">
            <sys:message content="${message}"/>
            <div class="row">
                <div class="col-sm-3 col-md-2">
                    <div id="jstree"></div>
                </div>
                <div class="col-sm-9 col-md-10 animated fadeInRight">
                    <!-- 搜索 -->
                    <div class="accordion-group">
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <form:form id="searchForm" modelAttribute="article"
                                           class="form form-horizontal well clearfix">
                                    <div class="col-xs-12 col-sm-6 col-md-4">
                                        <label class="label-item single-overflow pull-left" title="栏目编号：">栏目名称：</label>
                                        <sys:treeselect id="category"
                                                        name="category.id" value="${article.category.id}"
                                                        labelName="category.name" labelValue="${article.category.name}"
                                                        title="栏目" url="/cms/category/treeData"
                                                        extId="${article.category.id}"
                                                        cssClass="required form-control"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 col-md-4">
                                        <label class="label-item single-overflow pull-left" title="标题：">标题：</label>
                                        <form:input path="title" htmlEscape="false" maxlength="255"
                                                    class=" form-control"/>
                                    </div>
                                    <div class="col-xs-12 col-sm-6 col-md-4">
                                        <div style="margin-top:26px">
                                            <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                                    class="fa fa-search"></i> 查询</a>
                                            <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
                                                    class="fa fa-refresh"></i> 重置</a>
                                        </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                    <!-- 工具栏 -->
                    <div id="toolbar">
                        <shiro:hasPermission name="cms:article:add">
                            <a id="add" class="btn btn-primary" href="${ctx}/cms/article/form" title="文章"><i
                                    class="glyphicon glyphicon-plus"></i> 新建</a>
                        </shiro:hasPermission>
                        <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
                           href="#collapseTwo">
                            <i class="fa fa-search"></i> 检索
                        </a>
                    </div>

                    <!-- 表格 -->
                    <!-- 表格 -->
                    <table id="table"
                           data-toolbar="#toolbar"
                           data-show-refresh="true"
                           data-show-toggle="true"
                           data-show-columns="true"
                           data-show-export="true"
                           data-minimum-count-columns="2"
                           data-id-field="id">
                    </table>
                    <!-- context menu -->
                    <ul id="context-menu" class="dropdown-menu">
                        <li data-item="look"><a>预览</a></li>
                        <li data-item="action1"><a>取消</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>