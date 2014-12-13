-- Table: weibo.weibo

-- DROP TABLE weibo.weibo;

CREATE TABLE weibo.weibo
(
  weibo_id integer NOT NULL,
  user_id integer,
  weibo_content character varying(128),
  weibo_time time without time zone,
  comment_number integer,
  CONSTRAINT weibo_pkey PRIMARY KEY (weibo_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE weibo.weibo
  OWNER TO postgres;
