CREATE TABLE IF NOT EXISTS drone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    serial_number VARCHAR(100) NOT NULL,
    model VARCHAR(25) NOT NULL,
    battery_capacity DECIMAL(10, 2) NOT NULL,
    status VARCHAR(15) NOT NULL,
    create_dt DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS medication (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    weight FLOAT NOT NULL,
    code VARCHAR(255) NOT NULL,
    image VARCHAR(255),
    drone_id BIGINT,
    create_dt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (drone_id) REFERENCES drone(id)
);

INSERT INTO drone (serial_number, model, battery_capacity, status)
VALUES
    ('DRONE01', 'LIGHTWEIGHT', 100.00, 'IDLE'),
    ('DRONE02', 'MIDDLEWEIGHT', 90, 'IDLE'),
    ('DRONE03', 'CRUISERWEIGHT', 100.00, 'IDLE'),
    ('DRONE04', 'HEAVYWEIGHT', 80.00, 'IDLE');

INSERT INTO medication (name, weight, code, image, drone_id)
VALUES
    ('med-001', 100.0, 'M-001', 'med001.png', 1),
    ('med-002', 200.0, 'M-002', 'med002.png', 2),
    ('med-003', 150.0, 'M-003', 'med003.png', 3),
    ('med-004', 150.0, 'M-004', 'med004.png', 4);