<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#add').click(function () {
        jp.confirm('确认要同步微信粉丝吗？', function(){
            jp.loading();
            jp.get("${ctx}/weixin/wxFans/FansList", function(data){
                if(data.success){
                    $('#wxFansTable').bootstrapTable('refresh');
                    jp.success(data.msg);
                }else{
                    jp.error(data.msg);
                }
            })
        });
    });
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
               pageNumber:1,
               //每页的记录行数（*）
               pageSize: 10,
               //可供选择的每页的行数（*）
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
               url: "${ctx}/weixin/wxFans/data",
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
                   	window.location = "${ctx}/weixin/wxFans/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该微信粉丝记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/weixin/wxFans/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#wxFansTable').bootstrapTable('refresh');
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
		        field: 'nicknameStr',
		        title: '昵称',
		        sortable: true

		    }
			,{
		        field: 'sex',
		        title: '性别',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sex'))}, value, "-");
		        }
		    } ,{
                       field: 'headImgUrl',
                       title: '头像',
                       sortable: true,
					   formatter:function(value, row , index){
                           return "<img class='fans-portrait' width='30px' height='30px' src=" + value + " />";
                       }

              }

			,{
		        field: 'country',
		        title: '国家',
		        sortable: true

		    }
			,{
		        field: 'province',
		        title: '省/市',
		        sortable: true, formatter:function(value, row , index){
                   return value+"/"+row.city;
                 }

		    }
			,{
		        field: 'subscribeTime',
		        title: '关注时间',
		        sortable: true

		    }
		     ]

		});


	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端
		  $('#wxFansTable').bootstrapTable("toggleView");
		}

	  $('#wxFansTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#wxFansTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#wxFansTable').bootstrapTable('getSelections').length!=1);
        });



	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#wxFansTable').bootstrapTable('refresh');
		});

	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#wxFansTable').bootstrapTable('refresh');
		});


	});

  function getIdSelections() {
        return $.map($("#wxFansTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
}
</script>