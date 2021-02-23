package net.board.db;

import sun.misc.Request;

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

    // 글의 갯수 구하기
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
            System.out.println("pstmt ==== " + pstmt);
            rs = pstmt.executeQuery();
            System.out.println("ResultSet ==== " + rs);

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

    public void setReadCountUpdate(int num) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "update board set board_readcount=board_readcount+1 " +
                "where board_num = ?";

        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("setReadCountUpdate() 에러 : " + ex);
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
        }
    }

    public Board getDetail(int num) {
        Board board = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement("select * from board where board_num = ?");
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                board = new Board();
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
            }
        } catch (SQLException ex) {
            System.out.println("getDetail() 에러 : " + ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return board;
    }

    public int boardReply(Board board) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int num = 0;

        // board 테이블의 글번호를 구하기 위해 board_num 필드의 최대값을 구해옵니다.
        String board_max_sql = "select max(board_num)+1 from board";

        // 답변을 달 원문글 그룹 번호입니다.
        // 답변을 달게 되면 답변 글은 이 번호와 같은 관련글 번호를 갖게 처리되면서 같은 그룹에 속하게 됩니다.
        // 글목록에서 보여줄 때 하나의 그룹으로 묶여서 출력됩니다.
        int re_ref = board.getBoard_re_ref();

        // 답글의 깊이를 의미
        // 원문에 대한 답글이 출력될 대 한 번 들여쓰기 처리가 되고 답글에 대한 답글은 들여쓰기가 두번 처리되게 합니다.
        // 원문인 경우에는 이값이 0이고 원문의 답글은 1, 답글의 답글은 2가 됩니다.
        int re_lev = board.getBoard_re_lev();

        // 같은 관련 글 중에서 해당 글이 출력되는 순서입니다.
        int re_seq = board.getBoard_re_seq();

        try {
            conn = ds.getConnection();

            //트랜잭션을 이용하기 위해서 setAutoCommit을 false로 설정
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(board_max_sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                num = rs.getInt(1);
            }
            pstmt.close();

            // board_re_ref, board_re_seq 값을 확인하여 원문 글에 다른 답글이 있으면
            // 다른 답글들의 board_re_seq 값을 1씩 증가시킵니다.
            // 현재 글을 다른 답글보다 앞에 출력되게 하기 위해서 입니다.
            String sql = "update board set board_re_seq=board_re_seq + 1 " +
                    "where board_re_ref =? and board_re_seq > ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, re_ref);
            pstmt.setInt(2, re_seq);
            pstmt.executeUpdate();
            pstmt.close();

            // 답변할 답변 글의 board_re_lev, board_re_seq 값을 원문 글보다 1씩 증가시킵니다.
            re_seq = re_seq + 1;
            re_lev = re_lev + 1;

            String sql2 = "insert into board (board_num, board_name, board_pass, board_subject, board_content, board_file, board_re_ref, board_re_lev, board_re_seq, board_readcount) " +
                    " values(" + num + ", ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql2);
            pstmt.setString(1, board.getBoard_name());
            pstmt.setString(2, board.getBoard_pass());
            pstmt.setString(3, board.getBoard_subject());
            pstmt.setString(4, board.getBoard_content());
            pstmt.setString(5, ""); //답변에는 파일을 업로드 X
            pstmt.setInt(6, re_ref); // 원문의 글번호
            pstmt.setInt(7, re_lev);
            pstmt.setInt(8, re_seq);
            pstmt.setInt(9, 0); // baord_readcount = 0
            if (pstmt.executeUpdate() == 1) {
                conn.commit(); // commit 합니다.
            } else {
                conn.rollback();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("boardReply() 에러 : " + ex);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return num;
    }

    public boolean boardUpdate(Board board) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            conn = ds.getConnection();

            System.out.println("쿼리문 실행 전");
            String sql = "update board set board_subject=?, board_content=? where board_num = ?";

            pstmt = conn.prepareStatement(sql);
            System.out.println("쿼리문 실행 후");

            pstmt.setString(1, board.getBoard_name());
            System.out.println("(DAO)getBoard_name : " + board.getBoard_name());
            pstmt.setString(2, board.getBoard_content());
            System.out.println("(DAO)getBoard_content : " + board.getBoard_content());
            pstmt.setInt(3, board.getBoard_num());
            System.out.println("(DAO)getBoard_num : " + board.getBoard_num());
            result = pstmt.executeUpdate();
            System.out.println("*************" + result);

            if (result == 1) {
                System.out.println("데이터 수정이 모두 완료되었습니다.");
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
        return true;
    }

    public boolean isBoardWriter(int num, String pass) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean result = false;
        String board_sql = "select BOARD_PASS from board where BOARD_NUM=?";
        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(board_sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if(pass.equals(rs.getString("board_pass"))) {
                    result = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("isBoardWriter() 에러 : " + ex);
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
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    public boolean boardModify(Board board) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "update board set board_subject=?, board_content=?, board_file=? where board_num=? ";
        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, board.getBoard_subject());
            pstmt.setString(2, board.getBoard_content());
            pstmt.setString(3, board.getBoard_file());
            pstmt.setInt(4, board.getBoard_num());
            int result = pstmt.executeUpdate();
            if (result == 1) {
                System.out.println("업데이트 성공");
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean boardDelete(int num) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        String select_sql = "select board_re_ref, board_re_lev, board_re_seq from board"
                + " where board_num=?";
        String board_delete_sql = "delete from board "
                + "where board_re_ref=? and board_re_lev >=? and board_re_seq >=? "
                + "and board_re_seq<=(nvl((select min(board_re_seq)-1 from board "
                + "where board_re_ref=? and board_re_lev=? and board_re_seq>?) , "
                + " (select max(board_re_seq) from board where board_re_ref=? )))";
        boolean result_check = false;
        try {
            conn = ds.getConnection();
            pstmt=conn.prepareStatement(select_sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                pstmt2 = conn.prepareStatement(board_delete_sql);
                pstmt2.setInt(1, rs.getInt("board_re_ref"));
                pstmt2.setInt(2, rs.getInt("board_re_lev"));
                pstmt2.setInt(3, rs.getInt("board_re_seq"));
                pstmt2.setInt(4, rs.getInt("board_re_ref"));
                pstmt2.setInt(5, rs.getInt("board_re_lev"));
                pstmt2.setInt(6, rs.getInt("board_re_seq"));
                pstmt2.setInt(7, rs.getInt("board_re_ref"));

                int count=pstmt2.executeUpdate();

                if(count>=1)
                    result_check = true; // 삭제가 안된 경우에는 false를 반환
            }
        } catch (Exception ex) {
            System.out.println("boardDelete() 에러 : " + ex);
            ex.printStackTrace();
        } finally {
            if(rs!=null)
                try{
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            if(pstmt !=null)
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            if(pstmt2 != null)
                try {
                    pstmt2.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result_check;
    }
}
