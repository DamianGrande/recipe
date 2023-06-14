create database sfg_dev;
create database sfg_prod;

create user 'sfg_dev_user'@'localhost' identified by 'guru';
create user 'sfg_prod_user'@'localhost' identified by 'guru';

grant select, insert, delete, update on sfg_dev.* to 'sfg_dev_user'@'localhost';
grant select, insert, delete, update on sfg_prod.* to 'sfg_prod_user'@'localhost';