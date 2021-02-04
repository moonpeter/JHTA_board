package net.member.action;

import net.member.db.MemberDAO;
import net.member.db.Member;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MemberJoinProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        String pass = request.getParameter("pass");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");

        Member member = new Member();
        member.setId(id);
        member.setPassword(pass);
        member.setName(name);
        member.setAge(age);
        member.setGender(gender);
        member.setEmail(email);

        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        MemberDAO mdao = new MemberDAO();

        int result = mdao.insert(member);
        if (result == 1) {
            out.println("<script>alert('회원가입을 축하드립니다.');location.href='login.net';</script>");
        } else if(result == -1){
            out.println("<script>alert('아이디가 중복되었습니다. 다시 입력하세요.');history.back();</script>");
        } else {
            out.println("<script>alert('회원가입에 실패하였습니다.');location.href='join.net';</script>");

        }
        out.close();
        return null;
    }
}
