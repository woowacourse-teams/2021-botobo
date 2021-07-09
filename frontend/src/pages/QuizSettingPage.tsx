import styled from '@emotion/styled';
import React from 'react';

import { Button, CategoryList } from '../components';

const categories = [
  { id: 1, name: 'Java', description: '', cardCount: 2, logoUrl: '' },
  { id: 2, name: 'React', description: '', cardCount: 12, logoUrl: '' },
  { id: 3, name: 'JS', description: '', cardCount: 34, logoUrl: '' },
];

const QuizSettingPage = () => (
  <>
    <Title>퀴즈 설정</Title>
    <span>어떤 문제를 풀어볼까요?</span>
    <CategoryWrapper>
      {categories && <CategoryList categories={categories} />}
    </CategoryWrapper>
    <Button backgroundColor="pink" size="full">
      시작!
    </Button>
  </>
);

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
