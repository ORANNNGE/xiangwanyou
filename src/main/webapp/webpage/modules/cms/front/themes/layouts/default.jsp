<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/modules/cms/front/include/taglib.jsp" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html>
<head>
    <title><sitemesh:title default="欢迎访问"/> - ${site.title}</title>
    <%@ include file="/webpage/modules/cms/front/include/meta.jsp" %>
    <sitemesh:head/>
</head>
<body>
<div class="indexBigContainer">

    <sitemesh:body/>
    <div class="foot">
        <p>Copyright@2015-2020 ${site.copyright}</p>
        <p>沪ICP备16014509号-5</p>
    </div>
</div>
</body>
</html>