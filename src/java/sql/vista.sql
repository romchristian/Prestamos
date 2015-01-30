insert into rol (nombre, estado, version) values ('admin','ACTIVO',1);
insert into rol (nombre, estado, version) values ('cliente_lectura','ACTIVO',1);
insert into rol (nombre, estado, version) values ('cliente_edicion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_lectura','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_edicion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_imprime_pagare','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_confirma_pagare','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_cancela_pagare','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_desembolsa','ACTIVO',1);
insert into rol (nombre, estado, version) values ('cobro_cuota','ACTIVO',1);
insert into rol (nombre, estado, version) values ('configuraciones','ACTIVO',1);


insert into persona(dtype,estado,nrodocumento,usuario,clave,nombre) values('Usuario','ACTIVO','','admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','Administrador');
insert into persona_rol values(1,1);
CREATE OR REPLACE VIEW groups AS select cast(lower(r.nombre) as character varying(255)) AS groupid, p.usuario AS userid from persona_rol pr join rol r on r.id = pr.roles_id join persona p on p.id = pr.usuarios_id;
ALTER TABLE groups OWNER TO postgres;
CREATE OR REPLACE VIEW users AS SELECT usuario AS userid, clave AS password FROM persona where dtype = 'Usuario';
ALTER TABLE users OWNER TO postgres;
