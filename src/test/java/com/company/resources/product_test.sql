CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.product (
    id serial PRIMARY KEY,
    brand_id INTEGER NOT NULL,
    title VARCHAR(255)
);