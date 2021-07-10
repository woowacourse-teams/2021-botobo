import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import { Button, QuizResult } from '../components';
import { ROUTE } from '../constants';
import { Flex } from '../styles';

const quizzes = [
  {
    id: 1,
    question: '하하하하하하하하하하하하하하하하하하하하하하하하',
    answer: '호호호',
    categoryName: '카테고리',
    isChecked: false,
  },
  {
    id: 2,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
    isChecked: true,
  },
  {
    id: 4,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
    isChecked: false,
  },
  {
    id: 5,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
    isChecked: false,
  },
  {
    id: 6,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
    isChecked: true,
  },
  {
    id: 7,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
    isChecked: true,
  },
];

const QuizResultPage = () => {
  const history = useHistory();

  return (
    <Container>
      <span>총 7문제를 푸셨네요!</span>
      <br />
      <span>다음에 또 보고 싶은 문제를 선택해 주세요.</span>
      {quizzes && (
        <QuizResultList>
          {quizzes.map(({ id, question, categoryName, isChecked }) => (
            <li key={id}>
              <QuizResult
                question={question}
                categoryName={categoryName}
                isChecked={isChecked}
                setIsChecked={() => {
                  console.log();
                }}
              />
            </li>
          ))}
        </QuizResultList>
      )}
      <ButtonWrapper>
        <Button
          size="full"
          backgroundColor="white"
          shape="rectangle"
          onClick={() => history.push(ROUTE.HOME)}
        >
          저장 안하고 나가기
        </Button>
        <Button
          size="full"
          backgroundColor="green"
          shape="rectangle"
          onClick={() => history.push(ROUTE.HOME)}
        >
          다음에 또 보기
        </Button>
      </ButtonWrapper>
    </Container>
  );
};

const Container = styled.div`
  margin-bottom: 3rem;
`;

const QuizResultList = styled.ul`
  margin-top: 1rem;

  & > li {
    margin-bottom: 1rem;
  }
`;

const ButtonWrapper = styled.div`
  ${Flex()}
  opacity: 0.9;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
`;

export default QuizResultPage;
