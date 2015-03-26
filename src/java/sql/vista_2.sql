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