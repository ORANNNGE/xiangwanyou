<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    $.getJSON("${ctx}/cms/category/bootstrapTreeData",function(data){
        $('#jstree').treeview({
            data: data,
            levels:2,
            onNodeSelected: function(event, treeNode) {
                var id = treeNode.id == '0' ? '' :treeNode.id;
                    $("#categoryId").val(id);
                    $("#categoryName").val(treeNode.text);
                $('#table').bootstrapTable('refresh');
            },
        });
    });
    $('#table').bootstrapTable({
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
        pageNumber:1,
        //每页的记录行数（*）
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/cms/article/data",
        //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
        //queryParamsType:'',
        ////查询参数,每次调用是会带上这个参数，可自定义
        queryParams : function(params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
            searchParam.pageSize = params.limit === undefined? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
            return searchParam;
        },
        //分页方式：client客户端分页，server服务端分页（*）
        sidePagination: "server",
        contextMenuTrigger:"right",//pc端 按右键弹出菜单
        contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
        contextMenu: '#context-menu',
        onContextMenuItem: function(row, $el){
            if($el.data("item") == "edit"){
                window.location = "${ctx}/cms/article/form?id=" + row.id;
            } else if($el.data("item") == "delete"){
                jp.confirm('确认要删除该文章吗？', function(){
                    jp.loading();
                    jp.get("${ctx}/cms/article/delete?id="+row.id, function(data){
                        if(data.success){
                            $('#table').bootstrapTable('refresh');
                            jp.success(data.msg);
                        }else{
                            jp.error(data.msg);
                        }
                    })

                });

            }
        },

        onClickRow: function(row, $el){
        },
        columns: [{
            title: '序号',//标题  可不加
            formatter: function (value, row, index) {
                return index+1;
            }

        }
            ,{
                field: 'title',
                title: '标题',
                sortable: true

            }
            ,{
                field: 'keywords',
                title: '关键字',
                sortable: true

            }
            ,{
                field: 'stick',
                title: '置顶',
                sortable: true,
                formatter:function(value, row , index){
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-");
                }

            }
            ,{
                field: 'hits',
                title: '点击数',
                sortable: true

            }
            ,{
                field: 'tags',
                title: '标签',
                sortable: true

            }
            ,{
                field: 'status',
                title: '文章状态',
                sortable: true,
                formatter:function(value, row , index){
                    return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('cms_status'))}, value, "-")+'</font>';
                }

            }
            ,{
                field: 'updateDate',
                title: '更新时间',
                sortable: true

            },{
                field: 'operate',
                title: '操作',
                align: 'center',
                formatter: function operateFormatter(value, row, index) {
                    var status=row.status;

                    var str='<shiro:hasPermission name="cms:article:view">';
                    str+= '<a href="'+row.url+'" target="_blank" class="view" title="预览" ><i class="fa fa-eye"></i>查看 </a>';
                    str+= '</shiro:hasPermission>';
                    if(status !=2 && status !=3){
                        str+='<shiro:hasPermission name="cms:article:audit">';
                        str+= '<a href="javascript:audit(\''+row.id+'\')" class="audit" title="审核" ><i class="fa fa-paper-plane"></i>审核</a>';
                        str+= '</shiro:hasPermission>';


                        str+='<shiro:hasPermission name="cms:article:edit">';
                        str+='<a href="${ctx}/cms/article/form?id='+row.id+'" class="edit" title="修改"><i class="fa fa-edit"></i> 修改</a>';
                        str+='</shiro:hasPermission>';



                        str+= '<shiro:hasPermission name="cms:article:del">';
                        str+= '<a href="javascript:del(\''+row.id+'\')" class="del" title="删除"><i class="fa fa-trash"></i>删除</a>';
                        str+= '</shiro:hasPermission>';
                    }
                    if(status==2){
                        str+= '<shiro:hasPermission name="cms:article:publish">';
                        str+= '<a href="javascript:publish(\''+row.id+'\')" class="publish" title="发布"><i class="fa fa-paper-plane"></i>发布</a>';
                        str+= '</shiro:hasPermission>';
                    }
                    if(status==3){
                        str+= '<shiro:hasPermission name="cms:article:edit">';
                        str+= '<a href="javascript:cancel(\''+row.id+'\')" class="cancel" title="撤销"><i class="fa fa-trash"></i>撤销</a>';
                        str+= '</shiro:hasPermission>';
                    }
                    str+= '<shiro:hasPermission name="cms:cmsLog:view">';
                    str+= '<a href="javascript:jp.openTab(\'${ctx}/cms/cmsLog/articleList?articleId='+row.id+'\',\'【'+row.title+'】查看日志记录\',\'true\')" class="log" title="操作日志"><i class="fa fa-search"></i>操作日志</a>';
                    str+= '</shiro:hasPermission>';
                    return str;
                }
            }
        ]

    });

    if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

        $('#table').bootstrapTable("toggleView");
    }

    $("#search").click("click", function() {// 绑定查询按扭
        $('#table').bootstrapTable('refresh');
    });

    $("#reset").click("click", function() {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#table').bootstrapTable('refresh');
    });


});
function audit(id){
    if(!id){
        id = getIdSelections();
    }
    jp.openDialog('审核', "${ctx}/cms/article/auditForm?id=" + id,'300px', '200px',$('#table'));

}
  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function edit(){
	  window.location = "${ctx}/cms/article/form?id=" + getIdSelections();
  }
function publish(id){
    jp.confirm('确认要发布该文章吗？', function(){
        jp.loading();
        jp.get("${ctx}/cms/article/publish?id="+id, function(data){
            if(data.success){
                $('#table').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    });
}
function cancel(id){
    jp.confirm('确认要撤销发布吗？', function(){
        jp.loading();
        jp.get("${ctx}/cms/article/backout?id="+id, function(data){
            if(data.success){
                $('#table').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    });
}



function del(id){
    jp.confirm('确认要删除该文章吗？', function(){
        jp.loading();
        jp.get("${ctx}/cms/article/delete?id="+id, function(data){
            if(data.success){
                $('#table').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    });
}
</script>