insert into role (description,name) values ('Role Admin','ROLE_Admin');
insert into permission (description,name) values ('This permission is for admin','PERMISSION_Admin');

insert into role (description,name) values ('Role Student','ROLE_Student');
insert into permission (description,name) values ('This permission is for student','PERMISSION_Student');

insert into role (description,name) values ('Role College','ROLE_College');
insert into permission (description,name) values ('This permission is for College','PERMISSION_College');


insert into role_permission values (1,1);
insert into role_permission values (2,2);
insert into role_permission values (3,3);


insert into domain(id,createdTime,modifiedTime,code,contact_no,email,is_enabled,name) values(1,now(),now(),'D',0123456789,'s@g.com',1,'admin');

insert into college(id,code,name,totalSeat,allocated,availableSeat,freeseat) values(1,"a","A",5,3,2,2);

insert into college(id,code,name,totalSeat,allocated,availableSeat,freeseat) values(2,"b","B",4,2,2,2);

insert into college(id,code,name,totalSeat,allocated,availableSeat,freeseat) values(3,"c","C",10,6,4,4);
