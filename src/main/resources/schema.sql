DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS summary;
DROP TABLE IF EXISTS taxes;
 
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

CREATE SEQUENCE seq_orders;
CREATE SEQUENCE seq_summary;
CREATE SEQUENCE seq_taxes;