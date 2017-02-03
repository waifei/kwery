alter table smtp_configuration add column use_local_setting boolean;

create table url_setting (
  id integer generated by default as identity (START WITH 100, INCREMENT BY 1),
  scheme varchar(5) not null,
  domain varchar(253) not null,
  port integer not null check (port>=1),
  primary key (id)
);
