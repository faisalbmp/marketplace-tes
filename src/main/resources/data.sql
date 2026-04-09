INSERT INTO item (item_name, description, created_date, created_by, modified_date, modified_by)
VALUES ('T-Shirt', 'A nice cotton t-shirt', NOW(), 'system', null, null);
INSERT INTO variant (item_id, sku, variant_name, price, created_date, created_by, modified_date, modified_by)
VALUES (1, 'SHIRT-RED-L', 'Red T-Shirt Size L', 19.99, NOW(), 'system', null, null);
INSERT INTO inventory (variant_id, quantity, version, created_date, created_by, modified_date, modified_by)
VALUES (1, 10, 0, NOW(), 'system', null, null);

INSERT INTO item (item_name, description, created_date, created_by, modified_date, modified_by)
VALUES ('Coffee Mug', 'Ceramic coffee mug', NOW(), 'system', null, null);
INSERT INTO variant (item_id, sku, variant_name, price, created_date, created_by, modified_date, modified_by)
VALUES (2, 'MUG-WHT-11OZ', 'White 11oz Mug', 9.99, NOW(), 'system', null, null);
INSERT INTO inventory (variant_id, quantity, version, created_date, created_by, modified_date, modified_by)
VALUES (2, 50, 0, NOW(), 'system', null, null);
