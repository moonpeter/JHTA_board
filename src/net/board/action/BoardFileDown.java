package net.board.action;

import net.member.action.Action;
import net.member.action.ActionForward;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BoardFileDown implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fileName = request.getParameter("filename");
        System.out.println("fileName = " + fileName);

        String savePath = "boardupload";

        //서블릿 실행 환경정보를 담고 있는 객체를 리턴합니다.
        ServletContext context = request.getServletContext();
        String sDownloadPath = context.getRealPath(savePath);

        String sFilePath = sDownloadPath + "/" + fileName;
        System.out.println(sFilePath);

        byte b[] = new byte[4096];

        //sFilePath에 있는 파일의 MimeType을 구해옵니다.
        String sMimeType = context.getMimeType(sFilePath);
        System.out.println("sMimeType>>>" + sMimeType);

        if(sMimeType == null)
            sMimeType = "application/octet-stream";

        response.setContentType(sMimeType);

        // - 이 부분이 한글 파일명이 깨지는 것을 방지해 줍니다.
        String sEncoding = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        System.out.println(sEncoding);

        response.setHeader("Content-Disposition", "attachment; filename= " + sEncoding);

        try(
                BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath)))
        {
            int numRead;
            while ((numRead = in.read(b, 0, b.length)) != -1) {
                out2.write(b, 0, numRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
