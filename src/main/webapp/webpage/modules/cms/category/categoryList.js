<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var categoryTreeTable=null;
		$(document).ready(function() {
			categoryTreeTable=$('#categoryTreeTable').treeTable({
		    	   theme:'vsStyle',
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/cms/category/getChildren?parentId=',
		            callback:function(item) {
                        item.module =  jp.getDictLabel(${fns:toJson(fns:getDictList('cms_module'))}, item.module);//字典转换
                        item.inMenu =  jp.getDictLabel(${fns:toJson(fns:getDictList('show_hide'))}, item.inMenu);//字典转换
                        item.inList =  jp.getDictLabel(${fns:toJson(fns:getDictList('show_hide'))}, item.inList);//字典转换
		            	 var treeTableTpl= $("#categoryTreeTableTpl").html();
		            	 var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;
		            },
		            beforeClick: function(categoryTreeTable, id) {
		                //异步获取数据 这里模拟替换处理
		                categoryTreeTable.refreshPoint(id);
		            },
		            beforeExpand : function(categoryTreeTable, id) {
		            },
		            afterExpand : function(categoryTreeTable, id) {
		            },
		            beforeClose : function(categoryTreeTable, id) {

		            }
		        });

		        categoryTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点

		});

		function del(con,id){
			jp.confirm('确认要删除栏目吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/cms/category/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			categoryTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})

       		});

		}

		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			categoryTreeTable.refresh();
			jp.close(index);
		}
</script>
<script type="text/html" id="categoryTreeTableTpl">
			<td><a  href="${ctx}/cms/category/form?id={{d.row.id}}">
				{{d.row.office.name === undefined ? "": d.row.office.name}}
			</a></td>
            <td>
            {{d.row.name === undefined ? "": d.row.name}}
            </td>
			<td>
				{{d.row.module === undefined ? "": d.row.module}}
			</td>

			<td>
				{{d.row.keywords === undefined ? "": d.row.keywords}}
			</td>
			<td>
				{{d.row.sort === undefined ? "": d.row.sort}}
			</td>
			<td>
                {{d.row.inMenu === undefined ? "": d.row.inMenu}}
			</td>
			<td>
                  {{d.row.inList === undefined ? "": d.row.inList}}
			</td>
			<td>
				{{d.row.updateDate === undefined ? "": d.row.updateDate}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="cms:category:view">
						<li><a href="${ctx}/cms/category/form?id={{d.row.id}}"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="cms:category:edit">
		   				<li><a href="${ctx}/cms/category/form?id={{d.row.id}}"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="cms:category:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="cms:category:add">
						<li><a href="${ctx}/cms/category/form?parent.id={{d.row.id}}"><i class="fa fa-plus"></i> 添加下级栏目管理</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>