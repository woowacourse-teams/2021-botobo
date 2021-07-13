import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Quiz } from '../components';
import { useQuiz } from '../hooks';
import { Flex } from '../styles';

interface TooltipProps {
  isTooltipVisible: boolean;
}
interface QuizListProps {
  quizCount: number;
  currentIndex: number;
}

interface QuizItemProps {
  quizIndex: number;
}

const QuizPage = () => {
  const { quizzes, prevQuizId, currentQuizIndex, showNextQuiz, showPrevQuiz } =
    useQuiz();

  return (
    <Container>
      <QuizWrapper>
        <Tooltip isTooltipVisible={currentQuizIndex === 0}>
          카드를 클릭해 질문과 정답을 확인할 수 있어요.
        </Tooltip>
        {quizzes && (
          <QuizList quizCount={quizzes.length} currentIndex={currentQuizIndex}>
            {quizzes.map(({ id, question, answer, categoryName }, index) => (
              <QuizItem key={id} quizIndex={index}>
                <Quiz
                  question={question}
                  answer={answer}
                  categoryName={categoryName}
                  isChanged={id === prevQuizId}
                />
              </QuizItem>
            ))}
          </QuizList>
        )}
      </QuizWrapper>
      <PageNation>
        <button onClick={showPrevQuiz}></button>
        <Page>
          {currentQuizIndex + 1} / {quizzes.length}
        </Page>
        <button onClick={showNextQuiz}></button>
      </PageNation>
    </Container>
  );
};

const Container = styled.div`
  ${Flex({ justify: 'center', items: 'center', direction: 'column' })};
  width: 100%;
  overflow: hidden;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      height: ${theme.pageSize.height};
    `}
`;

const QuizWrapper = styled.div`
  position: relative;
  width: 100%;
`;

const Tooltip = styled.div<TooltipProps>`
  ${Flex({ justify: 'center', items: 'center' })};
  position: absolute;
  width: 100%;
  height: 2.5rem;
  top: -3rem;
  margin-bottom: 0.5rem;
  transition: opacity 0.1s ease;

  ${({ theme, isTooltipVisible }) => css`
    background-color: ${theme.color.indigo};
    border-radius: ${theme.borderRadius.square};
    opacity: ${isTooltipVisible ? 1 : 0};
    font-size: ${theme.fontSize.small};
    color: ${theme.color.white};
  `}
`;

const QuizList = styled.ul<QuizListProps>`
  ${Flex()};
  transition: transform 0.15s ease;

  ${({ quizCount, currentIndex }) => css`
    width: calc(100% * ${quizCount});
    transform: translateX(
      calc(-${(100 / quizCount) * currentIndex}% - ${1.25 * currentIndex}rem)
    );

    & > li {
      width: calc(100% / ${quizCount});
    }
  `};
`;

const QuizItem = styled.li<QuizItemProps>`
  ${({ quizIndex }) => css`
    transform: translateX(${1.25 * quizIndex}rem);
  `};
`;

const PageNation = styled.div`
  ${Flex({ items: 'center' })};
  margin-top: 5rem;

  & > button {
    background-color: transparent;
    ${({ theme }) =>
      css`
        font-size: ${theme.fontSize.medium};
      `}
  }

  svg {
    height: inherit;
  }
`;

const Page = styled.span`
  margin: 0 1.5rem;
`;

export default QuizPage;
