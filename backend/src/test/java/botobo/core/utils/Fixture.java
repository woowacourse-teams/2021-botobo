package botobo.core.utils;

import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.GoogleUserInfoResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fixture {


    public static final List<WorkbookRequest> WORKBOOK_REQUESTS
            = 문제집명만_포함된_문제집_요청_만들기(
            List.of(
                    Map.of("1", true),
                    Map.of("2", true),
                    Map.of("3", true),
                    Map.of("4", true),
                    Map.of("5", false)
            )
    );

    private static List<WorkbookRequest> 문제집명만_포함된_문제집_요청_만들기(List<Map<String, Boolean>> nameAndOpenedSets) {
        List<WorkbookRequest> requests = new ArrayList<>();

        for (Map<String, Boolean> args : nameAndOpenedSets) {
            for (Map.Entry<String, Boolean> entry : args.entrySet()) {
                requests.add(
                        WorkbookRequest.builder()
                                .name(entry.getKey())
                                .opened(entry.getValue())
                                .build()
                );
            }
        }
        return requests;
    }

    private static final List<TagRequest> TAG_REQUESTS_TYPE_1 = 태그명과_아이디가_포함된_태그_요청_만들기(
            List.of(
                    Map.of(1L, "자바"),
                    Map.of(2L, "자바스크립트"),
                    Map.of(3L, "리액트")
            )
    );

    private static final List<TagRequest> TAG_REQUESTS_TYPE_2 = 태그명과_아이디가_포함된_태그_요청_만들기(
            List.of(
                    Map.of(1L, "자바"),
                    Map.of(3L, "리액트"),
                    Map.of(5L, "네트워크")
            )
    );

    private static final List<TagRequest> TAG_REQUESTS_TYPE_3 = 태그명과_아이디가_포함된_태그_요청_만들기(
            List.of(
                    Map.of(2L, "자바스크립트"),
                    Map.of(4L, "스프링"),
                    Map.of(5L, "네트워크")
            )
    );

    public static final List<WorkbookRequest> WORKBOOK_REQUESTS_WITH_TAG =
            문제집명과_태그_요청이_포함된_문제집_요청_만들기(
                    List.of(
                            Map.of("Java", true),
                            Map.of("JAVAVA", true),
                            Map.of("Javascript", true)
                    ),
                    List.of(TAG_REQUESTS_TYPE_1, TAG_REQUESTS_TYPE_2, TAG_REQUESTS_TYPE_3)
            );

    /**
     * 주의, namedAndOpenedSets와 tagRequests의 사이즈는 동일해야하며
     * 병렬적으로 매핑되어 WorkbookRequest를 생성합니다.
     *
     * @param nameAndOpenedSets
     * @param tagRequests
     * @return 태그가 포함된 WorkbookRequest List
     */
    private static List<WorkbookRequest> 문제집명과_태그_요청이_포함된_문제집_요청_만들기(List<Map<String, Boolean>> nameAndOpenedSets,
                                                                     List<List<TagRequest>> tagRequests) {

        if (nameAndOpenedSets.size() != tagRequests.size()) {
            throw new IllegalArgumentException("문제집명과 태그 요청의 수는 같아야합니다.");
        }
        List<WorkbookRequest> requests = new ArrayList<>();
        int size = nameAndOpenedSets.size();

        for (int i = 0; i < size; i++) {
            Map<String, Boolean> nameAndOpened = nameAndOpenedSets.get(i);
            List<TagRequest> tagRequest = tagRequests.get(i);
            for (Map.Entry<String, Boolean> entry : nameAndOpened.entrySet()) {
                requests.add(
                        WorkbookRequest.builder()
                                .name(entry.getKey())
                                .opened(entry.getValue())
                                .tags(tagRequest)
                                .build()
                );
            }
        }
        return requests;
    }

    private static List<TagRequest> 태그명과_아이디가_포함된_태그_요청_만들기(List<Map<Long, String>> idAndNameSets) {
        List<TagRequest> requests = new ArrayList<>();

        for (Map<Long, String> args : idAndNameSets) {
            for (Map.Entry<Long, String> entry : args.entrySet()) {
                requests.add(
                        TagRequest.builder()
                                .id(entry.getKey())
                                .name(entry.getValue())
                                .build()
                );
            }
        }
        return requests;
    }

    public static final List<CardRequest> CARD_REQUESTS_OF_30_CARDS =
            질문_답변_문제집_아이디가_포함된_카드_요청_만들기(
                    질문_답변_문제집_아이디를_띄어쓰기로_구분해_30개_생성()
            );

    private static List<String> 질문_답변_문제집_아이디를_띄어쓰기로_구분해_30개_생성() {
        List<String> results = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            long id = ((i - 1) / 10) + 1;
            results.add(i + " answer " + id);
        }
        return results;
    }

    /**
     * 주의, questionAndAnswerAndWorkbookIds에 담기는 String은 "질문 답변 문제집ID"가 띄어쓰기로 구분된 형태의 String입니다.
     *
     * @param questionAndAnswerAndWorkbookIds
     * @return
     */
    private static List<CardRequest> 질문_답변_문제집_아이디가_포함된_카드_요청_만들기(List<String> questionAndAnswerAndWorkbookIds) {
        List<CardRequest> requests = new ArrayList<>();

        for (String questionAndAnswerAndWorkbookId : questionAndAnswerAndWorkbookIds) {
            String[] split = questionAndAnswerAndWorkbookId.split(" ");
            if (split.length != 3) {
                throw new IllegalArgumentException("카드 생성 요청을 만들기 위해서는 질문, 답변, 문제집 ID가 띄어쓰기로 구분되어 모두 포함되어야합니다.");
            }
            String question = split[0];
            String answer = split[1];
            Long workbookId = Long.valueOf(split[2]);

            requests.add(
                    CardRequest.builder()
                            .question(question)
                            .answer(answer)
                            .workbookId(workbookId)
                            .build()
            );
        }
        return requests;
    }

    public static UserInfoResponse pk = GithubUserInfoResponse.builder()
            .userName("pk")
            .socialId("10")
            .profileUrl("pk.profile")
            .build();
    public static UserInfoResponse bear = GithubUserInfoResponse.builder()
            .userName("bear")
            .socialId("20")
            .profileUrl("bear.profile")
            .build();
    public static UserInfoResponse oz = GithubUserInfoResponse.builder()
            .userName("oz")
            .socialId("30")
            .profileUrl("oz.profile")
            .build();
    public static UserInfoResponse joanne = GoogleUserInfoResponse.builder()
            .userName("joanne")
            .socialId("40")
            .profileUrl("joanne.profile")
            .build();
    public static UserInfoResponse kyle = GoogleUserInfoResponse.builder()
            .userName("kyle")
            .socialId("50")
            .profileUrl("kyle.profile")
            .build();
    public static UserInfoResponse ditto = GoogleUserInfoResponse.builder()
            .userName("ditto")
            .socialId("60")
            .profileUrl("ditto.profile")
            .build();
}
