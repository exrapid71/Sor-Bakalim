# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                            bigint auto_increment not null,
  body                          TEXT,
  question_id                   bigint,
  user_id                       bigint,
  create_date                   datetime(6) not null,
  constraint pk_answer primary key (id)
);

create table question (
  id                            bigint auto_increment not null,
  title                         varchar(255) not null,
  body                          TEXT,
  user_id                       bigint,
  create_date                   datetime(6) not null,
  constraint pk_question primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  username                      varchar(255) not null,
  password                      varchar(255) not null,
  email                         varchar(255) not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  active                        integer not null,
  constraint uq_user_username unique (username),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id)
);

alter table answer add constraint fk_answer_question_id foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_answer_question_id on answer (question_id);

alter table answer add constraint fk_answer_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_answer_user_id on answer (user_id);

alter table question add constraint fk_question_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_question_user_id on question (user_id);


# --- !Downs

alter table answer drop foreign key fk_answer_question_id;
drop index ix_answer_question_id on answer;

alter table answer drop foreign key fk_answer_user_id;
drop index ix_answer_user_id on answer;

alter table question drop foreign key fk_question_user_id;
drop index ix_question_user_id on question;

drop table if exists answer;

drop table if exists question;

drop table if exists user;

