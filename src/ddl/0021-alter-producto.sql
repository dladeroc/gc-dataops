--liquibase formatted sql

--changeset carlos.oliveira:agregar-columna-descripcion-en-producto
alter table producto add descripcion varchar(150);
--rollback alter table producto drop column descripcion;