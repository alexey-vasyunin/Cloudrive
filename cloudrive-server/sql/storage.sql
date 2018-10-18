-- Table: public.storage

-- DROP TABLE public.storage;

CREATE TABLE public.storage
(
  id integer NOT NULL DEFAULT nextval('storage_id_seq'::regclass),
  owner integer NOT NULL,
  path character varying(1024) COLLATE pg_catalog."default",
  filename character varying(255) COLLATE pg_catalog."default" NOT NULL,
  CONSTRAINT storage_pkey PRIMARY KEY (id),
  CONSTRAINT storage_owner_fkey FOREIGN KEY (owner)
  REFERENCES public.users (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.storage
  OWNER to postgres;