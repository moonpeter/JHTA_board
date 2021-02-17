package net.board.action;

import net.member.action.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.bo")
public class BoardFrontController extends javax.servlet.http.HttpServlet{
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
            case "/BoardWrite.bo":
                action = new BoardWriteAction();
                break;
            case "/BoardList.bo":
                action = new BoardListAction();
                break;
            case "/BoardAddAction.bo":
                action = new BoardAddAction();
                break;
            case "/BoardReplyView.bo":
                action = new BoardReplyView();
                break;
            case "/BoardReplyAction.bo":
                action = new BoardReplyAction();
                break;
            case "/BoardDeleteAction.bo":
                action = new BoardDeleteAction();
                break;
            case "/BoardFileDown.bo":
                action = new BoardFileDown();
                break;
            case "/BoardModifyView.bo":
                action = new BoardModifyView();
                break;
            case "/BoardModifyAction.bo":
                action = new BoardModifyAction();
                break;
            case "/BoardDetailAction.bo":
                action = new BoardDetailAction();
                break;
            case "/CommentAdd.bo":
                action = new CommentAdd();
                break;
            case "/CommentList.bo":
                action = new CommentList();
                break;
            case "/CommentDelete.bo":
                action = new CommentDelete();
                break;
            case "/CommentUpdate.bo":
                action = new CommentUpdate();
                break;
            case "/CommentReply.bo":
                action = new CommentReply();
                break;
        }
        forward = action.execute(request, response);

        if (forward != null) {
            if (forward.isRedirect()){  // 리다이렉트
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
