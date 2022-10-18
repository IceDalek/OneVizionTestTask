-- liquibase formatted sql

-- changeset liquibase:1
create table book (
id serial not null,
title varchar(150) not null,
author varchar(150) not null,
description varchar(150),
constraint book_pk primary key (id)
);

-- changeset liquibase:2
insert into BOOK (id, title, author, description)
values (default, 'Crime and Punishment', 'F. Dostoevsky', null);
insert into BOOK (id, title, author, description)
values (default, 'Anna Karenina', 'L. Tolstoy', null);
insert into BOOK (id, title, author, description)
values (default, 'The Brothers Karamazov', 'F. Dostoevsky', null);
insert into BOOK (id, title, author, description)
values (default, 'War and Peace', 'L. Tolstoy', null);
insert into BOOK (id, title, author, description)
values (default, 'Dead Souls', 'N. Gogol', null);
