<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>栏目管理管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="categoryList.js" %>
</head>
<body>

<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">栏目管理列表 </h3>
        </div>

        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left treetable-bar">
                        <shiro:hasPermission name="cms:category:add">
                            <a id="add" class="btn btn-primary" href="${ctx}/cms/category/form" title="栏目管理"><i
                                    class="glyphicon glyphicon-plus"></i> 新建</a>
                        </shiro:hasPermission>
                        <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()"
                                title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新
                        </button>

                    </div>
                </div>
            </div>
            <table id="categoryTreeTable" class="table table-hover">
                <thead>
                <tr>
                    <th>归属机构</th>
                    <th>栏目名称</th>
                    <th>栏目模块</th>
                    <th>关键字</th>
                    <th>排序</th>
                    <th>导航菜单</th>
                    <th>栏目列表</th>
                    <th>更新时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="categoryTreeTableList"></tbody>
            </table>
            <br/>
        </div>
    </div>
</div>
</body>
</html>