-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE public.users
(
  id integer NOT NULL DEFAULT nextval('users_id_seq'::regclass),
  username character varying(128) COLLATE pg_catalog."default" NOT NULL,
  password character varying(128) COLLATE pg_catalog."default" NOT NULL,
  email character varying(255) COLLATE pg_catalog."default" NOT NULL,
  active boolean,
  storagename character varying(127) COLLATE pg_catalog."default" NOT NULL,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT users_email_key UNIQUE (email)

)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
  OWNER to postgres;
COMMENT ON TABLE public.users
IS 'Table of cloudrive users';