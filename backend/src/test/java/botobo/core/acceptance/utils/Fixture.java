package botobo.core.acceptance.utils;

import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.GoogleUserInfoResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookRequest;

import java.util.List;

public class Fixture {

    public static UserInfoResponse PK = GithubUserInfoResponse.builder()
            .userName("pk")
            .socialId("10")
            .profileUrl("pk.profile")
            .build();
    public static UserInfoResponse BEAR = GithubUserInfoResponse.builder()
            .userName("bear")
            .socialId("20")
            .profileUrl("bear.profile")
            .build();
    public static UserInfoResponse OZ = GithubUserInfoResponse.builder()
            .userName("oz")
            .socialId("30")
            .profileUrl("oz.profile")
            .build();
    public static UserInfoResponse JOANNE = GoogleUserInfoResponse.builder()
            .userName("joanne")
            .socialId("40")
            .profileUrl("joanne.profile")
            .build();
    public static UserInfoResponse KYLE = GoogleUserInfoResponse.builder()
            .userName("kyle")
            .socialId("50")
            .profileUrl("kyle.profile")
            .build();
    public static UserInfoResponse DITTO = GoogleUserInfoResponse.builder()
            .userName("ditto")
            .socialId("60")
            .profileUrl("ditto.profile")
            .build();

    public static WorkbookRequest MAKE_SINGLE_WORKBOOK_REQUEST(
            String name,
            boolean opened,
            List<TagRequest> tagRequests
    ) {
        return WorkbookRequest.builder()
                .name(name)
                .opened(opened)
                .tags(tagRequests)
                .build();
    }

    public static TagRequest MAKE_SINGLE_TAG_REQUEST(
            Long id,
            String name
    ) {
        return TagRequest.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static CardRequest MAKE_SINGLE_CARD_REQUEST(
            String question,
            String answer,
            Long workbookId
    ) {
        return CardRequest.builder()
                .question(question)
                .answer(answer)
                .workbookId(workbookId)
                .build();
    }
}
