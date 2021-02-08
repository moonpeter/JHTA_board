<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/02
  Time: 4:14 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
    <jsp:include page="header.jsp"/>
    <script src="/js/modifyform.js"></script>
    <style>
        .container {width: 60%}
        h1 {font-size: 1.5rem; text-align: center; color: #1a92b9;}
        #upfile{display: none}
    </style>
</head>
<body>
<div class="container">
    <form action="BoardModifyAction.bo" method="post" name="modifyform" enctype="multipart/form-data">
        <input type="hidden" name="board_num" value="${board.board_num}">
        <h1>MVC 게시판 - 수정</h1>
        <div class="form-group">
            <label for="board_name">글쓴이</label>
            <input type="text" class="form-control" value="${board.board_name}" readonly>
        </div>

        <div class="form-group">
            <label for="board_subject">제목</label>
            <textarea name="board_subject" id="board_subject" rows="1" class="form-control" maxlength="100">${board.board_subject}</textarea>
        </div>

        <div class="form-group">
            <label for="board_content">내용</label>
            <textarea name="board_content" id="board_content" class="form-control" rows="15">${board.board_content}</textarea>
        </div>

        <c:if test="${board.board_re_lev==0}">
            <div class="form-group read">
                <label for="board_file">첨부파일</label>
                <label for="upfile">
                    <img src="/image/attach.png" alt="첨부파일" width="20px">
                </label>
                <input type="file" id="upfile" name="board_file">
                <span id="filevalue">${board.board_file}</span>
                <img src="/image/remove.png" alt="파일삭제" width="10xp" class="remove">
            </div>
        </c:if>

        <div class="form-group">
            <label for="board_pass">비밀번호</label>
            <input type="password" name="board_pass" id="board_pass" size="10" maxlength="30" class="form-control" placeholder="Enter board_pass" value="">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary">수정</button>
            <button type="reset" class="btn btn-danger" onclick="history.go(-1)">취소</button>
        </div>
    </form>
</div>
</body>
</html>
