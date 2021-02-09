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
    <script>
        $(function () {
            // 검색 클릭 후 응답화면에는 검색시 선택한 필드가 선택되도록 합니다.
            var selectedValue = '${search_field}'
            if (selectedValue != '-1')
                $("#viewcount").val(selectedValue);

            //검색 버튼 클릭한 경우
            $("button").click(function () {
                //검색어 공백 유효성 검사
                if ($("input").val() == '') {
                    alert("검색어를 입력하세요");
                    $("input").focus();
                    return false;
                }

                word = $(".input-group input").val();
                if (selectedValue == 2) {
                    pattern = /^[0-9]{2}$/;
                    if (!pattern.test(word)) {
                        alert("나이는 형식에 맞게 입력하세요(두자리 숫자)");
                        return false;
                    }
                } else if (selectedValue == 3) {
                    if (word != "남" && word != "여") {
                        alert("남 또는 여를 입력하세요");
                        return false;
                    }
                }
            });

            // 검색어 입력창에 placeholder 나타나도록 합니다.
            $("#viewcount").change(function () {
                selectedValue = $(this).val();
                $("input").val('');
                message = ["아이디", "이름", "나이", "여 또는 남"]
                $("input").attr("placeholder", message[selectedValue] + " 입력하세요");
            })

            // 회원 목록의 삭제를 클릭한 경우
            $("tr > td:nth-child(3) > a").click(function (event) {
                var answer = confirm("정말 삭제하시겠습니까?");
                console.log(answer); // 취소를 클릭한 경우 - false
                if (!answer) {//취소를 클릭한 경우
                    event.preventDefault(); //이동하지 않습니다.
                }
            })
        })
    </script>
    <style>
        table caption {
            caption-side: top;
            text-align: center
        }

        h1 {
            text-align: center
        }

        li .current {
            background: #faf7f7;
            color: gray;
        }

        body > div > table > tbody > tr > td:last-child > a {
            color: red
        }

        form {
            margin: 0 auto;
            width: 80%;
            text-align: center
        }

        select {
            color: #495057;
            background-color: #fff;
            background-clip: padding-box;
            border: 1px solid #ced5da;
            border-radius: 0.25rem;
            transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
            outline: none;
        }

        .container {
            width: 60%
        }

        td:nth-child(1) {
            width: 33%
        }

        .input-group {
            margin-bottom: 3em
        }
    </style>
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
                <td>아이디</td>
                <td>이름</td>
                <td>삭제</td>
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
                        <a href="memberList.net?page=${page-1}&search_field=${search_field}&search_word=${search_word}"
                           class="page-link">이전&nbsp;</a>
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
                            <a href="memberList.net?page=${a}&search_field=${search_field}&search_word=${search_word}"
                               class="page-link">${a}</a>
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
                        <a href="memberList.net?page=${page+1}&search_field=${search_field}&search_word=${search_word}"
                           class="page-link">&nbsp;다음</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </c:if>

    <c:if test="${listocunt == 0 && empty search_word }">
        <h1>등록된 회원이 없습니다.</h1>
    </c:if>

    <c:if test="${listocunt == 0 && !empty search_word }">
        <h1>검색 결과 없습니다.</h1>
    </c:if>
</div>
</body>
</html>
