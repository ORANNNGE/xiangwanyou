<%--
  Created by IntelliJ IDEA.
  User: 52555
  Date: 2018/11/22
  Time: 9:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <!--<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">-->
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no ,height=device-height"/>
    <link rel="stylesheet" href="css/base.css" />
    <title>享玩游</title>
    <style>
        html
        {
            height:100%;
            margin:0;
        }
        body
        {
            height:100%;
            margin:0;
        }
    </style>
</head>
<body>
<div class="index-body">
    <div class="index-bg"><img src="images/you-bg.png" width="100%"/></div>
    <div class="index-you">
        <div class="index-logo"><img src="images/you-logo.png" width="100%"/></div>
        <button class="index-click" onclick="download()"><i><img src="images/you-xiazai.png"/></i>点击安装</button>
        <p id="fileInfo"></p>
        <div class="index-phone">
            <p class="indphone-a">适用于Android设备</p>
            <p class="indphone-b">安卓</p>
        </div>
        <div class="index-qr">
            <p>或者用手机扫描下面的二维码安装</p>
            <i><img src="images/1543044487.png" width="100%"/></i>
            <span class="text">http://39.54.104.1/download/download.jsp</span><button class="btn" data-clipboard-action="copy" data-clipboard-target="span">复制地址</button>
        </div>
    </div>
</div>
<div class="index-black">
    <div class="indblack-up"><img src="images/you-xz.png" width="100%"/></div>
    <div class="indblack-li">
        <span>1</span>
        <p>点击右上角<i style="width: 12%;"><img src="images/you-cd.png" width="100%"/></i>打开菜单</p>
    </div>
    <div class="indblack-li">
        <span>2</span>
        <p>选择<i style="width: 68%;"><img src="images/you-liulq.png" width="100%"/></i></p>
    </div>
</div>
</body>
<script src="js/jquery.1.9.1.min.js"></script>
<script src="dist/clipboard.min.js"></script>
<!--<script type="text/javascript" src="http://www.jq22.com/demo/copy20161120/dist/clipboard.min.js"></script>-->

<script>

 //请求下载接口
function download() {
    console.log();
    location.href='../xiangwanyou.apk'
}

//判断浏览器类型
var browser = navigator.userAgent;
// alert(browser);
if(browser.indexOf('QQ') < 0){
    $('.index-black').hide();
}


$(function () {
    $.ajax({
        url:'../app/getDownloadFileInfo',
        dataType:'json',
        success:function (result) {
            var size = result.body.size;
            var date = result.body.date;
            $('#fileInfo').text(size+'M / '+date)
        }

    })
})

//复制地址
 var content = $("#urlContent").html();
 var clipboard = new ClipboardJS('.btn');
 clipboard.on('success', function(e) {
     alert("复制成功");
 });

 clipboard.on('error', function(e) {
     console.log(e);
 });
</script>
</html>

