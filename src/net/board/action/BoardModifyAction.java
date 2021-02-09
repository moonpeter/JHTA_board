package net.board.action;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
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

public class BoardModifyAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BoardDAO bDAO = new BoardDAO();
        Board board = new Board();
        ActionForward forward = new ActionForward();
        String realFolder = "";

        // WebContent 아래에 꼭 폴더 생성하세요
        String saveFolder = "boardupload";

        int fileSize = 5*1024*1024; // 업로드할 파일의 최대 사이즈입니다. 5MB

        ServletContext sc = request.getServletContext();
        realFolder = sc.getRealPath(saveFolder);
        System.out.println("realFolder = " + realFolder);
        boolean result = false;
        try {
            MultipartRequest multi = new MultipartRequest(request, realFolder, fileSize, "utf-8", new DefaultFileRenamePolicy());
            int num = Integer.parseInt(multi.getParameter("board_num"));
            String pass = multi.getParameter("board_pass");

            // 글쓴이 인지 확인하기 위해 저장된 비밀번호와 입력한 비밀번호를 비교
            boolean usercheck = bDAO.isBoardWriter(num, pass);

            // 비밀번호가 다른 경우
            if (usercheck == false) {
                response.setContentType("text/html;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script>");
                out.println("alert('비밀번호가 다릅니다.');");
                out.println("history.back();");
                out.println("</script>");
                out.close();
                return null;
            }

            //비밀번호가 일치하는 경우 수정 내용을 설정합니다.
            //board 객체에 글 등록 폼에서 입력받은 정보들을 저장
            board.setBoard_num(num);
            board.setBoard_subject(multi.getParameter("board_subject"));
            board.setBoard_content(multi.getParameter("board_content"));

            String check = multi.getParameter("check");
            System.out.println("check=" + check);
            if(check!=null) { // 기존파일 그대로 사용하는 경우
                board.setBoard_file(check);
            } else {
                // 업로드된 파일의 시스템 상에 업로드된 실제 파일명을 얻어옵니다.
                String filename = multi.getFilesystemName("board_file");
                board.setBoard_file(filename);
            }

            //DAO에서 수정 메서드 호출하여 수정합니다.
            result = bDAO.boardModify(board);

            // 수정에 실패한 경우
            if(result == false) {
                System.out.println("게시판 수정 실패");
                forward = new ActionForward();
                forward.setRedirect(false);
                request.setAttribute("message", "게시판 수정이 되지 않았습니다.");
                forward.setPath("/error/error.jsp");
                return forward;
            }
            // 수정 성공의 경우
            System.out.println("게시판 수정 완료");

            forward.setRedirect(true);
            //수정한 글 내용을 보여주기 위해 글 내용 보기 페이지로 이동하기 위해 경로를 설정
            forward.setPath("BoardDetailAction.bo?num=" + board.getBoard_num());
            return forward;
        } catch (IOException e) {
            e.printStackTrace();
            forward.setPath("/error/error.jsp");
            request.setAttribute("message", "게시판 업로드 중 실패입니다.");
            forward.setRedirect(false);
            return forward;
        }
    }
}
