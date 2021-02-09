package net.member.action;

import net.member.db.Member;
import net.member.db.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MemberUpdateAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");

        MemberDAO mDAO = new MemberDAO();
        Member member = mDAO.member_info(id);

        forward.setPath("/member/updateForm.jsp");
        forward.setRedirect(false);
        request.setAttribute("memberinfo", member);
        return forward;
    }
}
