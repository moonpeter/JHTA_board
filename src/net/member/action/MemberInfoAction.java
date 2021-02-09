package net.member.action;

import net.member.db.Member;
import net.member.db.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MemberInfoAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();
        String id = request.getParameter("id");
        MemberDAO mDAO = new MemberDAO();
        Member member = mDAO.member_info(id);

        if(member ==null) {
            forward.setPath("/error/error.jsp");
            forward.setRedirect(false);
            request.setAttribute("message", "아이디에 해당하는 정보가 없습니다.");
        }


        forward.setPath("/member/memberInfo.jsp");
        forward.setRedirect(false);
        request.setAttribute("memberinfo", member);
        return forward;
    }
}
