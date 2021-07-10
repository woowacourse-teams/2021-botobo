import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useState } from 'react';

import { Quiz } from '../components';
import { Flex } from '../styles';

const quizzes = [
  {
    id: 1,
    question: '하하하하하하하하하하하하하하하하하하하하하하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 2,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 4,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 5,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 6,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 7,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
];

const QuizPage = () => {
  const [quizIndex, setQuizIndex] = useState(0);
  const { id, question, answer, categoryName } = quizzes[quizIndex];

  const showPrevQuiz = () => {
    if (quizIndex === 0) return;

    setQuizIndex((prevValue) => prevValue - 1);
  };

  const showNextQuiz = () => {
    if (quizIndex === quizzes.length - 1) return;

    setQuizIndex((prevValue) => prevValue + 1);
  };

  return (
    <Container>
      <Tooltip>
        <TooltipDescription>
          카드를 클릭해 질문과 정답을 확인할 수 있어요.
        </TooltipDescription>
      </Tooltip>
      <Quiz
        id={id}
        question={question}
        answer={answer}
        categoryName={categoryName}
      />
      <PageNation>
        <button onClick={showPrevQuiz}>
          <i className="fas fa-arrow-left"></i>
        </button>
        <Page>
          {quizIndex + 1} / {quizzes.length}
        </Page>
        <button onClick={showNextQuiz}>
          <i className="fas fa-arrow-right"></i>
        </button>
      </PageNation>
    </Container>
  );
};

const Container = styled.div`
  ${Flex({ justify: 'center', items: 'center', direction: 'column' })};
  width: 100%;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      height: ${theme.pageSize.height};
    `}
`;

const Tooltip = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 100%;
  height: 2.5rem;
  margin-bottom: 0.5rem;
  ${({ theme }) => css`
    background-color: ${theme.color.indigo};
    border-radius: ${theme.borderRadius.square};
  `}
`;

const TooltipDescription = styled.span`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
    color: ${theme.color.white};
  `}
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

// TODO: margin-bottom 안쓰고 맞추기
const Page = styled.span`
  margin: 0 1.5rem;
  margin-bottom: 3px;
`;

export default QuizPage;
