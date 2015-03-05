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
insert into rol (nombre, estado, version) values ('agenda_cobro','ACTIVO',1);
insert into rol (nombre, estado, version) values ('estado_cuenta','ACTIVO',1);
insert into rol (nombre, estado, version) values ('informes','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_imprime_liquididacion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_imprime_detalle_cliente','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_historico','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_ajuste','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_aplicar_descuentos','ACTIVO',1);
insert into rol (nombre, estado, version) values ('clientes_imprime_registro_firma','ACTIVO',1);


insert into persona(dtype,estado,nrodocumento,usuario,clave,nombre) values('Usuario','ACTIVO','','admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','Administrador');
insert into persona_rol values(1,1);
CREATE OR REPLACE VIEW groups AS select cast(lower(r.nombre) as character varying(255)) AS groupid, p.usuario AS userid from persona_rol pr join rol r on r.id = pr.roles_id join persona p on p.id = pr.usuarios_id;
ALTER TABLE groups OWNER TO postgres;
CREATE OR REPLACE VIEW users AS SELECT usuario AS userid, clave AS password FROM persona where dtype = 'Usuario';
ALTER TABLE users OWNER TO postgres;

INSERT INTO moneda(abreviacion, decimales, estado, monedalocal, nombre, version) VALUES ('GS', 0, 'ACTIVO', TRUE, 'GUARANIES', 1);
INSERT INTO empresa(razonsocial, ruc, dv,estado,version) VALUES ('CREDI PYMES S.A.', '80065663-6', '2', 'ACTIVO', 1);
INSERT INTO sucursal( estado, nombre, version, empresa_id) VALUES ('ACTIVO', 'MATRIZ',1,1);

INSERT INTO persona(dtype, estado, empresa_id,sucursal_id, apellidos, nombres) VALUES ('Vendedor','ACTIVO',1,1,'De Prueba','Vendedor');
INSERT INTO persona(dtype, estado, empresa_id,sucursal_id, apellidos, nombres) VALUES ('Cobrador','ACTIVO',1,1,'De Prueba','Cobrador');

INSERT INTO categoria(calificacion) VALUES (0);
INSERT INTO categoria(calificacion) VALUES (1);
INSERT INTO categoria(calificacion) VALUES (2);
INSERT INTO categoria(calificacion) VALUES (3);
INSERT INTO categoria(calificacion) VALUES (4);
INSERT INTO categoria(calificacion) VALUES (5);

INSERT INTO plangastos(creacion, estado, nombre, empresa_id) VALUES ( now(),0, 'PLAN NORMAL - CONSUMO',1);

INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(50.000099,360,15,21.3507,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(50,420,15,21.35085,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(50,390,15,21.35085,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,510,15,24.0543,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,120,15,10.6129,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,480,15,24.0543,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,180,15,10.6129,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(38,240,15,16.739,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(43,300,15,21.5126,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,60,15,10.6129,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,570,15,24.2592,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,660,15,24.2592,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,720,15,27.236,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,210,15,16.739,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(38,270,15,16.739,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,150,15,10.6129,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,690,15,24.2592,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,450,15,24.0543,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(43,330,15,21.5126,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,600,15,24.2592,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,630,15,24.2592,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,30,15,10.6129,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(35,90,15,10.6129,1);
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(51,540,15,24.2592,1);



