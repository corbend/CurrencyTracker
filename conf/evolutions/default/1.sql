
# --- !Ups
CREATE TABLE charts (
    id bigint NOT NULL,
    name varchar(255) NOT NULL,
    period int DEFAULT 60000,
    CONSTRAINT pk_charts PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE charts;