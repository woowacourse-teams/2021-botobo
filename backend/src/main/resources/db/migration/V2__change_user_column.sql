alter table user change github_id social_id varchar(255) not null;
alter table user add column bio varchar(255) not null;
alter table user add column social_type varchar(255);