-- 데이터 베이스에 아무것도 없는 경우 초기 데이터 셋팅용으로만 사용해야함 !!!

-- 필요하면 AUTO_INCREMENT 초기화
ALTER TABLE user AUTO_INCREMENT = 1;

-- 어드민 생성
INSERT INTO user (github_id, user_name, profile_url, role, created_at, updated_at) VALUES (88036280, "1번 어드민", 'botobo.profile.url', 'ADMIN', now(), now());
INSERT INTO user (github_id, user_name, profile_url, role, created_at, updated_at) VALUES (88143445, "일반 유저", 'botobo.profile.url', 'USER', now(), now());
UPDATE user SET user_name = 'botobo-admin', profile_url = 'https://avatars.githubusercontent.com/u/88036280?v=4' where id = 1;
UPDATE user SET user_name = 'botoboUser', profile_url = 'https://avatars.githubusercontent.com/u/88143445?v=4' where id = 2;

-- 문제집 생성
INSERT INTO workbook (name, user_id, opened, deleted, created_at, updated_at) VALUES ('데이터베이스', 1, true, false, now(), now());
INSERT INTO workbook (name, user_id, opened, deleted, created_at, updated_at) VALUES ('자바', 1, true, false   , now(), now());
INSERT INTO workbook (name, user_id, opened, deleted, created_at, updated_at) VALUES ('자바스크립트', 1, true, false, now(), now());
INSERT INTO workbook (name, user_id, opened, deleted, created_at, updated_at) VALUES ('네트워크', 1, true, false, now(), now());
INSERT INTO workbook (name, user_id, opened, deleted, created_at, updated_at) VALUES ('리액트', 1, true, false, now(), now());
INSERT INTO workbook (name, user_id, opened, deleted, created_at, updated_at) VALUES ('스프링', 1, true, false, now(), now());

-- 카드 생성
-- 데이터 베이스
INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '인덱스(index)란 무엇인가요?',
    '인덱스는 데이터분야에 있어서 테이블에 대한 동작의 속도를 높여주는 자료 구조를 말한다. 인덱스는 테이블 내의 1개의 컬럼, 혹은 여러 개의 컬럼을 이용하여 생성될 수 있다. 고속의 검색 동작 뿐만 아니라 레코드 접근과 관련하여 효율적인 순서 매김 동작에 대한 기초를 제공한다.',
    0, false, false, 1, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '트랜잭션(Transaction)이란 무엇인가요?',
    '하나의 논리적인 기능을 수행하기 위한 작업 단위로 데이터베이스의 일관된 상태를 또 다른 일관된 상태로 변환시키는 기능을 수행한다.',
    0, false, false, 1, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '관계형 데이터베이스 관리 시스템(RDBMS)를 정의하세요.',
    '관계형 데이터베이스 관리 시스템 (RDBMS)은 데이터베이스에 별도의 테이블에 저장된 관계형 데이터 모델을 기반으로하며 공통 열의 사용과 관련이 있다. SQL (Structured Query Language)을 사용하여 관계형 데이터베이스에서 데이터에 쉽게 액세스 할 수 있다.',
    0, false, false, 1, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '데이터베이스 무결성이란 무엇인가요?',
    '데이터 베이스에 저장된 데이터 값과 그것이 표현하는 현실 세계의 실제값이 일치하는 정확성을 말합니다.',
    0, false, false, 1, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '데이터베이스 정규화란 무엇인가요?',
    '자료의 손실이나 불필요한 정보의 도입 없이 데이터의 일관성, 데이터 중복을 최소화하고 최대의 데이터 안정성 확보를 위한 안정적 자료 구조로 변환하기 위해서 하나의 테이블을 둘 이상을 분리하는 작업이다.',
    0, false, false, 1, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '트랜잭션의 4가지 성질에 대해 설명해주세요.',
    'Atomicity(원자성) 는 트랜잭션의 연산이 DB에 모두 반영되던지 전혀 반영이되지 않던지 둘중에 하나만 수행해야한다.\nConsistency(일관성) 는 트랜잭션이 성공적으로 완료된 후에는 언제나 일관성 있는 DB상태로 변환되어야한다.\nIsolation(독립성) 은 수행중인 트랜잭션이 완전히 완료되기 전에는 다른 트랙잭션에서 수행 결과를 참조할 수 없다.\nDurablility(지속성) 는 성공적으로 완료된 트랜잭션의 결과는 시스템이 고장나더라도 영구적으로 반영되어야 한다.',
    0, false, false, 1, false, now(), now()
);


INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '데이터베이스에서 데드락(Dead Lock)이란 무엇인가요?',
    '2개 이상의 트랜잭션이 특정 자원(테이블 또는 행)의 잠금(Lock)을 획득한 채 다른 트랜잭션이 소유하고 있는 잠금을 요구하면 아무리 기다려도 상황이 바뀌지 않는 상태가 되는데 이를 교착상태 라고 한다.',
    0, false, false, 1, false, now(), now()
);


INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '트랜잭션에서 격리 수준(Isolation Level)은 무엇이고 종류를 설명해주세요.',
    '격리 수준은 트랜잭션에서 일관성이 없는 데이터를 허용하도록 하는 수준을 말한다.\n\nRead Uncommitted(레벨 0): 한 트랜잭션에서 커밋하지 않은 데이터에 다른 트랜잭션의 접근이 가능하다. 즉 커밋하지 않은 데이터를 읽을 수 있다. 해당 격리 수준은 모든 문제에서 발생 가능성이 존재하지만 처리 성능은 가장 높다.\nRead Committed(레벨 1): Committed가 완료된 데이터만 읽을 수 있다. 이는 Dirty Read가 발생할 여지가 없으나, Read Uncommitted 수준보다 동시 처리 성능은 떨어진다. 다만 Non-Repeatable Read, Phantom Read가 발생 가능하다. 데이터베이스들은 보통 Read Committed를 디폴트 수준으로 지정한다.\nRepeatable Read(레벨 2): 트랜잭션 내에서 조회한 데이터를 반복해서 조회해도 같은 데이터가 조회된다. 이는 개별 데이터 이슈인 Dirty Read나 Non-Repeatable Read는 발생하지 않지만, 결과 집합 자체가 달라지는 Phantom Read는 발생 가능 하다.\nSerializable(레벨 3): 가장 엄격한 격리 수준. 3가지 문제점을 모두 커버할 수 있다. 다만 동시 처리 성능은 급격히 떨어질 수 있다.',
    0, false, false, 1, false, now(), now()
);


INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '데이터베이스 락(Lock)이란 무엇인가요?',
    '데이터베이스 연산(read/write)을 수행하기 전에 해당 데이터에 먼저 lock 연산을 실행하여 독점권을 획득하는 방식으로 트랜잭션의 직렬가능성을 보장하는 방식이다. 병행 수행되는 트랜잭션들이 동일한 데이터에 동시에 접근하지 못하도록 lock과 unlock이라는 2개의 연산을 이용해 제어한다. 기본 원리는 먼저 접근한 데이터에 대한 연산을 다 마칠 때까지, 해당 데이터에 다른 트랜잭션이 접근하지 못하도록 상호배제하여 직렬가능성을 보장하는 것이다.',
    0, false, false, 1, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
   'COMMIT과 ROLLBACK에 대해 설명해주세요.',
   'COMMIT은 해당 트랜잭션으로 반영된 데이터베이스 변경사항을 저장 하는 것이고 ROLLBACK은 해당 트랜잭션으로 반영된 데이터베이스 변경사항을 취소 하는 것이다.',
   0, false, false, 1, false, now(), now()
);

-- 자바
INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'java에서 String 클래스가 불변이어서 얻는 이점으로 무엇이 있을까요?',
    '1. 멀티 쓰레딩 환경에서 값이 변하지 않는다 \n 2. String 자료형은 비밀번호나 개인 정보와 같은 민감한 정보를 담는데, 값이 변하면 보안상의 문제를 야기할 수 있다. \n 3. 해시 자료구조에서 String은 key로 자주 사용된다. 이 때 값이 변하면 예상치 못한 문제가 발생할 수 있다. \n 4. String Pool에서 관리하며 같은 String 값을 필요로 할 때 새로운 String을 생성하지 않고 재활용할 수 있다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '싱글톤 클래스란 무엇이고, 왜 사용될까요?',
    '싱글톤 클래스란 인스턴스를 한 개만 생성할 수 있는 클래스를 말합니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'double과 float 데이터 타입의 차이는 무엇일까요?',
    'float형 데이터 타입은 메모리에서 4 바이트를 차지하고 double형 데이터 타입은 8 바이트를 차지합니다. 바이트 수의 차이가 생기는 이유는 정밀도 때문인데, float은 유효자릿수가 7이고, double은 유효자릿수가 16입니다. 따라서 실수를 더욱 정밀하게 다루고 싶다면 double형 데이터 타입을 사용해야합니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '원시타입(primitive type)과 래퍼 클래스(wrapper class)는 어떤 점에서 다른가요?',
    '1. 래퍼 클래스는 null 값을 가질 수 있지만, 원시타입은 null이 될 수 없다. \n 2. 컬렉션(Collection)에서 사용하기 위해서는 원시타입을 래퍼 클래스로 박싱해야한다. \n 3. 래퍼 클래스가 가지고 있는 값을 연산에 활용하기 위해서는 메서드를 사용하거나 값을 꺼내야한다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '추상 클래스와 인터페이스의 차이는 무엇일까요?',
    '인터페이스는 메서드를 선언만하고 메서드의 구현체를 포함할 수 없지만, 추상 클래스는 구현체가 없는 추상 메서드를 포함하거나 구현체가 있는 일반적인 메서드를 포함합니다. 또 다른 차이점으로는 인터페이스를 구현한 클래스는 인터페이스에서 선언한 모든 메서드를 구현해야 하지만 추상 클래스를 상속 받은 클래스는 그 추상 클래스의 모든 메서드를 다시 구현할 필요가 없습니다. 마지막으로, 하나의 클래스는 여러 인터페이스를 구현할 수 있지만 상속은 하나의 추상 클래스로부터만 받을 수 있습니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'checked exception과 unchecked exception의 차이가 무엇인가요?',
    'Checked Exception은 컴파일 하는 과정에서 확인되는 예외입니다. 그 예외가 발생했을 경우에는 예외를 반드시 처리해야합니다. Unchecked Exception은 꼭 처리하지 않아도 되는 예외입니다. Checked Exception의 예시로는 IOException, 그리고 Unchecked Exception의 예시로는 Runtime Exception이 있습니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '자바에서 접근제한자로 어떤 것들이 있나요?',
    'public, protected, default, private 이 있습니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '자바에서 변수에 final 키워드를 붙이면 어떻게 되나요?',
    '변수가 final로 선언되면 그 변수의 값을 재할당할 수 없습니다. 마치 상수처럼 되어버립니다. 하지만 해당 변수에 할당된 객체의 상태는 변경될 수 있습니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '상속을 써서 얻을 수 있는 이점이 무엇인가요?',
    '같은 기능을 하는 클래스들이 있을 경우, 그 기능을 구현한 코드를 한 부모 클래스에 넣어 중복되는 코드를 줄이며 재사용 가능한 구조를 만들 수 있다는 이점이 있습니다.',
    0, false, false, 2, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Stack과 Queue의 차이를 간단하게 설명해주세요.',
    'Stack과 Queue 둘 다 정보를 담는 자료구조임은 같습니다. 그러나 Stack은 마지막으로 넣은 자료를 가장 먼저 꺼낼 수 있는 Last In First Out (LIFO) 구조이며, Queue는 처음으로 넣은 자료를 먼저 꺼낼 수 있는 First In First Out (FIFO) 구조입니다.',
    0, false, false, 2, false, now(), now()
);

-- 자바스크립트
INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'JS의 원시타입',
    'Undefined, Null, Boolean, Number, BigInt, String, Symbol',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '일급 객체',
    '다른 객체에 일반적으로 적용 가능한 모든 연산을 지원하는 객체이며 Object type인 JS 함수의 특징을 활용한다.',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '호이스팅',
    '변수 / 함수의 선언부가 위치한 인접 스코프의 시작 지점에서 해당 식별자의 관측이 가능한 현상',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '클로저',
    '함수가 속한 렉시컬 스코프를 기억하여 함수가 렉시컬 스코프 밖에서 실행될 때도 그 스코프의 외부에 접근할 수 있게 하는 기능',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'this',
    '실행중인 모든 함수가 가지고 있는 자신의 현재 실행 컨텍스트에 대한 참조',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'this 바인딩 방식',
    'Default binding, Implicit binding, Explicit binding, new binding',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '프로토타입',
    'JS 객체가 서로의 기능을 상속하는 것을 가능하게 하는 메커니즘',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Iteration Protocol',
    '객체 내부의 반복 동작을 수행하기 위해 정립된 규칙이다.\nIteration Protocol의 하위 규칙은 iterable protocol과 iterator protocol이 있다.',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '동기와 비동기',
    '동기란 순차적으로 한번에 하나의 task 가 수행되는 방식이며 비동기란 한번에 여러 개의 task 가 서로의 흐름을 방해하지 않고 수행되는 방식.',
    0, false, false, 3, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Promise',
    '미래에 값을 반환할 수도 있는 함수를 캡슐화한 객체',
    0, false, false, 3, false, now(), now()
);

-- 네트워크
INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'HTTP란 무엇인가요?',
    'HyperText Transfer Protocol이란 서버/클라이언트 모델을 따라 데이터를 주고받기 위한 프로토콜로서 80번 포트를 사용한다. TCP/IP 위에서 작동하며 상태를 가지지 않는 Stateless 프로토콜이다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'HTTP와 HTTPS의 차이점을 설명해주세요.',
    'HTTPS는 HTTP에 데이터 암호화가 추가된 프로토콜이다. HTTPS는 80번 포트를 사용하는 HTTP와 달리 443 포트를 사용하며, 네트워크 상에서 중간에 제 3자가 정보를 볼 수 없도록 공개키 암호화를 지원하고 있다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'HTTP Method의 종류에 대해 설명해주세요.',
    'GET, POST, UPDATE, DELETE, PUT',
    0, false, false, 4, false, now(), now()
);


INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'UDP와 TCP의 차이점은 무엇인가요?',
    '가장 큰 차이점은 TCP는 연결지향형이며, UDP는 비연결지향형 프로토콜이다. TCP는 흐름 중심 프로토콜이며 통신을 주고받는 것을 중시함과 반대로 UDP는 데이터 중심 프로토콜로서 주고받는 통신보다 일방적으로 보내는 것을 중요시한다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'ISP란 무엇인가요?',
    'Internet Service Provider, 인터넷 서비스 공급자로 다양한 회선 상품을 제공하며 기업마다 서비스가 다르다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Cast의 세 종류에 대해 설명해주세요.',
    'Unicast: 1:1 통신, 원하는 대상 하나를 정해서 통신하는 것을 말한다. \n Multicast: 1:N 통신, 원하는 대상 여러 개를 정해서 통신하는 것을 말한다. \n Broadcast: 1:All 통신, 내 의지와 상관없이 무조건 받아들여야하는 통신을 말한다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'VPN이란 무엇인가요?',
    'Virtual Private Network, 즉 가상 사설망으로서 ISP에 정보를 넘겨주지 않고 익명성을 유지하여 인터넷에 접속할 수 있도록 해 주는 망을 의미한다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'IP란 무엇인가요?',
    'Internet Protocol Address로서 컴퓨터 네트워크에서 기기들이 서로를 인식하고 통신하기 위해 사용하는 식별 번호이다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '패킷이란 무엇인가요?',
    '네트워크 상에서 전송하는 데이터를 일정한 크기로 자른 작게 나뉘어진 데이터의 묶음을 패킷이라 한다. 누구에게, 어디로, 무엇을 보내야하는지에 대한 정보가 담겨있다.',
    0, false, false, 4, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '로드 밸런싱이란 무엇인가요?',
    '분산식 웹 서비스로 여러 서버에 부하를 나누어주는 것을 말한다. Round Robin, Least Connection, Response Time, Hash등의 기법이 존재한다.',
    0, false, false, 4, false, now(), now()
);

-- 리액트
INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '리액트란?',
    '자바스크립트의 선언적인 컴포넌트 기반 UI 라이브러리이다.(SPA)',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Virtual DOM은 무엇인가요?',
    'Virtual DOM이란 DOM의 상태를 객체 형태로 추상화시킨 개념이다.',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '리액트는 Virtual DOM을 왜 사용할까요?',
    '브라우저의 렌더링 과정을 최소화시키기 위해 View가 변경되면 Virtual DOM(메모리)에 바뀐 부분을 적용시키고, 모두 변경된 후에 Virtual DOM을 실제 DOM에 적용하기 위함',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '재조정이란 무엇인가요?',
    '상태가 변경되었을 때 render가 실행되면서 새로운 element가 반환된다. 이때, 이전 element와 비교해 효과적으로 변경된 element만 실제 DOM에 업데이트하기 위한 과정이 재조정이다.',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Hooks가 무엇인가요?',
    '클래스 컴포넌트에서만 가능하던 상태와 생명주기, 성능 최적화 등 리액트에서 제공하는 여러 기능들을 함수 컴포넌트에서도 사용할 수 있게 해주는 API의 모음',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Key란 무엇인가요?',
    'Key는 React가 어떤 항목(element)을 변경, 추가 또는 삭제할지 식별하는 것을 돕는 개념이다.',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Context API란?',
    '불필요한 prop drilling을 방지하기 위해 사용되는 API (불필요하게 자식에게 props를 넘겨줘야 하는 일이 잦을 때 사용할 수 있다.)',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '불변성을 유지해야 하는 이유?',
    'state나 props의 변화를 객체를 모두 순회하면서 비교하는 것이 비효율적이기 때문에 객체의 참조 값만 비교하기 위함',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '제어 컴포넌트란?',
    '리액트에 값이 완전히 제어되는 컴포넌트로 input의 값을 관리하기 위해 많이 사용된다.',
    0, false, false, 5, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    '제어 컴포넌트를 사용해야 하는 이유는?',
    '값이 항상 리액트 생명주기 내에서 관리되기 때문에 믿을 수 있는 값으로 관리할 수 있다. 즉, single source of truth를 만족하기 때문이다.',
    0, false, false, 5, false, now(), now()
);

-- 스프링
INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Spring의 의존성 주입(DI)방법에 대해 말해주세요.',
    '크게 3가지 의존성 주입 방법이 존재합니다.\nField 주입: 필드에 @Autowired를 붙인다.\nSetter 주입: Setter에 @Autowired를 붙인다.\nConstructor 주입: 필요한 의존성을 인자로 가지는 생성자를 만든다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Field, Setter, Constructor 주입 방식 중 어떤 방식을 선호하나요?',
    '생성자 주입방식을 선호합니다. 다른 주입전략과 다르게 필드에 final을 붙일 수 있다는 점과 순환 의존성을 가질 경우 BeanCurrentlyInCreationException이 발생하여 문제 상황을 알 수 있기 때문입니다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Spring Bean을 등록하는 방법에 대해 말해라.',
    'JavaConfig 방식이라면 @Configuration를 붙인 클래스에 @Bean을 통해 빈을 등록하거나, 클래스 위에 @Component를 붙여 @ComponentScan한다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'ArgumentResolver는 무엇인가요?',
    'Controller 메서드의 인자를 검사하여 조건에 만족하는 경우 값을 가공하여 원하는 값을 전달할 수 있는 방법을 제공한다. HandlerMethodArgumentResolver 인터페이스를 구현하여 정의한다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Interceptor는 무엇인가요?',
    '인터셉터는 컨트롤러에서 요청을 핸들링 하기 전이나 후에 특정 동작을 할 수 있는 방법을 제공한다. HandlerInterceptor 인터페이스를 구현하여 정의한다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'DispatcherServlet이 무엇인가요?',
    'DispatcherServlet은 표현 계층 전면에서 HTTP 프로토콜을 통해 들어오는 모든 요청을 중앙집중식으로 처리하는 프론트 컨트롤러이다. DispatcherServlet은 Spring MVC의 핵심 요소이다. 클라이언트로부터 어떤 요청이 들어오면 서블릿 컨테이너이 요청을 받는다. 이때 공통 작업은 DispatcherServlet에서 처리하고, 이외 작업은 적절한 세부 컨트롤러로 위임한다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Spring과 Spring Boot의 차이는 무엇인가요?',
    '스프링 프레임워크는 기능이 많은만큼 환경설정이 복잡하다. 이에 어려움을 느끼는 사용자들을 위해 나온 것이 바로 스프링 부트다. 스프링 부트는 스프링 프레임워크를 사용하기 위한 설정의 많은 부분을 자동화하여 사용자가 정말 편하게 스프링을 활용할 수 있도록 돕는다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Spring Bean의 Scope에 대해 설명해주세요.',
    '빈 스코프는 빈이 존재할 수 있는 범위를 뜻하며 singleton, prototype, request, session, application이 있습니다. singleton은 기본 스코프로 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프입니다. prototype은 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프입니다. request는 웹 요청이 들어오고 나갈때까지 유지하는 스코프, session은 웹 세션이 생성, 종료할때까지, application은 웹 서블릿 컨텍스트와 같은 범위로 유지하는 스코프입니다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Spring에서 CORS 에러를 해결하기 위한 방법을 설명해주세요.',
    'Servlet Filter를 사용하여 커스텀한 Cors 설정하거나, WebMvcConfiguer를 구현한 Configuration 클래스를 만들어서 addCorsMappings()를 재정의할 수도 있고, 마지막으로 Spring Security에서 CorsConfigurationSource를 Bean으로 등록하고 config에 추가해줌으로써 해결할 수 있습니다.',
    0, false, false, 6, false, now(), now()
);

INSERT INTO card (question, answer, encounter_count, bookmark, next_quiz, workbook_id, deleted, created_at, updated_at)
VALUES (
    'Spring Security란 무엇인가요?',
    'Spring Security는 Java 애플리케이션에서 인증 및 권한 부여 방법을 제공하는 데 초점을 맞춘 Spring 프레임 워크의 별도 모듈입니다. 또한 CSRF 공격과 같은 대부분의 일반적인 Security 취약점을 처리합니다.',
    0, false, false, 6, false, now(), now()
);

-- 태그 생성
INSERT INTO tag (name, created_at, updated_at) VALUES ('데이터베이스', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('db', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('자바', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('java', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('자바스크립트', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('자스', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('네트워크', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('network', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('react', now(), now());
INSERT INTO tag (name, created_at, updated_at) VALUES ('spring', now(), now());

-- 문제집-태그 생성
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (1, 1, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (1, 2, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (2, 3, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (2, 4, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (3, 5, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (3, 6, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (4, 7, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (4, 8, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (5, 9, false, now(), now());
INSERT INTO workbook_tag (workbook_id, tag_id, deleted, created_at, updated_at) VALUES (6, 10, false, now(), now());
