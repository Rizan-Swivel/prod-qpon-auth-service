SET sql_mode = '';

create database if not exists qpon_auth;

use qpon_auth;

create table if not exists resource (
   id int(11) not null auto_increment,
   isDefault tinyint(4) not null,
   name varchar(255) default null,
   primary key (id)
) engine=innodb;


create table if not exists  permission (
  id int(11) not null auto_increment,
  name varchar(512) default null,
  method varchar(100),
  resource_id int(11),
  primary key (id),
  unique key name (name),
  key resource_id (resource_id),
  constraint permission_resource foreign key (resource_id) references resource (id)
) engine=innodb;

create table if not exists role (
   id int(11) not null auto_increment,
   deletable tinyint(4) not null,
   name varchar(255) default null,
   primary key (id),
   unique key name (name)
) engine=innodb;


create table if not exists  user (
                                     id varchar (1024) not null,
                                     fullname varchar(100),
                                     email varchar(1024),
                                     password varchar(1024),
                                     mobileNo varchar (100),
                                     mobileNoAsUserName varchar (100),
                                     imageUrl varchar (1024),
                                     language varchar (1024),
                                     approvalStatus varchar (50),
                                     createdAt TIMESTAMP,
                                     updatedAt TIMESTAMP,
                                     enabled tinyint(4) not null,
                                     is_registered tinyint(4) not null,
                                     primary key (id),
                                     unique key mobileNo (mobileNo),
                                     unique key email (email),
                                     role_id int(11) not null,
                                     key role_id (role_id),
                                     constraint user_role foreign key (role_id) references role (id)
) engine=innodb;

create table  if not exists permission_role (
  permission_id int(11) default null,
  role_id int(11) default null,
  key permission_id (permission_id),
  key role_id (role_id),
  constraint permission_role_ibfk_1 foreign key (permission_id) references permission (id),
  constraint permission_role_ibfk_2 foreign key (role_id) references role (id)
) engine=innodb;


create table if not exists oauth_access_token (
                                                  token_id VARCHAR(256),
                                                  token LONG VARBINARY,
                                                  authentication_id VARCHAR(256) PRIMARY KEY,
                                                  user_name VARCHAR(256),
                                                  client_id VARCHAR(256),
                                                  authentication LONG VARBINARY,
                                                  refresh_token VARCHAR(256)
);


create table if not exists oauth_access_token (
                                                  token_id VARCHAR(256),
                                                  token LONG VARBINARY,
                                                  authentication_id VARCHAR(256) PRIMARY KEY,
                                                  user_name VARCHAR(256),
                                                  client_id VARCHAR(256),
                                                  authentication LONG VARBINARY,
                                                  refresh_token VARCHAR(256)
);

create table if not exists  oauth_client_details (
                                                     client_id varchar(255) not null,
                                                     client_secret varchar(255) not null,
                                                     web_server_redirect_uri varchar(2048) default null,
                                                     scope varchar(255) default null,
                                                     access_token_validity int(11) default null,
                                                     refresh_token_validity int(11) default null,
                                                     resource_ids varchar(1024) default null,
                                                     authorized_grant_types varchar(1024) default null,
                                                     authorities varchar(1024) default null,
                                                     additional_information varchar(4096) default null,
                                                     autoapprove varchar(255) default null,
                                                     primary key (client_id)
);

create table if not exists oauth_approvals (
                                               userId VARCHAR(256),
                                               clientId VARCHAR(256),
                                               scope VARCHAR(256),
                                               status VARCHAR(10),
                                               expiresAt TIMESTAMP,
                                               lastModifiedAt TIMESTAMP
);


create table if not exists oauth_code (
                                          code VARCHAR(256),
                                          authentication LONG VARBINARY
);

create table if not exists oauth_refresh_token (
                                                   token_id VARCHAR(256),
                                                   token LONG VARBINARY,
                                                   authentication LONG VARBINARY
);

create table if not exists oauth_client_token (
                                                  token_id VARCHAR(256),
                                                  token LONG VARBINARY,
                                                  authentication_id VARCHAR(256) PRIMARY KEY,
                                                  user_name VARCHAR(256),
                                                  client_id VARCHAR(256)
);


create table if not exists user_mobile_no (
                                                user_id VARCHAR(256) NOT NULL,
                                                mobileNo VARCHAR (100) NOT NULL,
                                                createdAt TIMESTAMP,
                                                primary key (user_id),
                                                unique key mobileNo (mobileNo)
);

INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids,
authorized_grant_types, additional_information)
VALUES ('tokomobile', '$2y$12$ZwonUte3P7EmlwXHhxrly.egnEv8IzaDrburGbCpIxP2296fw6FAm',
 'http://localhost:9191/login',
 'READ,WRITE,UPDATE', '3600', '10000',
 'tokomobile,oauth2-resource',
 'authorization_code,password,refresh_token,implicit', '{}');

INSERT INTO role (id, deletable, name) VALUES ('1', false, 'ADMIN');
INSERT INTO role (id, deletable, name) VALUES ('2', false, 'USER');
INSERT INTO role (id, deletable, name) VALUES ('3', false, 'MERCHANT');
INSERT INTO role (id, deletable, name) VALUES ('4', false, 'BANK');

