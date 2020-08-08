--liquibase formatted sql

--changeset juan-perez:creacion-categoria
create table categoria (
    categoria_id int primary key identity(1,1),
    nombre varchar(50) not null,
    descripcion varchar(50) not null,
);
--rollback drop table categoria;