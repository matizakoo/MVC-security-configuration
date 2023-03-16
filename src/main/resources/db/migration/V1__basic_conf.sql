create table users(
                      id serial primary key not null,
                      username varchar(255) not null,
                      password varchar(255) not null,
                      role varchar(10) not null
);

insert into users (username, password, role) values ('mati', '$2a$12$qeOvWz4g9SKGvnY9I/g/pOvet2yo2oWTq29827VLT5/qnhNHCYRe2', 'USER');
insert into users (username, password, role) values ('admin','$2a$10$/s2.0/M67GNg8l7Om2CL9.KfyiYwr80hOtzf313KzSpQVwH8WkwzK', 'ADMIN');