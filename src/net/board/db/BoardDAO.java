package net.board.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {
    private DataSource ds;

    public BoardDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception ex) {
            System.out.println("DB 연결 실패" + ex);
            return;
        }
    }


    public boolean boardInsert(Board board) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            conn = ds.getConnection();

            String max_sql = "(select nvl(max(board_num), 0)+1 from board)";

            // 원문글의 BOARD_RE_REF 필드는 자신의 글번호입니다.
            String sql = "insert into " +
                    "board(board_num, board_name, board_pass, board_subject, board_content, board_file, BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT)  " +
                    "values(" + max_sql + ",?,?,?,?,?," + max_sql + ",?,?,?)";

            // 새로운 글을 등록하는 부분입니다.
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getBoard_name());
            pstmt.setString(2, board.getBoard_pass());
            pstmt.setString(3, board.getBoard_subject());
            pstmt.setString(4, board.getBoard_content());
            pstmt.setString(5, board.getBoard_file());

            // 원문의 경우 BOARD_RE_LEV, BOARD_RE_SEQ 필드 값은 0임
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            pstmt.setInt(8, 0);
            result = pstmt.executeUpdate();

            if (result == 1) {
                System.out.println("데이터 삽입이 모두 완료되었습니다.");
                return true;
            }
        } catch (Exception ex) {
            System.out.println("boardInsert() 에러 : " + ex);
            ex.printStackTrace();
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
        return false;
    }

    // 글의 갯수 구하
    public int getListCount() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int x = 0;
        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement("select count(*) from board");
            rs = pstmt.executeQuery();

            if (rs.next()) {
                x = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println("getListCount() 에러 : " + ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return x;
    }

    public List<Board> getBoardList(int page, int limit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String board_list_sql = "select * from(select rownum rnum, board_num, board_name, board_subject, board_content, board_file, board_re_ref, board_re_lev, board_re_seq, board_readcount, board_date " +
                "from (select * from board order by board_re_ref desc, board_re_seq asc)) where rnum>=? and rnum<=? ";

        List<Board> list = new ArrayList<Board>();
        // 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
        int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호(1 11 21 31 ...
        int endrow = startrow + limit - 1;     // 읽을 마지막 row 번호(10 20 30 40 ...)
        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(board_list_sql);
            pstmt.setInt(1, startrow);
            pstmt.setInt(2, endrow);
            rs = pstmt.executeQuery();

            // DB에서 가져온 데이터를 VO객체에 담습니다.
            while (rs.next()) {
                Board board = new Board();
                board.setBoard_num(rs.getInt("board_num"));
                board.setBoard_name(rs.getString("board_name"));
                board.setBoard_subject(rs.getString("board_subject"));
                board.setBoard_content(rs.getString("board_content"));
                board.setBoard_file(rs.getString("board_file"));
                board.setBoard_re_ref(rs.getInt("board_re_ref"));
                board.setBoard_re_lev(rs.getInt("board_re_lev"));
                board.setBoard_re_seq(rs.getInt("board_re_seq"));
                board.setBoard_re_readcount(rs.getInt("board_readcount"));
                board.setBoard_date(rs.getString("board_date"));
                list.add(board);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getBoardList() 에러 : " + ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return list;
    }
}
