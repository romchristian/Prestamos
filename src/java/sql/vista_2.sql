DROP TABLE IF  EXISTS version_app;

CREATE TABLE version_app
(
  id serial NOT NULL,
  ultimavista text,
  version text,
  CONSTRAINT version_app_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

insert into version_app ( ultimavista,version) values('vista_2','1.0.1');


 ALTER TABLE puntoventa ADD COLUMN usuario_id bigint;

 ALTER TABLE puntoventa ADD CONSTRAINT fk_puntoventa_usuario_id FOREIGN KEY (usuario_id)
      REFERENCES persona (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE TABLE tipotransaccioncaja
(
  id serial NOT NULL,
  descripcion character varying(255),
  estado character varying(255),
  tipotransaccion character varying(255),
  CONSTRAINT tipotransaccioncaja_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tipotransaccioncaja
  OWNER TO postgres;

alter table tipotransaccioncaja add column oculto boolean;
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('AJUSTE DE SALDO POSITIVO','ACTIVO','ENTRADA',true);
INSERT INTO tipotransaccioncaja(descripcion,estado,tipotransaccion,oculto) VALUES('AJUSTE DE SALDO NEGATIVO','ACTIVO','SALIDA',true);

ALTER TABLE transaccion ADD COLUMN usuariologeado character varying(255);
