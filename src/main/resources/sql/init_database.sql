ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS pk_orders_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.orders DROP CONSTRAINT IF EXISTS fk_user_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.suppliers DROP CONSTRAINT IF EXISTS pk_suppliers_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.product_categories DROP CONSTRAINT IF EXISTS pk_product_categories_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS pk_products_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk_product_category_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products DROP CONSTRAINT IF EXISTS fk_supplier_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products_of_order DROP CONSTRAINT IF EXISTS pk_products_of_order_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products_of_order DROP CONSTRAINT IF EXISTS fk_order_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.products_of_order DROP CONSTRAINT IF EXISTS fk_product_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS pk_users_id CASCADE;


DROP TABLE IF EXISTS public.orders;
CREATE TABLE orders(
  id SERIAL NOT NULL,
  user_id INTEGER NOT NULL,
  order_time TIMESTAMP NOT NULL DEFAULT NOW(),
  status CHARACTER VARYING(255) DEFAULT 'new'
);

DROP TABLE IF EXISTS public.suppliers;
CREATE TABLE suppliers(
  id SERIAL NOT NULL,
  name CHARACTER VARYING(255) NOT NULL UNIQUE,
  description CHARACTER VARYING(1024) NOT NULL
);

DROP TABLE IF EXISTS public.product_categories;
CREATE TABLE product_categories(
  id SERIAL NOT NULL,
  name CHARACTER VARYING(255) NOT NULL UNIQUE,
  description CHARACTER VARYING(1024) NOT NULL
);

DROP TABLE IF EXISTS public.products;
CREATE TABLE products (
  id SERIAL NOT NULL,
  name CHARACTER VARYING(255) NOT NULL UNIQUE,
  description CHARACTER VARYING(1024) NOT NULL,
  price DOUBLE PRECISION NOT NULL,
  product_category_id INTEGER,
  supplier_id INTEGER,
  currency CHARACTER VARYING(3) NOT NULL
);


DROP TABLE IF EXISTS public.products_of_order;
CREATE TABLE products_of_order(
  id SERIAL NOT NULL,
  order_id INTEGER,
  product_id INTEGER,
  quantity INTEGER NOT NULL
);


DROP TABLE IF EXISTS public.users;
CREATE TABLE users(
  id SERIAL NOT NULL,
  email CHARACTER VARYING(255) NOT NULL,
  password CHARACTER VARYING(255) NOT NULL,
  shipping_address CHARACTER VARYING(255),
  billing_address CHARACTER VARYING(255),
  firstname CHARACTER VARYING(255) NOT NULL,
  lastname CHARACTER VARYING(255) NOT NULL
);



ALTER TABLE ONLY orders
    ADD CONSTRAINT pk_orders_id PRIMARY KEY (id);

ALTER TABLE ONLY suppliers
  ADD CONSTRAINT pk_suppliers_id PRIMARY KEY (id);

ALTER TABLE ONLY product_categories
  ADD CONSTRAINT pk_product_categories_id PRIMARY KEY (id);

ALTER TABLE ONLY products
  ADD CONSTRAINT pk_products_id PRIMARY KEY (id);

ALTER TABLE ONLY products_of_order
  ADD CONSTRAINT pk_products_of_order_id PRIMARY KEY (id);

ALTER TABLE ONLY users
  ADD CONSTRAINT pk_users_id PRIMARY KEY (id);




ALTER TABLE ONLY products
  ADD CONSTRAINT fk_product_category_id FOREIGN KEY (product_category_id) REFERENCES product_categories(id) ON DELETE CASCADE;

ALTER TABLE ONLY products
  ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE CASCADE;


ALTER TABLE ONLY products_of_order
  ADD CONSTRAINT fk_order_id FOREIGN KEY (order_id) REFERENCES orders(id);

ALTER TABLE ONLY products_of_order
  ADD CONSTRAINT fk_product_id FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE ONLY orders
    ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
