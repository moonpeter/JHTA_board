<%--
  Created by IntelliJ IDEA.
  User: moonpeter
  Date: 2021/02/03
  Time: 10:23 오전
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/css/join.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            var checkid = false;
            var checkemail = false;
            $('form').submit(function () {
                if (!$.isNumeric($("input[name='age']").val())) {
                    alert("나이는 숫자를 입력하세요");
                    $("input[name='age']").val('').focus();
                    return false;
                }
                console.log("form === " + checkid)

                if (!checkid) {
                    alert("사용 가능한 id로 입력하세요");
                    $("input:eq(0)").val('').focus();
                    return false;
                }

                if (!checkemail) {
                    alert("email 형식을 확인하세요");
                    $("input:eq(6)").focus();
                    return false;
                }
            });

            $("input:eq(6)").on('keyup', function () {
                $("#email_message").empty();
                var pattern = /^\w+@\w+[.]\w{3}$/;
                var email = $("input:eq(6)").val();
                if (!pattern.test(email)) {
                    $("#email_message").css('color', 'red').html("이메일 형식이 맞지 않습니다.");
                    checkemail = false;
                } else {
                    $("#email_message").css('color', 'green').html("이메일형식에 맞습니다.");
                    checkemail = true;
                }
            });

        $("input:eq(0)").on('keyup', function () {
                checkid = true;
                $("#message").empty();
                var pattern = /^\w{5,12}$/;  // \w = [A-Za-z0-9_]
                var id = $("input:eq(0)").val();
                if (!pattern.test(id)) {
                    $("#message").css('color', 'red').html("영문자 숫자 _로 5~12자 가능합니다.");
                    checkid = false;
                    console.log("testfalse === " + checkid)
                }
            })
        })
    </script>
</head>
<body>
<form action="joinProcess.net" name="joinform" method="post">
    <h1>회원가입</h1>
    <hr>
    <b>아이디</b>
    <input type="text" name="id" placeholder="Enter id" required maxlength="12">
    <span id="message"></span>

    <b>비밀번호</b>
    <input type="password" name="pass" placeholder="Enter password" required>

    <b>이름</b>
    <input type="text" name="name" placeholder="Enter name" maxlength="15" required>

    <b>나이</b>
    <input type="text" name="age" placeholder="Enter age" required>

    <b>성별</b>
    <div>
        <input type="radio" name="gender" value="남" checked><span>남자</span>
        <input type="radio" name="gender" value="여"><span>여자</span>
    </div>

    <b>이메일 주소</b>
    <input type="text" name="email" placeholder="Enter email" required>
    <span id="email_message"></span>
    <div class="clear-fix">
        <button type="submit" class="submitbtn">회원가입</button>
        <button type="reset" class="cancelbtn">다시작성</button>
    </div>
</form>
</body>
</html>
