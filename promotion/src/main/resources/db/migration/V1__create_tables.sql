CREATE TABLE promotion (
    id CHAR(36) NOT NULL,
    location_id CHAR(36) NOT NULL,
    code VARCHAR(255),
    id_client VARCHAR(255),
    discount_percentage DECIMAL(10,2),
    item_id CHAR(36),
    target_type VARCHAR(50),
    created_at DATETIME,
    status VARCHAR(50),
    start_date DATE,
    end_date DATE,
    PRIMARY KEY (id)
);
