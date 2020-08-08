--liquibase formatted sql

--changeset carlos.oliveira:creacion-tabla-proveedores
create table proveedores (
    proveedores_id int primary key identity(1,1),
    nombre_empresa varchar(50) not null,
    nombre_contacto varchar(50) not null,
);
--rollback drop table proveedores;