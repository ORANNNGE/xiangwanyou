<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#withdrawTable').bootstrapTable({
		 
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
               url: "${ctx}/game/admin/withdraw/data",
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
                   	edit(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该用户提现记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/game/admin/withdraw/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#withdrawTable').bootstrapTable('refresh');
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
		        field: 'users.phoneNum',
		        title: '用户',
		        sortable: true
		        ,formatter:function(value, row , index){
 			    if(value == null){
		            	return "<a href='javascript:edit(\""+row.id+"\")'>-</a>";
		            }else{
		                return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
		            }
		        }
		       
		    }

			,{
			   field: 'users.realName',
			   title: '姓名',
			   sortable: true
		   	}

			,{
			   field: 'users.alipayAccount',
			   title: '支付宝',
			   sortable: true
		   	}
			,{
		        field: 'sum',
		        title: '提现金额',
		        sortable: true
		    }
			,{
		        field: 'state',
		        title: '提现状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('withdraw_state'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'createDate',
		        title: '创建时间',
		        sortable: true
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#withdrawTable').bootstrapTable("toggleView");
		}
	  
	  $('#withdrawTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#withdrawTable').bootstrapTable('getSelections').length);
          $('#updateAll').prop('disabled', ! $('#withdrawTable').bootstrapTable('getSelections').length);
          $('#sumAll').prop('disabled', ! $('#withdrawTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#withdrawTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/game/admin/withdraw/import/template';
				  },
			    btn2: function(index, layero){
				        var inputForm =top.$("#importForm");
				        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				        inputForm.onsubmit = function(){
				        	jp.loading('  正在导入，请稍等...');
				        }
				        inputForm.submit();
					    jp.close(index);
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#withdrawTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#withdrawTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#withdrawTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function deleteAll(){

		jp.confirm('确认要删除该用户提现记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/game/admin/withdraw/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#withdrawTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  	//批量更新提现状态
	function updateAll(){

		jp.confirm('确认要提现吗？', function(){
			jp.loading();
			jp.get("${ctx}/game/admin/withdraw/updateAll?ids=" + getIdSelections(), function(data){
				if(data.success){
					$('#withdrawTable').bootstrapTable('refresh');
					jp.success(data.msg);
				}else{
					jp.error(data.msg);
				}
			})

		})
	}
	//计算提现金额
	function sumAll(){
			// jp.confirm('确认要计算所选用户提现金额吗？', function(){
				jp.loading();
				jp.get("${ctx}/game/admin/withdraw/sumAll?ids=" + getIdSelections(), function(data){
					if(data.success){
						// $('#withdrawTable').bootstrapTable('refresh');
						jp.alert(data.msg);
					}else{
						jp.error(data.msg);
					}
				})
			// })
		}

   function add(){
	  jp.openDialog('新增用户提现', "${ctx}/game/admin/withdraw/form",'800px', '500px', $('#withdrawTable'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	   <shiro:hasPermission name="game:admin:withdraw:edit">
	  jp.openDialog('编辑用户提现', "${ctx}/game/admin/withdraw/form?id=" + id,'800px', '500px', $('#withdrawTable'));
	   </shiro:hasPermission>
	  <shiro:lacksPermission name="game:admin:withdraw:edit">
	  jp.openDialogView('查看用户提现', "${ctx}/game/admin/withdraw/form?id=" + id,'800px', '500px', $('#withdrawTable'));
	  </shiro:lacksPermission>
  }

</script>