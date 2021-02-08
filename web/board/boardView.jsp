<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/02
  Time: 4:15 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <style>
        tr:nth-child(1) {text-align: center}
        td:nth-child(1) {width: 20%}
        a { color: white}
        tr:nth-child(5)>td:nth-child(2)>a{color:black}
        tbody tr:last-child {text-align: center}
        .btn-primary {background-color: #4f97e5}
        #myModal {display: none}
        tr>td:nth-child(1) {font-weight: bold; font-size: 1em}
    </style>
</head>
<body>
<div class="container">
    <table class="table">
        <tr><th colspan="2">MVC 게시판 - view 페이지</th></tr>
        <tr>
            <td><div>글쓴이</div></td>
            <td><div>${board.board_name}</div></td>
        </tr>
        <tr>
            <td><div>제목</div></td>
            <td><c:out value="${board.board_subject}"/></td>
        </tr>
        <tr>
            <td><div>내용</div></td>
            <td style="padding-right: 0px"><textarea class="form-control" rows="5"
                readonly>${board.board_content}</textarea></td>
        </tr>

        <c:if test="${board.board_re_lev==0}">
            <tr>
                <td><div>첨부파일</div></td>
                <c:if test="${!empty board.board_file}">
                    <td><img src="/image/down.png" width="10px">
                        <a href="BoardFileDown.bo?filename=${board.board_file}">
                            ${board.board_file}
                        </a>
                    </td>
                </c:if>
                <c:if test="${empty board.board_file}">
                    <td></td>
               </c:if>
            </tr>
        </c:if>

        <tr>
            <td colspan="2" class="center">
                <a href="BoardReplyView.bo?num=${board.board_num}">
                    <button class="btn btn-primary">답변</button>
                </a>
                <c:if test="${board.board_name == id || id == 'admin' }">
                    <a href="BoardModifyView.bo?num=${board.board_num}">
                        <button class="btn btn-info">수정</button>
                    </a>
                    <a href="#">
                        <button class="btn btn-danger" data-toggle="modal" data-target="#myModal">삭제</button>
                    </a>
                </c:if>
                <a href="BoardList.bo">
                    <button class="btn btn-secondary">목록</button>
                </a>
            </td>
        </tr>
    </table>

    <div class="modal" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body">
                    <form name="deleteForm" action="BoardDeleteAction.bo" method="post">
                        <input type="hidden" name="num" value="${param.num}">
                        <div class="form-group">
                            <label for="pwd">비밀번호</label>
                            <input type="password" class="form-control" placeholder="Enter password" name="board_pass" id="board_pass">
                        </div>
                        <button type="submit" class="btn btn-primary">전송</button>
                        <button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
