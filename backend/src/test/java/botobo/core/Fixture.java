package botobo.core;

import botobo.core.admin.dto.AdminAnswerRequest;
import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminCategoryRequest;

public class Fixture {

    public static final AdminCategoryRequest CATEGORY_REQUEST_1 = new AdminCategoryRequest("1"); //3개
    public static final AdminCategoryRequest CATEGORY_REQUEST_2 = new AdminCategoryRequest("2"); //3개
    public static final AdminCategoryRequest CATEGORY_REQUEST_3 = new AdminCategoryRequest("3"); //3개

    public static final AdminCardRequest CARD_REQUEST_1 = new AdminCardRequest("1", 1L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_2 = new AdminCardRequest("2", 1L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_3 = new AdminCardRequest("3", 1L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_4 = new AdminCardRequest("4", 1L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_5 = new AdminCardRequest("5", 1L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_6 = new AdminCardRequest("1", 2L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_7 = new AdminCardRequest("2", 2L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_8 = new AdminCardRequest("3", 2L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_9 = new AdminCardRequest("4", 2L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_10 = new AdminCardRequest("5", 2L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_11 = new AdminCardRequest("1", 3L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_12 = new AdminCardRequest("2", 3L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_13 = new AdminCardRequest("3", 3L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_14 = new AdminCardRequest("4", 3L); //15개 (1카테 5카드)
    public static final AdminCardRequest CARD_REQUEST_15 = new AdminCardRequest("5", 3L); //15개 (1카테 5카드)

    public static final AdminAnswerRequest ANSWER_REQUEST_1 = new AdminAnswerRequest("1", 1L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_2 = new AdminAnswerRequest("2", 2L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_3 = new AdminAnswerRequest("3", 3L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_4 = new AdminAnswerRequest("4", 4L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_5 = new AdminAnswerRequest("5", 5L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_6 = new AdminAnswerRequest("6", 6L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_7 = new AdminAnswerRequest("7", 7L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_8 = new AdminAnswerRequest("8", 8L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_9 = new AdminAnswerRequest("9", 9L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_10 = new AdminAnswerRequest("10", 10L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_11 = new AdminAnswerRequest("11", 11L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_12 = new AdminAnswerRequest("12", 12L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_13 = new AdminAnswerRequest("13", 13L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_14 = new AdminAnswerRequest("14", 14L); // 카드당 1개 (총 15개)
    public static final AdminAnswerRequest ANSWER_REQUEST_15 = new AdminAnswerRequest("15", 15L); // 카드당 1개 (총 15개)
}
