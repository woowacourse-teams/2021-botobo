import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, MainHeader, QuizResult } from '../components';
import { ROUTE } from '../constants';
import { useQuizResult, useRouter } from '../hooks';
import { Flex } from '../styles';
import { timeConverter } from '../utils';
import PageTemplate from './PageTemplate';

const QuizResultPage = () => {
  const {
    quizResults,
    quizTime,
    hasQuizTime,
    checkedCount,
    checkQuizResult,
    setNextQuiz,
  } = useQuizResult();
  const { routeMain } = useRouter();

  return (
    <>
      <MainHeader />
      <StyledPageTemplate isScroll={true}>
        <TopContent>
          <h2>{ROUTE.QUIZ_RESULT.TITLE}</h2>
          {hasQuizTime && (
            <Time>{timeConverter(quizTime)} 동안 학습했어요.</Time>
          )}
        </TopContent>
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
      </StyledPageTemplate>
    </>
  );
};

const StyledPageTemplate = styled(PageTemplate)`
  margin-bottom: 3rem;
`;

const TopContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'flex-end' })};
  margin-bottom: 1rem;
`;

const Time = styled.span`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
  `}
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
  width: 100%;
  left: 50%;
  transform: translateX(-50%);

  ${({ theme }) => css`
    max-width: ${theme.responsive.maxWidth};
  `}
`;

export default QuizResultPage;
