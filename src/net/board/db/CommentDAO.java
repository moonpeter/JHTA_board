package net.board.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentDAO {
    private DataSource ds;

    public CommentDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
        } catch (Exception ex) {
            System.out.println("DB 연결 실패" + ex);
            return;
        }
    }

    public int getListcount(int board_re_ref) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int x = 0;
        String sql = "select count(*) from comm where comment_board_num = ?";
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, board_re_ref);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                x = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return x;
    }

    public JsonArray getCommentList(int comment_board_num, int state) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sort = "asc";
        if (state == 2) {
            sort = "desc";
        }
        String sql = "select * from (select b.*, rownum rnum "
                + " from(select num, comm.id, content, reg_date, comment_re_lev, comment_re_seq, comment_re_ref, member.memberfile "
                + " from comm join member "
                + " on comm.id=member.id "
                + " where comment_board_num = ? "
                + " order by comment_re_ref " + sort + ", "
                + " comment_re_seq asc)b)";
        JsonArray array = new JsonArray();
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, comment_board_num);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                JsonObject object = new JsonObject();
                object.addProperty("num", rs.getInt(1));
                object.addProperty("id", rs.getString(2));
                object.addProperty("content", rs.getString(3));
                object.addProperty("reg_date", rs.getString(4));
                object.addProperty("comment_re_lev", rs.getInt(5));
                object.addProperty("comment_re_seq", rs.getInt(6));
                object.addProperty("comment_re_ref", rs.getInt(7));
                object.addProperty("memberfile", rs.getString(8));
                array.add(object);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getCommentList() 에러 : " + ex);
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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return array;
    }

    public int commentInsert(Comment co) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            con = ds.getConnection();

            String sql = "insert into comm "
                    + " values(com_seq.nextval, ?, ? ,sysdate, ?, ?, ?, com_seq.nextval)";

            // 새로운 글을 등록하는 부분입니다.
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, co.getId());
            pstmt.setString(2, co.getContent());
            pstmt.setInt(3, co.getComment_board_num());
            pstmt.setInt(4, co.getComment_re_lev());
            pstmt.setInt(5, co.getComment_re_seq());

            result = pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("commentInsert() 에러 : " + ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    public int commentUpdate(Comment co) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            con = ds.getConnection();

            String sql = "update comm set content = ? where num = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, co.getContent());
            pstmt.setInt(2, co.getNum());

            result = pstmt.executeUpdate();
            if (result == 1) {
                System.out.println("데이터 수정 되었습니다.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("commentUpdate() 에러 : " + ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    public int commentDelete(int num) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            con = ds.getConnection();

            String sql = "delete from comm where num = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, num);

            result = pstmt.executeUpdate();
            if (result == 1) {
                System.out.println("데이터 삭제가 완료되었습니다.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("commentDelete() 에러 : " + ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    public int commentReply(Comment co) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            StringBuilder update_sql = new StringBuilder();
            update_sql.append("update comm ");
            update_sql.append("set comment_re_seq=comment_re_seq+1 ");
            update_sql.append("where comment_re_ref=? ");
            update_sql.append("and comment_re_seq> ? ");
            pstmt = con.prepareStatement(update_sql.toString());
            pstmt.setInt(1, co.getComment_re_ref());
            pstmt.setInt(2, co.getComment_re_seq());
            pstmt.executeUpdate();
            pstmt.close();

            String sql = "insert into comm "
                    + " values(com_seq.nextval, ?, ? ,sysdate, ?, ?, ?, ?)";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, co.getId());
            pstmt.setString(2, co.getContent());
            pstmt.setInt(3, co.getComment_board_num());
            pstmt.setInt(4, co.getComment_re_lev() +1);
            pstmt.setInt(5, co.getComment_re_seq() +1);
            pstmt.setInt(6, co.getComment_re_ref());
            result = pstmt.executeUpdate();
            if(result==1){
                System.out.println("reply 삽입 완료되었습니다.");
                con.commit();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("commentReply() 에러 : " + ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }
}
