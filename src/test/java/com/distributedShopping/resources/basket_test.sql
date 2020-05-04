CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.basket (
    addressId INTEGER NOT NULL,
    productId INTEGER NOT NULL,
    selectedQuantity FLOAT NOT NULL,
    PRIMARY KEY(addressId, productId)
);

INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (1,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (2,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (2,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (3,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (3,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (3,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (4,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (4,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (4,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (4,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (5,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (5,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (5,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (5,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (5,5,5);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (6,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (6,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (6,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (6,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (6,5,5);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (6,6,6);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,5,5);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,6,6);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (7,7,7);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,5,5);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,6,6);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,7,7);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (8,8,8);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,5,5);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,6,6);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,7,7);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,8,8);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (9,9,9);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,1,1);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,2,2);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,3,3);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,4,4);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,5,5);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,6,6);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,7,7);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,8,8);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,9,9);
INSERT INTO basket(addressId,productId,selectedQuantity) VALUES (10,10,10);
