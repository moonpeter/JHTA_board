package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardReplyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();
        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();
        int result = 0;

        //파라미터로 넘어온 값들을 board 객체에 저장
        board.setBoard_name(request.getParameter("board_name"));
        board.setBoard_pass(request.getParameter("board_pass"));
        board.setBoard_subject(request.getParameter("board_subject"));
        board.setBoard_content(request.getParameter("board_content"));
        board.setBoard_re_ref(Integer.parseInt(request.getParameter("board_re_ref")));
        board.setBoard_re_lev(Integer.parseInt(request.getParameter("board_re_lev")));
        board.setBoard_re_seq(Integer.parseInt(request.getParameter("board_re_seq")));

        // 답변을 DB에 저장하기 위해 board 객체를 파라미터로 전달하고
        // DAO의 메서드 boardReply를 호출
        result = bDAO.boardReply(board);

        // 답변 저장에 실패한 경우
        if (result == 0 ) {
            System.out.println("답변 저장 실패");
            forward = new ActionForward();
            forward.setRedirect(false);
            request.setAttribute("message", "답변 저장 실패입니다.");
            forward.setPath("/error/error.jsp");
            return forward;
        }

        //답변 저장이 제대로 된 경우
        System.out.println("답변 완료");
        forward.setRedirect(true);
        forward.setPath("BoardDetailAction.bo?num="+result);
        return forward;
    }
}
