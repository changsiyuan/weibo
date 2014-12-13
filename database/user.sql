-- Table: weibo."user"

-- DROP TABLE weibo."user";

CREATE TABLE weibo."user"
(
  user_id integer NOT NULL,
  user_name character varying(16),
  user_email character varying(32),
  user_describe character varying(64),
  user_passwd integer,
  CONSTRAINT user_pkey PRIMARY KEY (user_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE weibo."user"
  OWNER TO postgres;
