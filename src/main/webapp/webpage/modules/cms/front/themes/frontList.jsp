<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${category.name}·${site.title}</title>
    <meta name="description" content="${site.description}"/>
    <meta name="keywords" content="${site.keywords}"/>
    <link href="${ctxStatic}/bootstrap/3.3.4/css_default/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script src="${ctxStatic}/bootstrap/3.3.4/js/bootstrap.min.js" type="text/javascript"></script>
    <link href="${ctxStatic}/awesome/4.4/css/font-awesome.min.css" rel="stylesheet"/>
    <%@ include file="/webpage/modules/cms/front/include/meta.jsp" %>
    <style>
        .pagination {
            margin: auto 0px;
        }

        .notice-list ul li {
            float: none;
            width: auto;
        }

        .notice-list ul li a {
            float: none;
            width: auto;
        }
    </style>
</head>
<body>
<div class="indexBigContainer">
    <%@ include file="/webpage/modules/cms/front/include/head.jsp" %>
    <div class="noticeContainer clearfix">
        <!--notice-->
        <div>
            <div class="notice-tt">
                <span>${category.name}</span>
            </div>
            <div class="notice-list clearfix">
                <ul>
                    <c:forEach items="${page.list}" var="article">
                        <li><i></i><span class="pull-right"><fmt:formatDate value="${article.updateDate}"
                                                                            pattern="yyyy-MM-dd"/></span><a
                                target="_blank" title="${article.title}" class="text-hidden" href="${article.url}"
                                style="color:${article.color}">${fns:abbr(article.title,150)}</a>
                            <c:if test="${article.attachUrl!=''}">
                                <a target="_blank" title="附件下载" class="pull-right text-hidden"
                                        href="${article.attachUrl}"
                                        style="color:blue; padding-right: 10px;">附件下载</a>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="pagination">${page}</div>
            <script type="text/javascript">
                function page(n, s) {
                    location = "/list/${category.id}${urlSuffix}?pageNo=" + n + "&pageSize=" + s;
                }
            </script>
        </div>
    </div>
    <script>
        $('#index_${category.id}').attr('class', 'on');
    </script>
    <%@ include file="/webpage/modules/cms/front/include/foot.jsp" %>
</div>
</body>
</html>