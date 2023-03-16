create table devices(
    id serial primary key not null,
    name varchar(50) not null,
    device_type varchar(50) not null,
    uuid UUID not null,
    users_id int not null
--     constraint fk_users foreign key (users_id) references users(id)
);

INSERT INTO devices (name, device_type, uuid, users_id) values
                                                        ('User phone', 'SMARTPHONE', 'b5b18cb5-0782-467d-b838-50c8e5b0d889', 1),
                                                        ('User pc', 'PC', 'b5b18cb5-0782-467d-b838-50c8e5b0d889', 1),
                                                        ('Admin notebook', 'NOTEBOOK', 'b5b18cb5-0782-467d-b838-50c8e5b0d889', 2),
                                                        ('Admin pc', 'PC', 'b5b18cb5-0782-467d-b838-50c8e5b0d889', 2)

