package net.member.action;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberLoginAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("여기는 login");
        String id = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("id")) {
                    id = cookies[i].getValue();
                }
            }
        }

        request.setAttribute("id", id);
        ActionForward forward = new ActionForward();
        forward.setRedirect(false);
        forward.setPath("member/loginForm.jsp");
        return forward;
    }
}
