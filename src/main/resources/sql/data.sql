-- INITIAL DATA TO BE INSERTED

-- SUPERADMIN: superadmin@kdg.be PASS: KDG123
INSERT INTO account (email, name, address, phone, role, password)
SELECT 'superadmin@kdg.com', 'Super Admin', 'Antwerp, Belgium',
       '123123123', 'SUPERADMIN', '$2a$10$1gBU9M0k/OlskuKvreeSA.7QqZp3P264xJ8Vm/3m26eFSMH.1sZQC'
WHERE NOT EXISTS (
    SELECT 1 FROM account WHERE email = 'superadmin@kdg.com'
);

-- ADMIN: admin@kdg.com PASS: admin
INSERT INTO account (email, name, address, is_role_approved, company_name, phone, role, password)
SELECT 'admin@kdg.com', 'Admin', 'Brussels, Belgium', TRUE, 'kdg',
       '123123123', 'ADMIN', '$2a$12$83rB9gsrB3BjikUwyo.yBeetd7/bXwM9XoN6Fcb3236K4nWKZB8wW'
WHERE NOT EXISTS (
    SELECT 1 FROM account WHERE email = 'admin@kdg.com'
);

-- TECHNICIAN: tech@kdg.com PASS: tech
INSERT INTO account (email, name, address, is_role_approved, experience_years, phone, role, password)
SELECT 'tech@kdg.com', 'Technician', 'Antwerp, Belgium', TRUE, 0,
       '123123123', 'TECHNICIAN', '$2a$12$32ZQFGDUJOqxnNCD.kKXmeCD9HLtrssx9v67qTpdnNrc2y9GJxzkW'
WHERE NOT EXISTS (
    SELECT 1 FROM account WHERE email = 'tech@kdg.com'
);

-- TECHNICIAN: tech1@kdg.com PASS: tech
INSERT INTO account (email, name, address, is_role_approved, experience_years, phone, role, password)
SELECT 'tech1@kdg.com', 'Technician', 'Antwerp, Belgium', FALSE, 0,
       '123123123', 'TECHNICIAN', '$2a$12$32ZQFGDUJOqxnNCD.kKXmeCD9HLtrssx9v67qTpdnNrc2y9GJxzkW'
WHERE NOT EXISTS (
    SELECT 1 FROM account WHERE email = 'tech1@kdg.com'
);

-- CUSTOMER: jurgen@kdg.com PASS: test
INSERT INTO account (email, full_name, name, address, company_name, phone, role, password)
SELECT 'jurgen@kdg.com', 'Jurgen Heyman', 'Jurgen', 'Talinn, Estonia', 'KdG',
       '123123123', 'CUSTOMER', '$2a$12$.jAX4Lol/n6G8i0xPsUQGu5XR2fp/Ze8pWYFbn3/7ti2eUeY5zqDC'
WHERE NOT EXISTS (
    SELECT 1 FROM account WHERE email = 'jurgen@kdg.com'
);

INSERT INTO account (email, full_name, name, address, company_name, phone, role, password)
SELECT 'jurgen3123@kdg.com', 'Paturgen Goodman', 'Jurgen', 'Talinn, Estonia', 'antwerp',
       '123123123', 'CUSTOMER', '$2a$12$.jAX4Lol/n6G8i0xPsUQGu5XR2fp/Ze8pWYFbn3/7ti2eUeY5zqDC'
WHERE NOT EXISTS (
    SELECT 1 FROM account WHERE email = 'jurgen3123@kdg.com'
);

-- E-bike model data
INSERT INTO ebike_model (name, battery_capacity, engine_power_max, engine_power_nominal, engine_torque, max_support)
SELECT 'Power', 333, 444, 444, 444, 444
WHERE NOT EXISTS (
    SELECT 1 FROM ebike_model WHERE name = 'Power' AND battery_capacity = 333
);

-- E-bike data
INSERT INTO ebike (battery, model, brand, model_id, added_date)
SELECT 'Endurance', 'Longevity', 'Takanaka', 1, '2025-06-13 14:30:00'
WHERE NOT EXISTS (
    SELECT 1 FROM ebike WHERE battery = 'Endurance' AND model = 'Longevity' AND brand = 'Takanaka'
);

-- Customer bike relationship
INSERT INTO cust_bike (customer_id, ebike_id)
SELECT 5, 1
WHERE NOT EXISTS (
    SELECT 1 FROM cust_bike WHERE customer_id = 5 AND ebike_id = 1
);

-- Test data
INSERT INTO test (customer_id, ebike_id, technician_id, start_date, end_date, type, status, uuid,
                  battery_capacity, engine_power_max, engine_power_nominal, engine_torque, max_support)
SELECT 5, 1, 3, '2025-06-13 14:30:00', '2025-06-13 15:30:00', 'FAST', 'PENDING', '0219377e-273e-4c3f-a660-36e0a1ded16d',
       333, 444, 444, 444, 444
WHERE NOT EXISTS (
    SELECT 1 FROM test WHERE uuid = '0219377e-273e-4c3f-a660-36e0a1ded16d'
);

-- Bearing threshold data
INSERT INTO bearing_threshold (bearing_type, max_horizontal_vibration, max_vertical_vibration)
SELECT 'FRONT_AXIS', 2.5, 2.0
WHERE NOT EXISTS (
    SELECT 1 FROM bearing_threshold WHERE bearing_type = 'FRONT_AXIS'
);

INSERT INTO bearing_threshold (bearing_type, max_horizontal_vibration, max_vertical_vibration)
SELECT 'REAR_AXIS', 2.8, 2.2
WHERE NOT EXISTS (
    SELECT 1 FROM bearing_threshold WHERE bearing_type = 'REAR_AXIS'
);

INSERT INTO bearing_threshold (bearing_type, max_horizontal_vibration, max_vertical_vibration)
SELECT 'MOTOR', 3.0, 2.5
WHERE NOT EXISTS (
    SELECT 1 FROM bearing_threshold WHERE bearing_type = 'MOTOR'
);