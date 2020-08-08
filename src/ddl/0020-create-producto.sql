--liquibase formatted sql

--changeset carlos.oliveira:creacion-tabla-producto
create table producto (
    producto_id int primary key identity(1,1),
    codigo varchar(50) not null,
    nombre varchar(50) not null,
    categoria_id int not null,
    foreign key (categoria_id) references categoria(categoria_id)
);
--rollback drop table producto;