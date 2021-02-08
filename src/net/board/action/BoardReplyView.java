package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardReplyView implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();
        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();

        // 파라미터로 전달받은 답변할 글 번호를 num변수에 저장
        int num = Integer.parseInt(request.getParameter("num"));

        // 글 번호 num에 해당하는 내용을 가져와서 board 객치에 wjwkd
        board = bDAO.getDetail(num);

        // 글 내용이 없는 경우
        if(board == null) {
            System.out.println("글이 존재하지 않습니다.");
            forward.setRedirect(false);
            request.setAttribute("message", "글이 존재하지 않습니다.");
            forward.setPath("error/error.jsp");
            return forward;
        }

        System.out.println("답변 페이지 이동 완료");
        // 답변 폼 페이지로 이동 할 때 원본글 내용을 보여주기 위해
        // board 객체를 Request 객체에 저장
        request.setAttribute("board", board);

        forward.setRedirect(false);
        forward.setPath("board/boardReply.jsp");
        return forward;
    }
}
