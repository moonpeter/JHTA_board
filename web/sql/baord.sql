drop table board cascade constraints;

CREATE TABLE board(
    board_num       number,
    board_name      varchar2(30),
    board_pass      varchar2(30),
    board_subject   varchar2(300),
    board_content   varchar2(4000),
    board_file      varchar2(50),
    board_re_ref    number,
    board_re_lev    number,
    board_re_seq    number,
    board_readcount number,
    board_date      date default sysdate,
    PRIMARY KEY(board_num)
);

-- insert into board(board_num, board_name, board_pass, board_subject, board_content, board_file)  values(1, 'test', 'test', 'test', 'test', 'test');

select nvl(max(board_num),0)+1 from board;

select * from board;