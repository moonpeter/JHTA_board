package net.member.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {
    private DataSource ds;

    public MemberDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception ex) {
            System.out.println("DB 연결 실패" + ex);
            return;
        }
    }

    public int insert(Member member) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            conn = ds.getConnection();
            System.out.println("getConnection : insert()");

            pstmt = conn.prepareStatement("insert into member values(?,?,?,?,?,?)");
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
            pstmt.setString(5, member.getGender());
            pstmt.setString(6, member.getEmail());
            result = pstmt.executeUpdate();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            result = -1;
            System.out.println("멤버 아이디 중복 에러입니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public int isId(String id, String pass) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = -1;
        try {
            conn = ds.getConnection();

            String sql = "select id, password from member where id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getString(2).equals(pass)) {
                    result = 1;
                } else {
                    result = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
}
