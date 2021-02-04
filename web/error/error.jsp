<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/04
  Time: 10:49 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        body{width: 500px; margin: 3em auto}
        div{
            color: orange;
            font-size: 30px;
        }
        span {font-size: 1.5em; color: #5d5de2}
    </style>
</head>
<body>
<img src="image/error.png" alt="" width="100px"><br>
<div>죄송합니다. <br>
    ${message}</div><br>
        <span>서비스 이용에 불편을 드려 죄송합니다.</span>
</body>
</html>
