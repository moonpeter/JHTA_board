package net.member.action;

import net.member.db.Member;
import net.member.db.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MemberUpdateProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        // String pass=requset.getParameter("pass");
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");

        Member member = new Member();
        member.setAge(age);
        member.setEmail(email);
        member.setGender(gender);
        member.setId(id);
        member.setName(name);

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        MemberDAO mDAO = new MemberDAO();

        int result = mDAO.update(member);
        out.println("<script>");
        //삽입 된 경우
        if(result==1){
            out.println("alert('수정되었습니다.');");
            out.println("location.href='BoardList.bo';");
        } else {
            out.println("alert('회원 정보 수정에 실패했습니다.');");
            out.println("history.back()");
        }
        out.println("</script>");
        out.close();
        return null;
    }
}
