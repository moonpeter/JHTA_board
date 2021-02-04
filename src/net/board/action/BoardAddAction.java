package net.board.action;

import net.board.db.Board;
import net.board.db.BoardDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;

public class BoardAddAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();
        ActionForward forward = new ActionForward();

        String realFolder = "";

        String saveFolder = "boardupload";

        int fileSize = 5*1024*1024;

        //실제 저장 경로를 지정
        ServletContext sc = request.getServletContext();
        realFolder = sc.getRealPath(saveFolder);

        System.out.println("realFolder = " + realFolder);
        boolean result = false;

        try{
            MultipartRequest multi = null;
            multi = new MultipartRequest(request, realFolder, fileSize, "utf-8", new DefaultFileRenamePolicy());

            // Board 객체에 글 등록 폼에서 입력 받은 정보들을 저장
            board.setBoard_name(multi.getParameter("board_name"));
            board.setBoard_pass(multi.getParameter("board_pass"));
            board.setBoard_subject(multi.getParameter("board_subject"));
            board.setBoard_content(multi.getParameter("board_content"));

            // 시스템 상에 업로드된 실제 파일명을 얻어옵니다.
            String filename = multi.getFilesystemName("board_file");
            board.setBoard_file(filename);
            
            // 글 등록 처리를 위해 DAO의 boardInsert()메서드를 호출
            // 글 등록 폼에서 입력한 정보가 저장되어 있는 board객체를 전달
            result = bDAO.boardInsert(board);
            
            // 글 등록에 실패할 경우 false를 반환
            if(result==false) {
                System.out.println("게시판 등록 실패");
                forward.setPath("error/error.jsp");
                request.setAttribute("message", "게시판 등록 실패입니다.");
                forward.setRedirect(false);
                return forward;
            }
            System.out.println("게시판 등록 완료");

            // 글 등록이 완료되면 목록을 보여주기 위해 "BoardList.bo"로 이동
            // Redirect 여부를 true로 설정
            forward.setRedirect(true);
            forward.setPath("BoardList.bo");    // 이동할 경로를 지정
            return forward;
        } catch (IOException ex) {
            forward.setPath("error/error.jsp");
            request.setAttribute("message", "게시판 등록 실패입니다.");
            forward.setRedirect(false);
            return forward;
        }
    }
}
