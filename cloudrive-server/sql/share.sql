-- Table: public.share

-- DROP TABLE public.share;

CREATE TABLE public.share
(
  id integer NOT NULL,
  file integer NOT NULL,
  "user" integer NOT NULL,
  CONSTRAINT share_pkey PRIMARY KEY (id),
  CONSTRAINT share_file_fkey FOREIGN KEY (file)
  REFERENCES public.storage (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION,
  CONSTRAINT share_user_fkey FOREIGN KEY ("user")
  REFERENCES public.users (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.share
  OWNER to postgres;