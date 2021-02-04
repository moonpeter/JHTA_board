package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoardListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BoardDAO bDAO = new BoardDAO();
        List<Board> boardList = new ArrayList<Board>();

        // 로그인 성공시 파라미터 page가 없어요. 그래서 초기값이 필요함
        int page = 1;   // 보여줄 page
        int limit = 10; // 한 페이지에 보여줄 게시판 목록의 수
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        System.out.println("넘어온 페이지 = " + page);
        System.out.println("넘어온 limit = " + limit);

        // 총 리스트 수를 받아옵니다.
        int listcount = bDAO.getListCount();

        // 리스트를 받아옵니다.
        boardList = bDAO.getBoardList(page, limit);

        // 총 페이지 수 =
        // (DB에 저장된 총 리스트 수 + 한 페이지에서 보여주는 리스트의 수 -1) / 한 페이지에서 보여주는 리스트의 수
        int maxpage = (listcount + limit - 1) / limit;
        System.out.println("총 페이지 수 = " + maxpage);

        int startpage = ((page - 1) / 10) * 10 + 1;
        System.out.println("현제 페이지에 보여줄 시작 페이지 수 = " + startpage);
        int endpage = startpage + 10 - 1;
        System.out.println("현제 페이지에 보여줄 마지막 페이지 수 = " + endpage);

        if (endpage > maxpage) {
            endpage = maxpage;
        }

        request.setAttribute("page", page); // 현재 페이지 수
        request.setAttribute("maxpage", maxpage); // 최대 페이지 수

        // 현재 페이지에 표시할 첫 페이지 수
        request.setAttribute("startpage", startpage);

        // 현재 페이지에 표시할 끝 페이지 수
        request.setAttribute("endpage", endpage);

        request.setAttribute("listcount", listcount); // 총 글의 수
        
        //해당 페이지의 글 목록을 갖고 있는 리스트
        request.setAttribute("boardlist", boardList);
        request.setAttribute("limit", limit);
        ActionForward forward = new ActionForward();
        forward.setRedirect(false);
        
        //글 목록 페이지로 이동하기 위해 경로를 설정
        forward.setPath("board/boardList.jsp");
        return forward;
    }
}
