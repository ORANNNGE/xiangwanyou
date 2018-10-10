<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="topContainer">
    <div class="erweimaBox"><img alt="二维码" src="/static/web/img/erweima.jpg"/></div>
    <div class="banner"><img style="padding: 0 !important;" alt="${site.title}" src="/static/web/img/banner.jpg"
                             class="container"></div>
    <div class="nav">
        <div class="menu clearfix">
            <ul>
                <em></em>
                <li id="index_0">
                    <a href="/"><span class="menu-ch">网站首页</span><br><span class="menu-en">WangZhanShouYe</span></a>
                </li>
                <c:forEach items="${fnc:getMainNavList(site.id)}" var="category" varStatus="status">
                    <c:if test="${status.index lt 6}">
                        <em></em>
                        <li id="index_${category.id}">
                            <a href="${category.url}" target="${category.target}">
                                <span class="menu-ch">${category.name}</span><br><span
                                    class="menu-en">${category.keywords}</span></a>
                        </li>
                    </c:if></c:forEach>
            </ul>
            <a href="/admin/" target="_blank" class="adminBtn">管理者入口</a>
        </div>
    </div>
</div>
</div>




