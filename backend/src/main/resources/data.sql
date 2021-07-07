-- 카테고리 삽입 3개
-- 카드 10개 (카테고리 당 카드 10개)
-- 답안 30개 (카드질문 당 답 1개)

INSERT INTO category(name, is_deleted, logo_url, description) VALUES ('JAVA', false, 'logo1', 'java입니다.');
INSERT INTO category(name, is_deleted, logo_url, description) VALUES ('Javascript', false, 'logo2', 'javascript 입니다');
INSERT INTO category(name, is_deleted, logo_url, description) VALUES ('Spring', false, 'logo3', 'Spring입니다.');

INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 1', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 2', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 3', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 4', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 5', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 6', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 7', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 8', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 9', false,1);
INSERT INTO card(question, is_deleted, category_id) VALUES ('Java 질문 10',false, 1);

INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 1', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 2', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 3', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 4', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 5', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 6', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 7', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 8', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 9', false,2);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Javascript 질문 10',false, 2);

INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 1', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 2', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 3', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 4', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 5', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 6', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 7', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 8', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 9', false,3);
INSERT INTO card(question, is_deleted,category_id) VALUES ('Spring 질문 10',false, 3);

INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 1', false,1);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 2', false,2);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 3', false,3);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 4', false,4);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 5', false,5);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 6', false,6);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 7', false,7);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 8', false,8);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 9', false,9);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Java 답 10',false, 10);

INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 1', false,11);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 2', false,12);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 3', false,13);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 4', false,14);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 5', false,15);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 6', false,16);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 7', false,17);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 8', false,18);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 9', false,19);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Javascript 답 10',false,20);

INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 1', false,21);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 2', false,22);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 3', false,23);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 4', false,24);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 5', false,25);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 6', false,26);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 7', false,27);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 8', false,28);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 9', false,29);
INSERT INTO answer(content,is_deleted, card_id) VALUES ('Spring 답 10',false, 30);




