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

INSERT INTO canal(empresa_id, nombre, estado) VALUES (1,'TELEMARKETING', 'ACTIVO');
INSERT INTO canal(empresa_id, nombre, estado) VALUES (1,'FUERZA DE VENTAS', 'ACTIVO');
INSERT INTO canal(empresa_id, nombre, estado) VALUES (1,'ASOCIACIONES', 'ACTIVO');

INSERT INTO plangastos(creacion, estado, nombre, empresa_id) VALUES ( now(),0, 'PLAN NORMAL - CONSUMO',1);
INSERT INTO plangastos(creacion, estado, nombre, empresa_id) VALUES ( now(),0, 'PLAN ASOCIACIONES - CUOTA 125.000 X MILLON"',1);

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
INSERT INTO detplangastos(tasa, plazo, porcentanjegastos, porcentanjecomision, plangastos_id) VALUES(32,360,15,10.0153,2);

INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('COBRO DE CUOTA','ACTIVO','ENTRADA',true);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('DESEMBOLSO PRESTAMO','ACTIVO','SALIDA',true);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('PAGO A PROVEEDOR','ACTIVO','SALIDA',false);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('ADELANTO','ACTIVO','SALIDA',false);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('INGRESO DE EFECTIVO','ACTIVO','ENTRADA',false);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('AJUSTE DE SALDO POSITIVO','ACTIVO','ENTRADA',true);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('AJUSTE DE SALDO NEGATIVO','ACTIVO','SALIDA',true);


-- DROP TABLE IF  EXISTS version_app;

-- CREATE TABLE version_app
-- (
--   id serial NOT NULL,
--   ultimavista text,
--   version text,
--   CONSTRAINT version_app_pk PRIMARY KEY (id)
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- 
-- insert into version_app ( ultimavista,version) values('vista_2','1.0.1');


-- 14/04/2015 

--ALTER TABLE sesiontpv add column cerrarConDiferencia boolean;

-- 17/04/2015 
--alter table pago add column descripcion character varying(255);
--ALTER TABLE  pago add column montopagado numeric(38,0);
--ALTER TABLE  pago add column cambio numeric(38,0);

-- 24/04/2015 

-- ALTER TABLE pago ADD COLUMN transaccion_id bigint;
-- 
-- ALTER TABLE pago
--   ADD CONSTRAINT fk_pago_transaccion_id FOREIGN KEY (transaccion_id)
--       REFERENCES transaccion (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;
-- 
-- 14/05/2015
-- Table: auditoria

-- DROP TABLE auditoria;

-- CREATE TABLE auditoria
-- (
--   id serial NOT NULL,
--   fecha timestamp without time zone,
--   tablaafectada character varying(255),
--   tipooperacion character varying(255),
--   usuariologeado character varying(255),
--   CONSTRAINT auditoria_pkey PRIMARY KEY (id)
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE auditoria
--   OWNER TO postgres;
-- 
-- -- Table: banco
-- 
-- -- DROP TABLE banco;
-- 
-- CREATE TABLE banco
-- (
--   id serial NOT NULL,
--   nombre character varying(255),
--   CONSTRAINT banco_pkey PRIMARY KEY (id)
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE banco
--   OWNER TO postgres;
-- 
-- -- Table: estadocheque
-- 
-- -- DROP TABLE estadocheque;
-- 
-- CREATE TABLE estadocheque
-- (
--   id serial NOT NULL,
--   nombre character varying(255),
--   CONSTRAINT estadocheque_pkey PRIMARY KEY (id)
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE estadocheque
--   OWNER TO postgres;

-- 
-- -- Table: cheque
-- 
-- -- DROP TABLE cheque;
-- 
-- CREATE TABLE cheque
-- (
--   id serial NOT NULL,
--   emision date,
--   monto numeric(38,0),
--   numero character varying(255),
--   tipocheque character varying(255),
--   vencimiento date,
--   banco_id bigint,
--   estado_id bigint,
--   moneda_id bigint,
--   CONSTRAINT cheque_pkey PRIMARY KEY (id),
--   CONSTRAINT fk_cheque_banco_id FOREIGN KEY (banco_id)
--       REFERENCES banco (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_cheque_estado_id FOREIGN KEY (estado_id)
--       REFERENCES estadocheque (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_cheque_moneda_id FOREIGN KEY (moneda_id)
--       REFERENCES moneda (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE cheque
--   OWNER TO postgres;
--   
-- -- Table: cuentabancaria
-- 
-- -- DROP TABLE cuentabancaria;
-- 
-- CREATE TABLE cuentabancaria
-- (
--   id serial NOT NULL,
--   borrado boolean,
--   creacion timestamp without time zone,
--   denominacion character varying(255),
--   estado character varying(255),
--   numero character varying(255),
--   saldoreal numeric(38,0),
--   saldoteorico numeric(38,0),
--   banco_id bigint,
--   moneda_id bigint,
--   CONSTRAINT cuentabancaria_pkey PRIMARY KEY (id),
--   CONSTRAINT fk_cuentabancaria_banco_id FOREIGN KEY (banco_id)
--       REFERENCES banco (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_cuentabancaria_moneda_id FOREIGN KEY (moneda_id)
--       REFERENCES moneda (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE cuentabancaria
--   OWNER TO postgres;

-- ALTER TABLE pago ADD COLUMN estadocheque character varying(255);
-- ALTER TABLE pago ADD COLUMN librador character varying(255);
-- ALTER TABLE pago ADD COLUMN numero character varying(255);
-- ALTER TABLE pago ADD COLUMN vencimiento date;
-- ALTER TABLE pago ADD COLUMN banco_id bigint;
-- ALTER TABLE pago ADD COLUMN cambio numeric(38,0);
-- ALTER TABLE pago ADD COLUMN montopagado numeric(38,0);
-- 
-- -- Foreign Key: fk_pago_banco_id
-- 
-- -- ALTER TABLE pago DROP CONSTRAINT fk_pago_banco_id;
-- 
-- ALTER TABLE pago
--   ADD CONSTRAINT fk_pago_banco_id FOREIGN KEY (banco_id)
--       REFERENCES banco (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;
-- 
-- -- Foreign Key: fk_pago_transaccion_id
-- 
-- -- ALTER TABLE pago DROP CONSTRAINT fk_pago_transaccion_id;
-- 
-- ALTER TABLE pago
--   ADD CONSTRAINT fk_pago_transaccion_id FOREIGN KEY (transaccion_id)
--       REFERENCES transaccion (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;

-- ALTER TABLE facturaventadetalle ADD COLUMN prestamo_id bigint;
-- ALTER TABLE facturaventadetalle
--   ADD CONSTRAINT fk_facturaventadetalle_prestamo_id FOREIGN KEY (prestamo_id)
--       REFERENCES prestamo (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;

-- -- Foreign Key: fk_transaccion_tipotransaccioncaja_id
-- 
-- -- ALTER TABLE transaccion DROP CONSTRAINT fk_transaccion_tipotransaccioncaja_id;
-- 
-- ALTER TABLE transaccion
--   ADD CONSTRAINT fk_transaccion_tipotransaccioncaja_id FOREIGN KEY (tipotransaccioncaja_id)
--       REFERENCES tipotransaccioncaja (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;

-- -- Column: totalpagado
-- 
-- -- ALTER TABLE facturaventa DROP COLUMN totalpagado;
-- 
-- ALTER TABLE facturaventa ADD COLUMN totalpagado numeric(38,0);
-- 
-- -- Foreign Key: fk_puntoventa_usuario_id
-- 
-- -- ALTER TABLE puntoventa DROP CONSTRAINT fk_puntoventa_usuario_id;
-- 
-- ALTER TABLE puntoventa
--   ADD CONSTRAINT fk_puntoventa_usuario_id FOREIGN KEY (usuario_id)
--       REFERENCES persona (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;

insert into rol (nombre, estado, version) values ('tesoreria_configuracion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_cobracuota','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_desembolsa','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_cierre','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_transferencia','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_canal_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_canal_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_canal_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_canal_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_canal_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_canal_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_categoria_list_accion_eliminar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_list_accion_crea','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cliente_view_accion_imprimeregistrofirma','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_cobrador_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_empresa_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_plangastos_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_sucursal_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_tipotransaccioncaja_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_tipotransaccioncaja_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_tipotransaccioncaja_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_tipotransaccioncaja_listado_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_cambiarclave','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_usuario_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_list_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_list_accion_ver','ACTIVO',1);
insert into rol (nombre, estado, version) values ('adm_vendedor_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('cobracuota_cobracuota','ACTIVO',1);
insert into rol (nombre, estado, version) values ('cobracuota_creafactura','ACTIVO',1);
insert into rol (nombre, estado, version) values ('cobracuota_formacancelacion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_diario_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_diario_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_diario_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_diario_listado_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_facturaventa_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_facturaventa_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_facturaventa_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('contabilidad_facturaventa_view','ACTIVO',1);
insert into rol (nombre, estado, version) values ('desembolsa_creafactura','ACTIVO',1);
insert into rol (nombre, estado, version) values ('desembolsa_desembolsa','ACTIVO',1);
insert into rol (nombre, estado, version) values ('desembolsa_resumen','ACTIVO',1);
insert into rol (nombre, estado, version) values ('finanzas_cuentabancaria_cuerpo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('finanzas_cuentabancaria_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('finanzas_cuentabancaria_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('finanzas_cuentabancaria_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('home_home','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_configuracion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_contabilidad','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_finanza','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_informe','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_prestamo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_puntoventa','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_terminalcaja','ACTIVO',1);
insert into rol (nombre, estado, version) values ('menus_tesoreria','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_informes_listadopresamo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_create','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_desembolsar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_edit','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_editdescuento','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_agendacobros','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_estadocuenta','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_create_accion_ajustar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_edit_accion_ajustar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_editar','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_aplicardescuento','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_imprimirpagare','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_imprimirliqprestamo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_imprimirdetcliente','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_pasaraendesembolso','ACTIVO',1);
insert into rol (nombre, estado, version) values ('prestamos_prestamo_list_accion_pasarapendientedesembolso','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_admin_graficodisponibilidad','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_admin_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_metodopago_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_metodopago_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_metodopago_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_metodopago_listado_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_metodopago_listado_accion_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_secuencia_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_secuencia_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_secuencia_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_secuencia_listado_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_cierre','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_terminalcaja','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_transferencia','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_listado_accion_iniciosesion','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_listado_accion_imprimirreportecajacierre','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_listado_accion_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_terminalcaja_accion_desembolsa','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_terminalcaja_accion_cobracuota','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_terminalcaja_accion_transferencia','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_sesiontpv_terminalcaja_accion_cierre','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_terminal_edita','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_terminal_listado','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_terminal_nuevo','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_terminal_listado_crear','ACTIVO',1);
insert into rol (nombre, estado, version) values ('tesoreria_terminal_listado_edita','ACTIVO',1);

--29/05/2015
--ChequeRecibido
--agregar chequeRecibido
--CuentaBancaria
--BoletaDeposito
--TransaccionBancaria //Padre
--TranssaccionDepositoEfectivo extiende a Padre

-- -- Table: transaccionbancaria
-- 
-- -- DROP TABLE transaccionbancaria;
-- 
-- CREATE TABLE transaccionbancaria
-- (
--   id serial NOT NULL,
--   dtype character varying(31),
--   borrado boolean,
--   codref character varying(255),
--   descripcion character varying(255),
--   fecha timestamp without time zone,
--   monto numeric(38,0),
--   tipo integer,
--   usuario character varying(255),
--   cotizacion_id bigint,
--   cuentabancaria_id bigint,
--   moneda_id bigint,
--   comprobante character varying(255),
--   CONSTRAINT transaccionbancaria_pkey PRIMARY KEY (id),
--   CONSTRAINT fk_transaccionbancaria_cotizacion_id FOREIGN KEY (cotizacion_id)
--       REFERENCES cotizacion (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_transaccionbancaria_cuentabancaria_id FOREIGN KEY (cuentabancaria_id)
--       REFERENCES cuentabancaria (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_transaccionbancaria_moneda_id FOREIGN KEY (moneda_id)
--       REFERENCES moneda (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE transaccionbancaria
--   OWNER TO postgres;

-- -- Table: cheque
-- 
-- -- DROP TABLE cheque;
-- 
-- CREATE TABLE cheque
-- (
--   id serial NOT NULL,
--   emision date,
--   monto numeric(38,0),
--   numero character varying(255),
--   tipocheque character varying(255),
--   vencimiento date,
--   banco_id bigint,
--   estado_id bigint,
--   moneda_id bigint,
--   CONSTRAINT cheque_pkey PRIMARY KEY (id),
--   CONSTRAINT fk_cheque_banco_id FOREIGN KEY (banco_id)
--       REFERENCES banco (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_cheque_estado_id FOREIGN KEY (estado_id)
--       REFERENCES estadocheque (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_cheque_moneda_id FOREIGN KEY (moneda_id)
--       REFERENCES moneda (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE cheque
--   OWNER TO postgres;
-- 
-- 02-06-2015
--ALTER TABLE boletadeposito ADD COLUMN estado integer;


--06-06-2015

-- DROP TABLE cheque;
-- CREATE TABLE chequeemitido
-- (
--   id serial NOT NULL,
--   beneficiario character varying(255),
--   emision date,
--   monto numeric(38,0),
--   numero character varying(255),
--   tipocheque character varying(255),
--   vencimiento date,
--   cuentabancaria_id bigint,
--   estado_id bigint,
--   moneda_id bigint,
--   secuencia_id bigint,
--   CONSTRAINT chequeemitido_pkey PRIMARY KEY (id),
--   CONSTRAINT fk_chequeemitido_cuentabancaria_id FOREIGN KEY (cuentabancaria_id)
--       REFERENCES cuentabancaria (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_chequeemitido_estado_id FOREIGN KEY (estado_id)
--       REFERENCES estadocheque (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_chequeemitido_moneda_id FOREIGN KEY (moneda_id)
--       REFERENCES moneda (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION,
--   CONSTRAINT fk_chequeemitido_secuencia_id FOREIGN KEY (secuencia_id)
--       REFERENCES secuencia (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION
-- )
-- WITH (
--   OIDS=FALSE
-- );
-- ALTER TABLE chequeemitido
--   OWNER TO postgres;
-- 
-- 
-- ALTER TABLE secuencia ADD COLUMN serie character varying(255);
-- ALTER TABLE secuencia ADD COLUMN cuentabancaria_id bigint;
-- ALTER TABLE secuencia
--   ADD CONSTRAINT fk_secuencia_cuentabancaria_id FOREIGN KEY (cuentabancaria_id)
--       REFERENCES cuentabancaria (id) MATCH SIMPLE
--       ON UPDATE NO ACTION ON DELETE NO ACTION;

--ALTER TABLE banco ADD COLUMN estado character varying(255);