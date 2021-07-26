package botobo.core.quiz.utils;

import botobo.core.quiz.domain.card.Card;
import botobo.core.quiz.domain.workbook.Workbook;

import java.util.List;

public class CardFactory {
    public static List<Card> createGuestCards() {
        final Workbook workbook = Workbook.builder()
                .name("비회원 문제집")
                .build();

        final Card firstCard = Card.builder()
                .id(0L)
                .question("HTTP Method의 종류에 대해 설명해주세요.")
                .answer("GET, POST, UPDATE, DELETE, PUT")
                .workbook(workbook)
                .build();

        final Card secondCard = Card.builder()
                .id(0L)
                .question("ISP란 무엇인가요?")
                .answer("Internet Service Provider, 인터넷 서비스 공급자로 다양한 회선 상품을 제공하며 기업마다 서비스가 다르다.")
                .workbook(workbook)
                .build();

        final Card thirdCard = Card.builder()
                .id(0L)
                .question("Spring Security란 무엇인가요?")
                .answer("Spring Security는 Java 애플리케이션에서 인증 및 권한 부여 방법을 제공하는 데 초점을 맞춘 Spring 프레임 워크의 별도 모듈입니다. 또한 CSRF 공격과 같은 대부분의 일반적인 Security 취약점을 처리합니다.")
                .workbook(workbook)
                .build();

        final Card fourthCard = Card.builder()
                .id(0L)
                .question("Spring에서 Interceptor는 무엇인가요?")
                .answer("인터셉터는 컨트롤러에서 요청을 핸들링 하기 전이나 후에 특정 동작을 할 수 있는 방법을 제공한다. HandlerInterceptor 인터페이스를 구현하여 정의한다.")
                .workbook(workbook)
                .build();

        final Card fifthCard = Card.builder()
                .id(0L)
                .question("COMMIT과 ROLLBACK에 대해 설명해주세요.")
                .answer("COMMIT은 해당 트랜잭션으로 반영된 데이터베이스 변경사항을 저장 하는 것이고 ROLLBACK은 해당 트랜잭션으로 반영된 데이터베이스 변경사항을 취소 하는 것이다.")
                .workbook(workbook)
                .build();

        final Card sixthCard = Card.builder()
                .id(0L)
                .question("데이터베이스 무결성이란 무엇인가요?")
                .answer("데이터 베이스에 저장된 데이터 값과 그것이 표현하는 현실 세계의 실제값이 일치하는 정확성을 말합니다.")
                .workbook(workbook)
                .build();

        final Card seventhCard = Card.builder()
                .id(0L)
                .question("javascript의 Promise란 무엇인가요?")
                .answer("미래에 값을 반환할 수도 있는 함수를 캡슐화한 객체")
                .workbook(workbook)
                .build();

        final Card eighthCard = Card.builder()
                .id(0L)
                .question("javascript의 this란 무엇인가요?")
                .answer("실행중인 모든 함수가 가지고 있는 자신의 현재 실행 컨텍스트에 대한 참조")
                .workbook(workbook)
                .build();

        final Card ninthCard = Card.builder()
                .id(0L)
                .question("리액트는 Virtual DOM을 왜 사용할까요?")
                .answer("브라우저의 렌더링 과정을 최소화시키기 위해 View가 변경되면 Virtual DOM(메모리)에 바뀐 부분을 적용시키고, 모두 변경된 후에 Virtual DOM을 실제 DOM에 적용하기 위해 사용합니다.")
                .workbook(workbook)
                .build();

        final Card tenthCard = Card.builder()
                .id(0L)
                .question("자바에서 접근제한자로 어떤 것들이 있나요?")
                .answer("public, protected, default, private 이 있습니다.")
                .workbook(workbook)
                .build();

        return List.of(firstCard, secondCard, thirdCard, fourthCard, fifthCard,
                sixthCard, seventhCard, eighthCard, ninthCard, tenthCard);
    }
}
