package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardModifyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();
        ActionForward forward = new ActionForward();
        System.out.println("^^^^^^^^^^^ = " + request.getParameter("num"));
        int num = Integer.parseInt(request.getParameter("num"));
        System.out.println("(Action)request.getParameter(num) : " + request.getParameter("num"));
        System.out.println("(Action)num : " + num);

        boolean result = false;

        board.setBoard_name(request.getParameter("board_name"));
        board.setBoard_pass(request.getParameter("board_pass"));
        board.setBoard_subject(request.getParameter("board_subject"));
        board.setBoard_content(request.getParameter("board_content"));

        System.out.println("(Action)board_name : " + request.getParameter("board_name"));
        System.out.println("(Action)board_pass : " + request.getParameter("board_pass"));
        System.out.println("(Action)board_subject : " + request.getParameter("board_subject"));
        System.out.println("(Action)board_content : " + request.getParameter("board_content"));

        result = bDAO.boardUpdate(board);

        if(result == false) {
            System.out.println("게시글 수정 실패");
            forward.setPath("error/error.jsp");
            request.setAttribute("message", "게시글 수정 실패입니다.");
            forward.setRedirect(false);
            return forward;
        }
        System.out.println("게시글 수정 완료");
        forward.setRedirect(true);
        forward.setPath("BoardDetailAction.bo?num=" + num);
        return forward;
    }
}
