package net.board.action;

import net.board.db.Comment;
import net.board.db.CommentDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CommentDelete implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int num = Integer.parseInt(request.getParameter("num"));

        CommentDAO cDAO = new CommentDAO();

        int result = cDAO.commentDelete(num);
        PrintWriter out = response.getWriter();
        out.print(result);

        return null;
    }
}
