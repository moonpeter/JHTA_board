package net.member.action;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.net")
public class MemberFrontController extends javax.servlet.http.HttpServlet{
    private static final long serialVersionUID = 1L;

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String RequestURI = request.getRequestURI();
        System.out.println("RequestURI = " + RequestURI);

        String contextPath = request.getContextPath();
        System.out.println("contextPath = " + contextPath);

        String command = RequestURI.substring(contextPath.length());
        System.out.println("command = " + command);

        ActionForward forward = null;
        Action action = null;

        switch (command) {
            case "/join.net":
                action = new MemberJoinAction();
                break;
            case "/idcheck.net":
                action = new MemberIdCheckAction();
                break;
            case "/login.net":
                action = new MemberLoginAction();
                break;
            case "/joinProcess.net":
                action = new MemberJoinProcessAction();
                break;
            case "/loginProcess.net":
                action = new MemberLoginProcessAction();
                break;
            case "/logout.net":
                action = new MemberLogoutAction();
                break;
            case "/memberUpdate.net":
                action = new MemberUpdateAction();
                break;
            case "/updateProcess.net":
                action = new MemberUpdateProcessAction();
                break;
            case "/memberList.net":
                action = new MemberSearchAction();
                break;
            case "/memberInfo.net":
                action = new MemberInfoAction();
                break;
            case "/memberDelete.net":
                action = new MemberDeleteAction();
                break;
        }
        forward = action.execute(request, response);

        if (forward != null) {
            if (forward.isRedirect()){
                response.sendRedirect(forward.getPath());
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("utf-8");
        doProcess(request, response);
    }
}
