
# --- !Ups
CREATE TABLE collectors (
    id bigint NOT NULL,
    url varchar(255) NOT NULL,
    period int DEFAULT 60000,
    CONSTRAINT pk_collectors PRIMARY KEY (id)
);

CREATE SEQUENCE collectors_pk_seq;

ALTER TABLE collectors ALTER COLUMN id SET DEFAULT nextval('collectors_pk_seq'::regclass);

CREATE TABLE currency_data (
    id bigint NOT NULL,
    currency_name varchar(255) NOT NULL,
    date_changed bigint NOT NULL,
    value double precision NOT NULL,
    CONSTRAINT pk_currency_data PRIMARY KEY (id)
);

CREATE SEQUENCE currency_data_pk_seq;

ALTER TABLE currency_data ALTER COLUMN id SET DEFAULT nextval('currency_data_pk_seq'::regclass);

# --- !Downs

DROP TABLE collectors;
DROP TABLE currency_data;

DROP SEQUENCE collectors_pk_seq;
DROP SEQUENCE currency_data_pk_seq;