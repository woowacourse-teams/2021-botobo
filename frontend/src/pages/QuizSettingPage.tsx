import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import { Button, CategoryList } from '../components';
import { ROUTE } from '../constants';
import { useQuizSetting } from '../hooks';

const QuizSettingPage = () => {
  const { categories, checkCategory } = useQuizSetting();
  const history = useHistory();

  return (
    <Container>
      <Title>퀴즈 설정</Title>
      <span>어떤 문제를 풀어볼까요?</span>
      <CategoryWrapper>
        <CategoryList categories={categories} onClickCategory={checkCategory} />
      </CategoryWrapper>
      <Button
        onClick={() => history.push(ROUTE.QUIZ)}
        backgroundColor="green"
        size="full"
      >
        시작!
      </Button>
    </Container>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const Title = styled.h2`
  font-size: ${({ theme }) => theme.fontSize.semiLarge};
  margin-top: 1rem;
  margin-bottom: 2rem;
`;

const CategoryWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 2.5rem;
`;

export default QuizSettingPage;
