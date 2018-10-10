<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#wxMsgTextTable').bootstrapTable({

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
               url: "${ctx}/weixin/wxMsgText/data",
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
                   	window.location = "${ctx}/weixin/wxMsgText/form?id=" + row.id;
                   }  else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该文本消息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/weixin/wxMsgText/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#wxMsgTextTable').bootstrapTable('refresh');
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

		    },{
                   title: '序号',//标题  可不加
                   formatter: function (value, row, index) {
                       return index+1;
                   }
               },{
                   field: 'wxMsgBase.inputCode',
                   title: '关键字',
                   sortable: true

               }
			,{
		        field: 'title',
		        title: '标题',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/weixin/wxMsgText/form?id="+row.id+"'>"+value+"</a>";
		         }
		    }
			,{
		        field: 'content',
		        title: '内容',
		        sortable: true

		    }

			,{
		        field: 'createDate',
		        title: '创建时间',
		        sortable: true

		    },{
                       field: 'operate',
                       title: '操作',
                       align: 'center',
                       formatter: function operateFormatter(value, row, index) {
                           return '<a href="javascript:select('+row.id+')" class="edit" title="群发" ><i class="fa fa-share"></i>消息群发</a>';
                       }
               }
		     ]

		});

	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


		  $('#wxMsgTextTable').bootstrapTable("toggleView");
		}

	  $('#wxMsgTextTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#wxMsgTextTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#wxMsgTextTable').bootstrapTable('getSelections').length!=1);
        });



	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#wxMsgTextTable').bootstrapTable('refresh');
		});

	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#wxMsgTextTable').bootstrapTable('refresh');
		});


	});

  function getIdSelections() {
        return $.map($("#wxMsgTextTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

  function deleteAll(){
		jp.confirm('确认要删除该文本消息记录吗？', function(){
			jp.loading();
			jp.get("${ctx}/weixin/wxMsgText/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#wxMsgTextTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})

		})
  }
  function edit(){
	  window.location = "${ctx}/weixin/wxMsgText/form?id=" + getIdSelections();
  }

  function select(wxMsgTextId) {
      jp.open({
          type: 2,
          area: ['1200px', '700px'],
          title:"选择粉丝",
          auto:true,
          maxmin: true, //开启最大化最小化按钮
          content: ctx+"/weixin/wxMsgText/select?isMultiSelect=true",
          btn: ['确定', '关闭'],
          yes: function(index, layero){
              var rows = layero.find("iframe")[0].contentWindow.getSelections();
              if(rows.length ==0){
                  jp.warning("请选择至少一个粉丝!");
                  return;
              }
              var openIds = [];
              for(var i=0; i<rows.length; i++){
                  openIds.push(rows[i].openId);
              }
              jp.loading();
              $.ajax({
                  url: ctx+'/weixin/wxMsgText/massSendTextByOpenIds',
                  data: {
                      textId:wxMsgTextId,
                      openIds: openIds.join(",")
                  },type:'post',
                  success: function (result) {
                      if (result.success) {
                          jp.success(result.msg);
                          top.layer.close(index);
                      } else {
                          jp.success(result.msg);
                      }
                  }
              })
          },
          cancel: function(index){
              //取消默认为空，如需要请自行扩展。
              top.layer.close(index);
          }
      });
  }
</script>