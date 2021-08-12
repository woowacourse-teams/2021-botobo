CREATE TABLE heart(
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      workbook_id BIGINT not null,
                      user_id BIGINT not null,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY(workbook_id) REFERENCES workbook(id) ON UPDATE CASCADE ON DELETE RESTRICT
);