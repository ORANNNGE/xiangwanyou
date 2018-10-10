<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${category.name}·${site.title}</title>
    <meta name="description" content="${site.description}"/>
    <meta name="keywords" content="${site.keywords}"/>
    <%@ include file="/webpage/modules/cms/front/include/meta.jsp" %>
</head>
<body>
<div class="indexBigContainer">
    <%@ include file="/webpage/modules/cms/front/include/head.jsp" %>
    <!--Sponsor-->
    <div class="SponsorContainer">
        <div class="SponsorContainer-hd hd">
            <ul>
                <c:forEach items="${categoryList}" var="tpl">
                    <li>
                        <a href="javascript:void(0)">${tpl.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="SponsorContainer-bd bd">
            <!--赛事合作伙伴-->

            <c:forEach items="${categoryList}" var="tpl">
            <ul>
                <li>
                    <div class="SponsorList">
                        <ul>
                            <c:forEach items="${fnc:getArticleList(site.id,tpl.id, 200, '')}" var="article"
                                       varStatus="status">
                            <c:if test="${status.index%6==0 && status.index ne 0}">
                        </ul>
                        <ul>
                            </c:if>
                            <li><img src="${article.image}"/></li>
                            </c:forEach>
                        </ul>
                    </div>
                </li>
            </ul>
            </c:forEach>
        </div>
    </div>
    <script>
        $(".SponsorContainer").slide({});
        $('#index_${category.id}').attr('class', 'on');
    </script>
    <%@ include file="/webpage/modules/cms/front/include/foot.jsp" %>
</div>
</body>
</html>