package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardModifyView implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();
        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();

        //파라미터로 전달받은 수정할 글 번호를 num변수에 저장
        int num = Integer.parseInt(request.getParameter("num"));
        //글 내용을 불러와서 board객에체 저장
        board = bDAO.getDetail(num);

        //글 내용 불러오기 실패한 경우
        if(board==null){
            System.out.println("(수정) 상세보기 실패");
            forward = new ActionForward();
            forward.setRedirect(false);
            request.setAttribute("message", "게시판 수정 상세 보기 실패입니다.");
            forward.setPath("error/error.jsp");
        }
        System.out.println("(수정) 상세보기 성공");

        request.setAttribute("board", board);
        forward.setRedirect(false);
        forward.setPath("board/boardModify.jsp");
        return forward;
    }
}
