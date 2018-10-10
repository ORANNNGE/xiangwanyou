<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>视频预览</title>
    <meta name="decorator" content="ani"/>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <div class="panel-body">
                    <video style="height: 450px; width: 730px;" controls="controls"
                           src="${wxMediaFiles.uploadUrl}"></video>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>