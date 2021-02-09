package net.member.action;

import net.member.db.Member;
import net.member.db.MemberDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MemberSearchAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionForward forward = new ActionForward();
        MemberDAO mDAO = new MemberDAO();

        int page = 1;
        int limit = 3;
        if(request.getParameter("page") !=null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        System.out.println("넘어온 페이지 = " + page);
        System.out.println("넘어온 limit = " + limit);

        List<Member> memberList = null;
        int listcount = 0;
        int index=-1;

        String search_word="";
        //메뉴-관리자-회원정보 클릭한 경우(member_list.net)
        //또는 메뉴-관리자-회원정보 클릭 후 페이지 클릭한 경우(member_list.net?page=2&search_field=-1&search_word=)
        if (request.getParameter("search_word") == null || request.getParameter("search_word").equals("")){
            // 총 리스트 수를 받아옵니다.
            listcount = mDAO.getListCount();
            memberList = mDAO.getMemberList(page, limit);
        } else { //검색을 클릭한 경우
            index=Integer.parseInt(request.getParameter("search_field"));
            String[] search_field = new String[] {"id", "name", "age", "gender"};
            search_word = request.getParameter("search_word");
            listcount=mDAO.getListCount(search_field[index], search_word);
            memberList = mDAO.getMemberList(search_field[index], search_word, page, limit);
        }

        int maxpage = (listcount + limit -1) /limit;
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
        request.setAttribute("memberlist", memberList);
        request.setAttribute("search_field", index);
        request.setAttribute("search_word", search_word);

        forward.setPath("member/memberList.jsp");
        forward.setRedirect(false);
        return forward;
    }
}
