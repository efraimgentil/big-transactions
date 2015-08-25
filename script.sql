-- RUN ALONE, BEFORE THE CREATE TABLE
create database test;
--

-- Connect to the test database and run the creation --
CREATE TABLE public.employee
(
  id bigserial NOT NULL,
  birth_date date,
  father_name character varying(255),
  mother_name character varying(255),
  name character varying(255),
  sex character varying(255),
  CONSTRAINT employee_pkey PRIMARY KEY (id)
);