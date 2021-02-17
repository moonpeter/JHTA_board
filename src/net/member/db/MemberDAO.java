package net.member.db;

import net.board.db.Board;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public Member member_info(String id) {
        Member member = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ds.getConnection();

            String sql = "select * from MEMBER where id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                member = new Member();
                member.setId(rs.getString(1));
                member.setPassword(rs.getString(2));
                member.setName(rs.getString(3));
                member.setAge(rs.getInt(4));
                member.setGender(rs.getString(5));
                member.setEmail(rs.getString(6));
                member.setMemberfile(rs.getString(7));
            }
        } catch (Exception ex) {
            System.out.println("member_info() : 에러 " + ex);
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
        return member;
    }

    public int update(Member member) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        String sql = "update member set NAME=?, age=?, gender=?, EMAIL=?, memberfile=? where ID=? ";
        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getName());
            pstmt.setInt(2, member.getAge());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getMemberfile());
            pstmt.setString(6, member.getId());
            result = pstmt.executeUpdate();
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
        return result;
    }


    public int getListCount() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int x = 0;
        try {
            conn = ds.getConnection();
            pstmt = conn.prepareStatement("select count(*) from member where id != 'admin'");
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

    public int getListCount(String field, String value) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int x = 0;
        try {
            conn = ds.getConnection();
            String sql = "select count(*) from member where id != 'admin' and " + field + " like ? ";
            System.out.println(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + value + "%");
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

    public List<Member> getMemberList(int page, int limit) {
        List<Member> list = new ArrayList<Member>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ds.getConnection();

            String member_list_sql = "select * from(select b.*, rownum rnum " +
                    "from (select * from member where id != 'admin' order by id) b) where rnum>=? and rnum<=? ";

            pstmt = conn.prepareStatement(member_list_sql);
            // 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
            int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호(1 11 21 31 ...
            int endrow = startrow + limit - 1;     // 읽을 마지막 row 번호(10 20 30 40 ...)
            pstmt.setInt(1, startrow);
            pstmt.setInt(2, endrow);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPassword(rs.getString(2));
                member.setName(rs.getString(3));
                member.setAge(rs.getInt(4));
                member.setGender(rs.getString(5));
                member.setEmail(rs.getString(6));
                list.add(member);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getMemberList() 에러 : " + ex);
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

    public List<Member> getMemberList(String field, String value, int page, int limit) {
        List<Member> list = new ArrayList<Member>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ds.getConnection();

            String sql = "select * from(select b.*, rownum rnum " +
                    "from (select * from member where id != 'admin' and " + field + " like ? order by id) b) where rnum between ? and ?";
            System.out.println(sql);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + value + "%");
            // 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
            int startrow = (page - 1) * limit + 1; // 읽기 시작할 row 번호(1 11 21 31 ...
            int endrow = startrow + limit - 1;     // 읽을 마지막 row 번호(10 20 30 40 ...)
            pstmt.setInt(2, startrow);
            pstmt.setInt(3, endrow);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPassword(rs.getString(2));
                member.setName(rs.getString(3));
                member.setAge(rs.getInt(4));
                member.setGender(rs.getString(5));
                member.setEmail(rs.getString(6));
                list.add(member);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("getMemberList() 에러 : " + ex);
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

    public int memberDelete(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            conn = ds.getConnection();
            String delete_sql = "delete from MEMBER where id = ?";
            pstmt = conn.prepareStatement(delete_sql);
            pstmt.setString(1, id);
            result = pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println("memberDelete() 에러 : " + ex);
            ex.printStackTrace();
        } finally {
            if (pstmt != null)
                try {
                    pstmt.close();
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
        return result;
    }

    public int isId(String id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = -1;
        try {
            conn = ds.getConnection();

            String sql = "select id from member where id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = 0;
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
            return result;
        }
    }
}