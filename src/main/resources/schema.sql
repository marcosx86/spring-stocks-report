DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS summaries;
DROP TABLE IF EXISTS taxes;
DROP TABLE IF EXISTS stocks;
DROP TABLE IF EXISTS crawler_config;

DROP SEQUENCE IF EXISTS seq_orders;
DROP SEQUENCE IF EXISTS seq_summary;
DROP SEQUENCE IF EXISTS seq_taxes;
DROP SEQUENCE IF EXISTS seq_stocks;
DROP SEQUENCE IF EXISTS seq_crawler_config;

CREATE TABLE orders (
	id integer NOT NULL,
	stock varchar(10) NOT NULL,
	date date NOT NULL,
	count integer NOT NULL,
	price number NOT NULL,
	ordertype varchar(1) NOT NULL
);
ALTER TABLE orders ADD CONSTRAINT pk_orders PRIMARY KEY (id);

CREATE TABLE summaries (
	id integer NOT NULL,
	stock varchar(10) NOT NULL,
	date date NOT NULL,
	count integer NOT NULL,
	avgprice number NOT NULL,
	daytrade boolean NOT NULL
);
ALTER TABLE summaries ADD CONSTRAINT pk_summaries PRIMARY KEY (id);

CREATE TABLE taxes (
	id integer NOT NULL,
	stock varchar(10) NOT NULL,
	date date NOT NULL,
	dueprice number NOT NULL
);
ALTER TABLE taxes ADD CONSTRAINT pk_taxes PRIMARY KEY (id);

CREATE TABLE stocks (
	id integer NOT NULL,
	stock varchar(10) NOT NULL,
	price number NOT NULL,
	updated date NOT NULL
);

CREATE TABLE crawler_config (
	id integer NOT NULL,
	url varchar(255) NOT NULL,
	selector varchar(255) NOT NULL,
	regex_matcher varchar(255) NOT NULL,
	regex_replace varchar(255) NOT NULL,
	last_success date NOT NULL,
	enabled boolean NOT NULL
);
ALTER TABLE crawler_config ADD CONSTRAINT pk_crawler_config PRIMARY KEY (id);

CREATE SEQUENCE seq_orders;
CREATE SEQUENCE seq_summary;
CREATE SEQUENCE seq_taxes;
CREATE SEQUENCE seq_stocks;
CREATE SEQUENCE seq_crawler_config;

DROP TABLE IF EXISTS scheduler_job_info;
CREATE TABLE scheduler_job_info (
	id bigint generated by default as identity,
	cron_expression varchar(255),
	cron_job boolean,
	job_class varchar(255),
	job_group varchar(255),
	job_name varchar(255),
	repeat_time bigint,
	primary key (id)
);
