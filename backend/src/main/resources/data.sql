-- 카테고리 삽입 3개
-- 카드 10개 (카테고리 당 카드 10개)
-- 답안 30개 (카드질문 당 답 1개)

INSERT INTO category(name, logo_url, description) VALUES ("JAVA", "logo1", "java입니다.");
INSERT INTO category(name, logo_url, description) VALUES ("Javascript", "logo2", "javascript 입니다");
INSERT INTO category(name, logo_url, description) VALUES ("Spring", "logo3", "Spring입니다.");

INSERT INTO card(question, category_id) VALUES ("Java 질문 1", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 2", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 3", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 4", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 5", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 6", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 7", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 8", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 9", 1);
INSERT INTO card(question, category_id) VALUES ("Java 질문 10", 1);

INSERT INTO card(question, category_id) VALUES ("Javascript 질문 1", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 2", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 3", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 4", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 5", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 6", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 7", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 8", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 9", 2);
INSERT INTO card(question, category_id) VALUES ("Javascript 질문 10", 2);

INSERT INTO card(question, category_id) VALUES ("Spring 질문 1", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 2", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 3", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 4", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 5", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 6", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 7", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 8", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 9", 3);
INSERT INTO card(question, category_id) VALUES ("Spring 질문 10", 3);

INSERT INTO answer(content, card_id) VALUES ("Java 답 1", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 2", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 3", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 4", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 5", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 6", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 7", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 8", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 9", 1);
INSERT INTO answer(content, card_id) VALUES ("Java 답 10", 1);

INSERT INTO answer(content, card_id) VALUES ("Javascript 답 1", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 2", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 3", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 4", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 5", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 6", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 7", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 8", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 9", 2);
INSERT INTO answer(content, card_id) VALUES ("Javascript 답 10", 2);

INSERT INTO answer(content, card_id) VALUES ("Spring 답 1", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 2", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 3", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 4", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 5", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 6", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 7", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 8", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 9", 3);
INSERT INTO answer(content, card_id) VALUES ("Spring 답 10", 3);





