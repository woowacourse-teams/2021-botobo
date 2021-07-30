CREATE TABLE user(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    github_id BIGINT NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    profile_url VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE workbook(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    opened TINYINT(1) NOT NULL,
    deleted TINYINT(1) NOT NULL,
    user_id BIGINT not null,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES user(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE card(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question BLOB NOT NULL,
    answer BLOB NOT NULL,
    encounter_count INT NOT NULL,
    bookmark TINYINT(1) not null,
    next_quiz TINYINT(1) not null,
    workbook_id BIGINT not null,
    deleted TINYINT(1) not null,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(workbook_id) REFERENCES workbook(id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE tag(
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(30) UNIQUE NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE workbooktag(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workbook_id BIGINT not null,
    tag_id BIGINT not null,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(workbook_id) REFERENCES workbook(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY(tag_id) REFERENCES tag(id) ON UPDATE CASCADE ON DELETE RESTRICT
);