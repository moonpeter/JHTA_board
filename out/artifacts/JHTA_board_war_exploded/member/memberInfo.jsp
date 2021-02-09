<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/09
  Time: 11:46 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="/board/header.jsp"/>
    <style>
        tr>td:nth-child(odd){font-weight: bold}
        td{text-align: center}
        .container{width: 50%}
    </style>
</head>
<body>

<div class="container">
<table class="table table-bordered">
    <tr>
        <td>아이디</td>
        <td>${memberinfo.id}</td>
    </tr>
    <tr>
        <td>비밀번호</td>
        <td>${memberinfo.password}</td>
    </tr>
    <tr>
        <td>이름</td>
        <td>${memberinfo.name}</td>
    </tr>
    <tr>
        <td>나이</td>
        <td>${memberinfo.age}</td>
    </tr>
    <tr>
        <td>성별</td>
        <td>${memberinfo.gender}</td>
    </tr>
    <tr>
        <td>이메일 주소</td>
        <td>${memberinfo.email}</td>
    </tr>
    <tr>
        <td colspan="2"><a href="memberList.net">리스트로 돌아가기</a></td>
    </tr>
</table>
</div>

</body>
</html>
