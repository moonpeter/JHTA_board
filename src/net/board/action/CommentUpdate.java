package net.board.action;

import net.board.db.Comment;
import net.board.db.CommentDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentUpdate implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommentDAO cDAO = new CommentDAO();
        Comment co = new Comment();

        co.setContent(request.getParameter("content"));
        System.out.println("content = " + co.getContent());

        co.setNum(Integer.parseInt(request.getParameter("num")));

        int ok = cDAO.commentUpdate(co);
        response.getWriter().print(ok);

        return null;
    }
}
