package net.member.action;

import net.member.db.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberIdCheckAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MemberDAO mDAO = new MemberDAO();
        int result = mDAO.isId(request.getParameter("id"));
        response.getWriter().append(Integer.toString(result));
        System.out.println(result);
        return null;
    }
}
