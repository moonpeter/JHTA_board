<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/08
  Time: 4:29 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
    <jsp:include page="../board/header.jsp"/>

</head>
<body>
<div class="container">
    <form action="memberList.net" method="post">
        <div class="input-group">
            <select name="search_field" id="viewcount">
                <option value="0" selected>아이디</option>
                <option value="1">이름</option>
                <option value="2">나이</option>
                <option value="3">성별</option>
            </select>
            <input type="text" name="search_word" class="form-control" placeholder="아이디 입력하세요" value="${search_word}">
            <button class="btn btn-primary" type="submit">검색</button>
        </div>
    </form>
    <c:if test="${listcount>0}">
        <table class="table table-striped">
            <caption style="font-weight: bold">회원목록</caption>
            <thead>
            <tr>
                <th colspan="2">MVC 게시판 - 회원 정보 List</th>
                <th>
                    <font size="3">회원 수 : ${listcount}</font>
                </th>
            </tr>
            <tr>
                <td>아이디</td><td>이름</td><td>삭제</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="m" items="${memberlist}">
                <tr>
                    <td>
                        <a href="memberInfo.net?id=${m.id}">${m.id}</a>
                    </td>
                    <td>${m.name}</td>
                    <td><a href="memberDelete.net?id=${m.id}" style="color: red">삭제</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div class="center-block">
            <ul class="pagination justify-content-center">
                <c:if test="${page <=1 }">
                    <li class="page-item">
                        <a class="page-link current" href="#">이전&nbsp;</a>
                    </li>
                </c:if>
                <c:if test="${page>1}">
                    <li class="page-item">
                        <a href="memberList.net?page=${page-1}&search_field=${search_field}&search_word=${search_word}" class="page-link">이전&nbsp;</a>
                    </li>
                </c:if>

                <c:forEach var="a" begin="${startpage}" end="${endpage}">
                    <c:if test="${a == page}">
                        <li class="page-item">
                            <a class="page-link current" href="#">${a}</a>
                        </li>
                    </c:if>
                    <c:if test="${a != page}">
                        <li class="page-item">
                            <a href="memberList.net?page=${a}&search_field=${search_field}&search_word=${search_word}" class="page-link">${a}</a>
                        </li>
                    </c:if>
                </c:forEach>

                <c:if test="${page >= maxpage }">
                    <li class="page-item">
                        <a class="page-link current">&nbsp;다음</a>
                    </li>
                </c:if>
                <c:if test="${page < maxpage }">
                    <li class="page-item">
                        <a href="memberList.net?page=${page+1}&search_field=${search_field}&search_word=${search_word}" class="page-link">&nbsp;다음</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </c:if>

    <c:if test="${listocunt == 0 && empty search_word }">
        <h1>등록된 회원이 없습니다.</h1>
    </c:if>

    <c:if test="${listocunt == 0 && !empty search_word }">
        <h1>검색 결과가 없습니다.</h1>
    </c:if>
</div>
</body>
</html>
