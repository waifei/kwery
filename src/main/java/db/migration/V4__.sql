create table job_rule (
  id integer generated by default as identity (START WITH 100, INCREMENT BY 1),
  r_name varchar(255) not null,
  value varchar(255) not null,
  job_id_fk integer not null,
  primary key (id)
);

alter table job_rule add constraint fk_job_rule_job_id_fk foreign key (job_id_fk) references job;
alter table job_rule add constraint uc_job_rule_r_name_job_id_fk unique(r_name, job_id_fk);