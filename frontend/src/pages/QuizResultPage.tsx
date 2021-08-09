import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, MainHeader, QuizResult } from '../components';
import { ROUTE } from '../constants';
import { useQuizResult, useRouter } from '../hooks';
import { Flex } from '../styles';

const QuizResultPage = () => {
  const { quizResults, checkedCount, checkQuizResult, setNextQuiz } =
    useQuizResult();
  const { routeMain } = useRouter();

  return (
    <>
      <MainHeader />
      <Container>
        <Title>{ROUTE.QUIZ_RESULT.TITLE}</Title>
        <span>총 {quizResults.length}문제를 푸셨네요!</span>
        <br />
        <span>다음에 또 보고 싶은 문제를 선택해 주세요.</span>
        <QuizResultList>
          {quizResults.map(
            ({ id, question, workbookName, encounterCount, isChecked }) => (
              <li key={id}>
                <QuizResult
                  question={question}
                  workbookName={workbookName}
                  encounterCount={encounterCount}
                  isChecked={isChecked}
                  onClick={() => checkQuizResult(id)}
                />
              </li>
            )
          )}
        </QuizResultList>
        <ButtonWrapper>
          <Button
            size="full"
            backgroundColor="white"
            color="green"
            shape="rectangle"
            onClick={routeMain}
          >
            저장 안하고 나가기
          </Button>
          <Button
            size="full"
            shape="rectangle"
            onClick={setNextQuiz}
            backgroundColor={checkedCount > 0 ? 'green' : 'gray_4'}
            disabled={checkedCount === 0}
          >
            다음에 또 보기
          </Button>
        </ButtonWrapper>
      </Container>
    </>
  );
};

const Container = styled.div`
  margin-bottom: 3rem;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const Title = styled.h2`
  margin-bottom: 1rem;
`;

const QuizResultList = styled.ul`
  margin-top: 1rem;

  & > li {
    margin-bottom: 1rem;
  }
`;

const ButtonWrapper = styled.div`
  ${Flex()};
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
`;

export default QuizResultPage;
