package net.member.action;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import net.member.db.Member;
import net.member.db.MemberDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MemberUpdateProcessAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String realFolder = "";

        String saveFolder = "memberupload";

        int fileSize = 5*1024*1024;

        ServletContext sc = request.getServletContext();
        realFolder = sc.getRealPath(saveFolder);
        System.out.println("realFolder = " + realFolder);
        try {
            MultipartRequest multi = null;
            multi = new MultipartRequest(request, realFolder, fileSize, "utf-8", new DefaultFileRenamePolicy());
            String id = multi.getParameter("id");
            String name = multi.getParameter("name");
            int age = Integer.parseInt(multi.getParameter("age"));
            String gender = multi.getParameter("gender");
            String email = multi.getParameter("email");
            String memberfile = multi.getFilesystemName("memberfile");

            Member member = new Member();
            member.setAge(age);
            member.setEmail(email);
            member.setGender(gender);
            member.setId(id);
            member.setName(name);
            member.setMemberfile(memberfile);

            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();

            MemberDAO mDAO = new MemberDAO();

            int result = mDAO.update(member);
            out.println("<script>");
            //삽입 된 경우
            if(result==1){
                out.println("alert('수정되었습니다.');");
                out.println("location.href='BoardList.bo';");
            } else {
                out.println("alert('회원 정보 수정에 실패했습니다.');");
                out.println("history.back()");
            }
            out.println("</script>");
            out.close();
            return null;
        } catch (IOException ex) {
            ActionForward forward = new ActionForward();
            forward.setPath("error/error.jsp");
            request.setAttribute("message", "프로필 사진 업로드 실패입니다.");
            forward.setRedirect(false);
            return forward;
        }
    }
}
