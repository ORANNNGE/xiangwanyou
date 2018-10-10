<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${article.title} - ${category.name}</title>
    <meta name="description" content="${article.description} ${category.description}"/>
    <meta name="keywords" content="${article.keywords} ${category.keywords}"/>
    <link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script src="${ctxStatic}/bootstrap/3.3.4/js/bootstrap.min.js" type="text/javascript"></script>
    <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet"/>
    <%@ include file="/webpage/modules/cms/front/include/meta.jsp" %>
</head>
<body>
<div class="indexBigContainer">
    <%@ include file="/webpage/modules/cms/front/include/head.jsp" %>
    <div class="noticeContainer clearfix">
        <div class="row">
            <div class="span10">
                <h3 style="color:#555555;font-size:20px;text-align:center;border-bottom:1px solid #ddd;padding-bottom:15px;margin:25px 0;">${article.title}</h3>

                <div style="border-bottom:1px solid #ddd;padding:10px;margin:25px 0;">发布者：${article.user.name} &nbsp;
                    点击数：${article.hits} &nbsp; 发布时间：<fmt:formatDate value="${article.createDate}"
                                                                    pattern="yyyy-MM-dd HH:mm:ss"/> &nbsp;
                    更新时间：<fmt:formatDate value="${article.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                <c:if test="${not empty article.description}">
                    <div>摘要：${article.description}</div>
                </c:if>
                <br/>
                <div>${article.content}</div>
                <c:if test="${article.attachUrl!=''}">
                <div>  <a
                            target="_blank" title="附件下载" class="pull-left text-hidden"
                            href="${article.attachUrl}"
                            style="color:blue; padding-left: 10px;">附件下载</a>
                </div>
                </c:if>
            </div>
        </div>
    </div>
    <script>
        $('#index_${category.id}').attr('class', 'on');
    </script>
    <%@ include file="/webpage/modules/cms/front/include/foot.jsp" %>
</div>
</body>
</html>