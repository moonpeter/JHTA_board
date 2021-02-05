package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardDetailAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();

        // 글 번호 파라미터 값을 num 변수에 저장
        int num = Integer.parseInt(request.getParameter("num"));

        // 내용을 확인할 글의 조회수를 증가시킴
        bDAO.setReadCountUpdate(num);

        // 글의 내용을 DAO에서 읽은 후 얻은 결과를 board 객체에 저장
        board = bDAO.getDetail(num);

        //board=null;//error테스트를 위한 값 설정
        //DAO에서 글의 내용을 읽지 못했을 경우 null을 반환
        if(board==null){
            System.out.println("상세보기 실패");
            ActionForward forward = new ActionForward();
            forward.setRedirect(false);
            request.setAttribute("message", "데이터를 읽지 못했습니다.");
            forward.setPath("error/error.jsp");
            return forward;
        }
        System.out.println("상세보기 성공");

        //board 객체를 request 객체에 저장
        request.setAttribute("board", board);

        ActionForward forward = new ActionForward();
        forward.setRedirect(false);

        // 글 내용 보기 페이지로 이동하기 위해 경로를 설정
        forward.setPath("board/boardView.jsp");
        return forward;
    }
}
