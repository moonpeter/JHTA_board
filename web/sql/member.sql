drop table member;

create table member(
id          varchar2(12),
password    varchar2(10),
name        varchar2(15),
age         number(2),
gender      varchar2(6),
email       varchar2(30),
PRIMARY KEY(id)
);

-- insert into member(id, password, name)
-- values ('admin', '1234', '관리자');

select * from member;