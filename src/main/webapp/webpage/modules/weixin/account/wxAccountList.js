<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#wxAccountTable').bootstrapTable({
		 
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
               url: "${ctx}/weixin/wxAccount/data",
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
                   	window.location = "${ctx}/weixin/wxAccount/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该微信账号记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/weixin/wxAccount/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#wxAccountTable').bootstrapTable('refresh');
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
		        checkbox: true
		       
		    }
			,{
		        field: 'name',
		        title: '公众号名称',
		        sortable: true
		    },{
                field: 'account',
                title: '公众号账号',
               sortable: true

              }
		    ,{
               	field: 'qrCode',
				title: '二维码',
                sortable: true
				,formatter:function(value, row , index){
                     return '<a target="_blank" href="http://open.weixin.qq.com/qr/code?username='+row.account+'" title="点击查看原图"><img src="http://open.weixin.qq.com/qr/code?username='+row.account+'" style="width:30px;height:30px"></a>';
                 }
            }
			,{
		        field: 'appid',
		        title: 'appid',
		        sortable: true
		       
		    }
			,{
		        field: 'appsecret',
		        title: 'appsecret',
		        sortable: true
		       
		    }
			,{
		        field: 'url',
		        title: '验证时用的url',
		        sortable: true
		       
		    }
			,{
		        field: 'token',
		        title: 'token',
		        sortable: true
		       
		    }
			,{
		        field: 'msgCount',
		        title: '自动回复条数',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#wxAccountTable').bootstrapTable("toggleView");
		}
	  
	  $('#wxAccountTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#wxAccountTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#wxAccountTable').bootstrapTable('getSelections').length!=1);
          $('#edit1').prop('disabled', $('#wxAccountTable').bootstrapTable('getSelections').length!=1);
        });
		  

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#wxAccountTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#wxAccountTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#wxAccountTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){
		jp.confirm('确认要删除该微信账号记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/weixin/wxAccount/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#wxAccountTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/weixin/wxAccount/form?id=" + getIdSelections();
  }

function select(){
    jp.confirm('确认要切换到当前公众号吗？', function(){
        jp.loading();
        jp.get("${ctx}/weixin/wxAccount/select?id=" + getIdSelections(), function(data){
            if(data.success){
                $('#wxAccountTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })

    })
}
</script>