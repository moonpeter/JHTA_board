<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/08
  Time: 2:08 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/css/join.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
        h3 {
            text-align: center;
            color: #1a92b9;
        }
    </style>
</head>
<body>
<jsp:include page="../board/header.jsp"/>
<form action="updateProcess.net" name="joinform" method="post">
    <h3>회원 정보 수정</h3>
    <hr>
    <b>아이디</b>
    <input type="text" name="id" value="${memberinfo.id}" readonly>

    <b>비밀번호</b>
    <input type="password" name="pass" value="${memberinfo.password}" readonly>

    <b>이름</b>
    <input type="text" name="name" required value="${memberinfo.name}">

    <b>나이</b>
    <input type="text" name="age" required value="${memberinfo.age}">

    <b>성별</b>
    <div>
        <input type="radio" name="gender" value="남" checked><span>남자</span>
        <input type="radio" name="gender" value="여"><span>여자</span>
    </div>

    <b>이메일 주소</b>
    <input type="text" name="email" required value="${memberinfo.email}">
    <span id="email_message"></span>
    <div class="clear-fix">
        <button type="submit" class="submitbtn">정보수정</button>
        <button type="reset" class="cancelbtn">다시작성</button>
    </div>
</form>
<script>
    //성별 체크해주는 부분
    $("input[value='${memberinfo.gender}']").prop('checked',true);

    $(".cancelbtn").click(function (){
        history.back();
    });

    //처음 화면 로드시 보여줄 이메일은 이미 체크 완료된 것이므로 기본 checkemail=true입니다.
    var checkemail = true;
    $("input:eq(6)").on('keyup', function (){
        $("#email_message").empty();
        var pattern = /^\w+@\w+[.]\w{3}$/;
        var email = $("input:eq(6)").val();
        if(!pattern.test(email)) {
            $("#email_message").css('color', 'red').html("이메일 형식이 맞지 않습니다.");
            checkemail=false;
        } else {
            $("#email_message").css('color', 'green').html("이메일형식에 맞습니다.");
            checkemail=true;
        }
    });

    $('form').submit(function (){
        if(!$.isNumeric($("input[name='age']").val())){
            alert("나이는 숫자를 입력하세요");
            $("input[name='age']").val('').focus();
            return false;
        }

        if(!checkemail) {
            alert("email 형식을 확인하세요");
            $("input:eq(6)").focus();
            return false;
        }
    })
</script>
</body>
</html>
