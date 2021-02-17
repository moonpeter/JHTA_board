package net.board.action;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.board.db.CommentDAO;
import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CommentList implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommentDAO cDAO = new CommentDAO();

        int comment_board_num = Integer.parseInt(request.getParameter("comment_board_num"));
        System.out.println(comment_board_num);
        int state = Integer.parseInt(request.getParameter("state"));
        int listcount = cDAO.getListcount(comment_board_num);

        JsonObject object = new JsonObject();
        JsonArray jarray = cDAO.getCommentList(comment_board_num, state);
        object.addProperty("listocunt", listcount);
        JsonElement je = new Gson().toJsonTree(jarray);
        object.add("boardlist", je);

        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(object.toString());
        System.out.println(object.toString());
        return null;
    }
}
