package botobo.core;

import botobo.core.admin.dto.AdminCardRequest;
import botobo.core.admin.dto.AdminWorkbookRequest;

public class Fixture {

    public static final AdminWorkbookRequest WORKBOOK_REQUEST_1 = new AdminWorkbookRequest("1"); //3개
    public static final AdminWorkbookRequest WORKBOOK_REQUEST_2 = new AdminWorkbookRequest("2"); //3개
    public static final AdminWorkbookRequest WORKBOOK_REQUEST_3 = new AdminWorkbookRequest("3"); //3개

    public static final AdminCardRequest CARD_REQUEST_1 = new AdminCardRequest("1", "answer", 1L); //(문제집 1)
    public static final AdminCardRequest CARD_REQUEST_2 = new AdminCardRequest("2", "answer", 1L); //(문제집 1)
    public static final AdminCardRequest CARD_REQUEST_3 = new AdminCardRequest("3", "answer", 1L); //(문제집 1)
    public static final AdminCardRequest CARD_REQUEST_4 = new AdminCardRequest("4", "answer", 1L); //(문제집 1)
    public static final AdminCardRequest CARD_REQUEST_5 = new AdminCardRequest("5", "answer", 1L); //(문제집 1)
    public static final AdminCardRequest CARD_REQUEST_6 = new AdminCardRequest("1", "answer", 2L); //(문제집 2)
    public static final AdminCardRequest CARD_REQUEST_7 = new AdminCardRequest("2", "answer", 2L); //(문제집 2)
    public static final AdminCardRequest CARD_REQUEST_8 = new AdminCardRequest("3", "answer", 2L); //(문제집 2)
    public static final AdminCardRequest CARD_REQUEST_9 = new AdminCardRequest("4", "answer", 2L); //(문제집 2)
    public static final AdminCardRequest CARD_REQUEST_10 = new AdminCardRequest("5", "answer", 2L); //(문제집 2)
    public static final AdminCardRequest CARD_REQUEST_11 = new AdminCardRequest("1", "answer", 3L); //(문제집 3)
    public static final AdminCardRequest CARD_REQUEST_12 = new AdminCardRequest("2", "answer", 3L); //(문제집 3)
    public static final AdminCardRequest CARD_REQUEST_13 = new AdminCardRequest("3", "answer", 3L); //(문제집 3)
    public static final AdminCardRequest CARD_REQUEST_14 = new AdminCardRequest("4", "answer", 3L); //(문제집 3)
    public static final AdminCardRequest CARD_REQUEST_15 = new AdminCardRequest("5", "answer", 3L); //(문제집 3)
}
