<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/modules/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Toteny软件研发中心</title>
    <meta name="description" content="Toteny软件研发中心"/>
    <meta name="keywords" content="Toteny软件研发中心"/>
    <%@ include file="/webpage/modules/cms/front/include/meta.jsp" %>
</head>
<body>
<div class="indexBigContainer">
    <%@ include file="/webpage/modules/cms/front/include/head.jsp" %>
    <div class="noticeContainer clearfix">
        <!--notice-->
        <div class="notice">
            <div class="notice-tt">
                <span>通知公告</span>
                <a href="/list/2${urlSuffix}" target="_blank" title="更多+">更多+</a>
            </div>
            <div class="notice-list clearfix">
                <ul>
                    <c:forEach items="${fnc:getArticleList(site.id, 2, 6, '')}" var="article">
                        <li><i></i>
                            <a href="${article.url}" target="_blank" title="${article.title}"
                               class="text-hidden" style="color:${article.color}">${fns:abbr(article.title,70)}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!--download-->
        <div class="notice notice-r">
            <div class="notice-tt">
                <span>资料下载</span>
                <a href="/list/5${urlSuffix}" target="_blank" title="更多+">更多+</a>
            </div>
            <div class="notice-list clearfix">
                <ul>
                    <c:forEach items="${fnc:getArticleList(site.id, 5, 6, '')}" var="article">
                        <li><i></i>
                            <a href="${article.url}" target="_blank" title="${article.title}"
                               class="text-hidden" style="color:${article.color}">${fns:abbr(article.title,70)}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>

    <!--轮播图-->
    <div class="jdtContainer">
        <div class="jdtDetails">
            <!--bd-->
            <div class="jdt-bd bd">
                <ul>
                    <c:forEach items="${fnc:getArticleList(site.id, 1520567433624, 6, '')}" var="article">
                        <li>
                            <a href="${article.url}" target="_blank"> <img src="${article.image}"/></a>
                            <p>${article.title}</p>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <!--hd-->
            <div class="jdt-hd hd">
                <ul>
                    <c:forEach items="${fnc:getArticleList(site.id, 1520567433624, 6, '')}" var="article">
                        <li><a href="${article.url}" target="_blank"></a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <!--prev-->
            <div class="pnbtn prev">
                <span class="arrow"><em class="iconfont icon-xiangzuo "></em></span>
            </div>
            <!--next-->
            <div class="pnbtn next">
                <span class="arrow"><em class="iconfont icon-xiangyou2 "></em></span>
            </div>
        </div>
        <script type="text/javascript">
            $(".jdtDetails .bd li").first().before(jQuery(".jdtDetails .bd li").last());
            $(".jdtDetails .pnbtn").hover(function () {
                $(this).find(".arrow").show()
            }, function () {
                $(this).find(".arrow").hide()
            });
            $(".jdtDetails").slide({
                mainCell: ".bd ul",
                effect: "leftLoop",
                autoPlay: true,
                vis: 3
            });
        </script>
    </div>

    <!--search-->
    <div class="searchContainer clearfix">
        <div class="inputBox">
            <form:form id="inputForm" action="/list/matchList.shtml" method="post">
                <input type="text" name="title" id="title" placeholder="请输入赛事名称"/>
                <a href="javascript:doSubmit()" class="iconfont icon-sousuo"></a>
            </form:form>
        </div>
        <a href="/list/matchList.shtml" class="moreBtn">更多</a>
    </div>

    <!--matchlist-->
    <div class="matchlistContainer">
        <ul>
            <c:forEach items="${matchList}" var="list">
                <li>
                    <div class="matchDetails">
                        <div class="matchImg">
                            <a href="/list/matchLook?id=${list.id}"><img src="${list.pic}"/></a>
                        </div>
                        <div class="matchTT">
                            <a href="/list/matchLook?id=${list.id}"
                               title="${list.title} ">${fns:abbr(list.title,30)} </a>
                        </div>
                        <p class="matchPosition">${list.actAddr}</p>
                        <div class="age-price clearfix">
                            <span>${list.age}-${list.age2}岁</span>
                            <em>￥<fmt:formatNumber
                                    value="${list.price}"
                                    pattern="#,##0.00#"></fmt:formatNumber></em>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <!--calendar-->


    <!--matchVideo-->
    <div class="matchVideoContainer clearfix">
        <!--list-->
        <div class="notice matchVideo">
            <div class="notice-tt">
                <span>赛事视频</span>
            </div>
            <div class="notice-list matchVideolist clearfix">
                <ul>
                    <c:forEach items="${fnc:getArticleList(site.id, 6, 6, '')}" var="article">
                        <li id="li_${article.id}" onclick="getVideo('${article.id}')"><i></i>
                            <a href="javascript:void(0)" id="${article.id}" attachUrl="${article.attachUrl}"
                               image="${article.image}"
                               title="${article.title}"
                               class="text-hidden" style="color:${article.color}">${fns:abbr(article.title,70)}</a><span
                                    class="iconfont icon-shipinbofangyingpian"></span></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!--video-->
        <div class="videoBox">
            <video id="videoMp4" src="" controls="controls" width="524" height="295" preload="auto" poster="">
            </video>
        </div>
    </div>
    <script type="text/javascript" src="${ctxStatic}/web/js/swfobject.js"></script>
    <script type="text/javascript">
        function getVideo(id) {
            $('.notice-list li').attr('class', '');
            $('#li_' + id).attr('class', 'on');
            var attachUrl = $('#' + id).attr('attachUrl');
            var image = $('#' + id).attr('image');
            $('#videoMp4').attr('src', attachUrl);
            $('#videoMp4').attr('poster', image);
        }
    </script>
    <!--Sponsor-->
    <div class="SponsorContainer">
        <div class="SponsorContainer-hd hd">
            <ul>
                <li class="on">
                    <a href="javascript:void(0)">赛事合作伙伴</a>
                </li>
                <li>
                    <a href="javascript:void(0)">赛事支持单位</a>
                </li>
                <li>
                    <a href="javascript:void(0)">赞助商</a>
                </li>
            </ul>
        </div>
        <div class="SponsorContainer-bd bd">
            <!--赛事合作伙伴-->
            <ul>
                <li>
                    <div class="SponsorList">
                        <ul>
                            <c:forEach items="${fnc:getArticleList(site.id,8, 200, '')}" var="article"
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

            <!--赛事支持单位-->
            <ul>
                <li>
                    <div class="SponsorList">
                        <ul>
                            <c:forEach items="${fnc:getArticleList(site.id,8, 200, '')}" var="article"
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

            <!--赞助商-->
            <ul>
                <li>
                    <div class="SponsorList">
                        <ul>
                            <c:forEach items="${fnc:getArticleList(site.id,8, 200, '')}" var="article"
                                       varStatus="status">
                            <c:if test="${status.index%6==0 && status.index ne 0}">
                        </ul>
                        <ul>
                            </c:if>
                            <li><img src="${article.image}" title="${article.title}"/></li>
                            </c:forEach>
                        </ul>
                    </div>
                </li>
            </ul>

        </div>
    </div>
    <script type="text/javascript">
        $(".SponsorContainer").slide({});
        $('#index_0').attr('class', 'on');

        function doSubmit() {
            var title = $('#title').val();
            if (title != '') {
                $("#inputForm").submit();
                return true;
            }
            return false;
        }
    </script>
    <%@ include file="/webpage/modules/cms/front/include/foot.jsp" %>
</div>
</body>
</html>