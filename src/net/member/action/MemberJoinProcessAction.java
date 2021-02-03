package net.member.action;

import net.member.db.DAO;
import net.member.db.Member;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MemberJoinProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

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

        DAO dao = new DAO();
        int result = dao.insert(member);
        System.out.println(result);
        if (result == 1) {
            out.println("<script>alert('회원가입을 축하드립니다.');location.href='/';</script>");
        } else {
            out.println("<script>alert('회원가입에 실패했습니다.');location.href='/';</script>");
        }
        out.close();

        forward.setRedirect(false);
        forward.setPath("member/joinForm.jsp");
        return forward;
    }

}
