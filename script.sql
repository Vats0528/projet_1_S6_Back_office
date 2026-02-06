Create database projet_1_s6;
/c projet_1_s6;

create table test(
id int primary key,
text TEXT not null,
date date not null
);
insert into test values (1, 'Hello world', '2026-01-30');