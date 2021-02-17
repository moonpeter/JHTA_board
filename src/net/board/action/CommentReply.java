package net.board.action;

import net.board.db.Comment;
import net.board.db.CommentDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentReply implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommentDAO cDAO = new CommentDAO();
        Comment co = new Comment();

        co.setId(request.getParameter("id"));
        co.setContent(request.getParameter("content"));
        co.setComment_re_lev(Integer.parseInt(request.getParameter("comment_re_lev")));
        co.setComment_board_num(Integer.parseInt(request.getParameter("comment_board_num")));
        co.setComment_re_seq(Integer.parseInt(request.getParameter("comment_re_seq")));
        co.setComment_re_ref(Integer.parseInt(request.getParameter("comment_re_ref")));
        int ok = cDAO.commentReply(co);
        response.getWriter().print(ok);
        return null;
    }
}
