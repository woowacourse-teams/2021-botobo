import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CategoryList } from '../components';
import { useQuizSetting } from '../hooks';

const QuizSettingPage = () => {
  const { categories, checkCategory, startQuiz } = useQuizSetting();

  return (
    <Container>
      <span>어떤 문제를 풀어볼까요?</span>
      <CategoryWrapper>
        <CategoryList categories={categories} onClickCategory={checkCategory} />
      </CategoryWrapper>
      <Button onClick={startQuiz} size="full">
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

const CategoryWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 2.5rem;
`;

export default QuizSettingPage;
