package botobo.core.utils;

import botobo.core.dto.admin.AdminCardRequest;
import botobo.core.dto.admin.AdminWorkbookRequest;

import java.util.Arrays;
import java.util.List;

public class Fixture {

    public static final AdminWorkbookRequest WORKBOOK_REQUEST_1 = new AdminWorkbookRequest("1");
    public static final AdminWorkbookRequest WORKBOOK_REQUEST_2 = new AdminWorkbookRequest("2");
    private static final AdminWorkbookRequest WORKBOOK_REQUEST_3 = new AdminWorkbookRequest("3");
    private static final AdminWorkbookRequest WORKBOOK_REQUEST_4 = new AdminWorkbookRequest("4");
    private static final AdminWorkbookRequest WORKBOOK_REQUEST_5 = new AdminWorkbookRequest("5", false);

    public static final List<AdminWorkbookRequest> ADMIN_WORKBOOK_REQUESTS =
            Arrays.asList(WORKBOOK_REQUEST_1, WORKBOOK_REQUEST_2, WORKBOOK_REQUEST_3,
                    WORKBOOK_REQUEST_4, WORKBOOK_REQUEST_5);

    private static final AdminCardRequest CARD_REQUEST_1 = new AdminCardRequest("1", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_2 = new AdminCardRequest("2", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_3 = new AdminCardRequest("3", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_4 = new AdminCardRequest("4", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_5 = new AdminCardRequest("5", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_6 = new AdminCardRequest("6", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_7 = new AdminCardRequest("7", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_8 = new AdminCardRequest("8", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_9 = new AdminCardRequest("9", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_10 = new AdminCardRequest("10", "answer", 1L);
    private static final AdminCardRequest CARD_REQUEST_11 = new AdminCardRequest("11", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_12 = new AdminCardRequest("12", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_13 = new AdminCardRequest("13", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_14 = new AdminCardRequest("14", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_15 = new AdminCardRequest("15", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_16 = new AdminCardRequest("16", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_17 = new AdminCardRequest("17", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_18 = new AdminCardRequest("18", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_19 = new AdminCardRequest("19", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_20 = new AdminCardRequest("20", "answer", 2L);
    private static final AdminCardRequest CARD_REQUEST_21 = new AdminCardRequest("21", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_22 = new AdminCardRequest("22", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_23 = new AdminCardRequest("23", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_24 = new AdminCardRequest("24", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_25 = new AdminCardRequest("25", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_26 = new AdminCardRequest("26", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_27 = new AdminCardRequest("27", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_28 = new AdminCardRequest("28", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_29 = new AdminCardRequest("29", "answer", 3L);
    private static final AdminCardRequest CARD_REQUEST_30 = new AdminCardRequest("30", "answer", 3L);

    public static final List<AdminCardRequest> ADMIN_CARD_REQUESTS_OF_15_CARDS =
            Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3, CARD_REQUEST_4,
                    CARD_REQUEST_5, CARD_REQUEST_6, CARD_REQUEST_7, CARD_REQUEST_8, CARD_REQUEST_9, CARD_REQUEST_10,
                    CARD_REQUEST_11, CARD_REQUEST_12, CARD_REQUEST_13, CARD_REQUEST_14, CARD_REQUEST_15);

public static final List<AdminCardRequest> ADMIN_CARD_REQUESTS_OF_30_CARDS =
            Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3, CARD_REQUEST_4,
                    CARD_REQUEST_5, CARD_REQUEST_6, CARD_REQUEST_7, CARD_REQUEST_8, CARD_REQUEST_9, CARD_REQUEST_10,
                    CARD_REQUEST_11, CARD_REQUEST_12, CARD_REQUEST_13, CARD_REQUEST_14, CARD_REQUEST_15, CARD_REQUEST_16,
                    CARD_REQUEST_17, CARD_REQUEST_18, CARD_REQUEST_19, CARD_REQUEST_20, CARD_REQUEST_21, CARD_REQUEST_22,
                    CARD_REQUEST_23, CARD_REQUEST_24, CARD_REQUEST_25, CARD_REQUEST_26, CARD_REQUEST_27, CARD_REQUEST_28,
                    CARD_REQUEST_29, CARD_REQUEST_30);

    public static final List<AdminCardRequest> ADMIN_CARD_REQUESTS_IN_ONE_WORKBOOK =
            Arrays.asList(CARD_REQUEST_1, CARD_REQUEST_2, CARD_REQUEST_3);
}
