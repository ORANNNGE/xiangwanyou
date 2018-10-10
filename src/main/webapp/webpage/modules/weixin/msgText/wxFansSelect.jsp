<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>微信粉丝管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <%@ include file="/webpage/include/anihead.jsp" %>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">微信粉丝列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="wxFans" class="form form-horizontal well clearfix">
                            <div class="col-xs-6 col-sm-6 col-md-4">
                                <label class="label-item single-overflow pull-left" title="微信昵称">微信昵称：</label>
                                <form:input path="nicknameStr" htmlEscape="false" class=" form-control"/>
                            </div>
                            <div class="col-xs-6 col-sm-6 col-md-4">
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
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2"
                   href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="wxFansTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
<script>$(document).ready(function () {
    $('#wxFansTable').bootstrapTable({
        //请求方法
        method: 'get',
        //类型json
        dataType: "json",
        //显示刷新按钮
        showRefresh: true,
        //显示切换手机试图按钮
        showToggle: true,
        //显示 内容列下拉框
        showColumns: true,
        //显示到处按钮
        showExport: true,
        //显示切换分页按钮
        showPaginationSwitch: true,
        //最低显示2行
        minimumCountColumns: 2,
        //是否显示行间隔色
        striped: true,
        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        cache: false,
        //是否显示分页（*）
        pagination: true,
        //排序方式
        sortOrder: "asc",
        //初始化加载第一页，默认第一页
        pageNumber: 1,
        //每页的记录行数（*）
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/weixin/wxFans/data",
        //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
        //queryParamsType:'',
        ////查询参数,每次调用是会带上这个参数，可自定义
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        //分页方式：client客户端分页，server服务端分页（*）
        sidePagination: "server",
        contextMenuTrigger: "right",//pc端 按右键弹出菜单
        contextMenuTriggerMobile: "press",//手机端 弹出菜单，click：单击， press：长按。
        contextMenu: '#context-menu',
        onClickRow: function (row, $el) {
        },
        columns: [{
            <c:if test="${isMultiSelect}">
            checkbox: true
            </c:if>
            <c:if test="${!isMultiSelect}">
            radio: true
            </c:if>
        }, {
            title: '序号',//标题  可不加
            formatter: function (value, row, index) {
                return index + 1;
            }
        }, {
            field: 'openId',
            title: 'openId',
            visible:false

        }
            , {
                field: 'nicknameStr',
                title: '昵称',
                sortable: true

            }
            , {
                field: 'sex',
                title: '性别',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('sex'))}, value, "-");
                }
            }, {
                field: 'headImgUrl',
                title: '头像',
                sortable: true,
                formatter: function (value, row, index) {
                    return "<img class='fans-portrait' width='30px' height='30px' src=" + value + " />";
                }
            }
            , {
                field: 'country',
                title: '国家',
                sortable: true

            }
            , {
                field: 'province',
                title: '省/市',
                sortable: true, formatter: function (value, row, index) {
                    return value + "/" + row.city;
                }
            }
            , {
                field: 'subscribeTime',
                title: '关注时间',
                sortable: true

            }
        ]

    });

    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端
        $('#wxFansTable').bootstrapTable("toggleView");
    }

    $("#search").click("click", function () {// 绑定查询按扭
        $('#wxFansTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#wxFansTable').bootstrapTable('refresh');
    });
});

function getSelections() {
    return $.map($("#wxFansTable").bootstrapTable('getSelections'), function (row) {
        return row;
    });
}
</script>
</body>
</html>