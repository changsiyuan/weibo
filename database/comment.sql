-- Table: weibo.comment

-- DROP TABLE weibo.comment;

CREATE TABLE weibo.comment
(
  comment_id integer NOT NULL,
  weibo_id integer,
  user_id integer,
  comment_content character varying(128),
  CONSTRAINT comment_pkey PRIMARY KEY (comment_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE weibo.comment
  OWNER TO postgres;
