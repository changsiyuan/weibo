-- Table: weibo.attention

-- DROP TABLE weibo.attention;

CREATE TABLE weibo.attention
(
  attention_id integer NOT NULL,
  user_id integer,
  attention_user_id integer,
  CONSTRAINT attention_pkey PRIMARY KEY (attention_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE weibo.attention
  OWNER TO postgres;
