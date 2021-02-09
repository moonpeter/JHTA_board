package net.member.action;

import net.board.db.BoardDAO;
import net.member.db.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MemberDeleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MemberDAO mDAO = new MemberDAO();
        String id = request.getParameter("id");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        int result = mDAO.memberDelete(id);
        if (result ==1) {
            out.println("<script>");
            out.println("alert('삭제 성공입니다.');");
            out.println("location.href='memberList.net'");
            out.println("</script>");
        } else {
            out.println("<script>");
            out.println("alert('회원 삭제 실패입니다.');");
            out.println("history.back()");
            out.println("</script>");
        }
        out.close();
        return null;
    }
}
