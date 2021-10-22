package botobo.core.acceptance.utils;

import botobo.core.domain.user.Role;
import botobo.core.domain.user.User;
import botobo.core.dto.auth.GithubUserInfoResponse;
import botobo.core.dto.auth.GoogleUserInfoResponse;
import botobo.core.dto.auth.UserInfoResponse;
import botobo.core.dto.card.CardRequest;
import botobo.core.dto.tag.TagRequest;
import botobo.core.dto.workbook.WorkbookRequest;

import java.util.List;

public class Fixture {

    public static User USER_PK = User.builder()
            .socialId("10")
            .userName("pk")
            .profileUrl("github.io")
            .role(Role.USER)
            .build();

    public static User USER_BEAR = User.builder()
            .socialId("20")
            .userName("bear")
            .profileUrl("github.io")
            .role(Role.USER)
            .build();

    public static User USER_OZ = User.builder()
            .socialId("30")
            .userName("oz")
            .profileUrl("github.io")
            .role(Role.USER)
            .build();

    public static User USER_JOANNE = User.builder()
            .socialId("40")
            .userName("joanne")
            .profileUrl("github.io")
            .role(Role.USER)
            .build();

    public static User USER_KYLE = User.builder()
            .socialId("50")
            .userName("kyle")
            .profileUrl("github.io")
            .role(Role.USER)
            .build();

    public static User USER_DITTO = User.builder()
            .socialId("60")
            .userName("ditto")
            .profileUrl("github.io")
            .role(Role.USER)
            .build();

    public static UserInfoResponse USER_RESPONSE_OF_PK = GithubUserInfoResponse.builder()
            .userName("pk")
            .socialId("10")
            .profileUrl("pk.profile")
            .build();
    public static UserInfoResponse USER_RESPONSE_OF_BEAR = GithubUserInfoResponse.builder()
            .userName("bear")
            .socialId("20")
            .profileUrl("bear.profile")
            .build();
    public static UserInfoResponse USER_RESPONSE_OF_OZ = GithubUserInfoResponse.builder()
            .userName("oz")
            .socialId("30")
            .profileUrl("oz.profile")
            .build();
    public static UserInfoResponse USER_RESPONSE_OF_JOANNE = GoogleUserInfoResponse.builder()
            .userName("joanne")
            .socialId("40")
            .profileUrl("joanne.profile")
            .build();
    public static UserInfoResponse USER_RESPONSE_OF_KYLE = GoogleUserInfoResponse.builder()
            .userName("kyle")
            .socialId("50")
            .profileUrl("kyle.profile")
            .build();
    public static UserInfoResponse USER_RESPONSE_OF_DITTO = GoogleUserInfoResponse.builder()
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
