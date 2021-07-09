import styled from '@emotion/styled';
import React from 'react';

import { Button, CategoryList, QuizStarter } from '../components';
import { Flex } from '../styles';

const categories = [
  {
    id: 1,
    name: 'Java',
    description: '',
    cardCount: 2,
    logoUrl: '',
  },
  {
    id: 2,
    name: 'React',
    description: '',
    cardCount: 12,
    logoUrl: '',
  },
  {
    id: 3,
    name: 'JS',
    description: '',
    cardCount: 34,
    logoUrl: '',
  },
];

const MainPage = () => (
  <>
    <QuizStarter />
    <section>
      <CategoryWrapper>
        <CategoryTitle>전체 카테고리</CategoryTitle>
        <Button shape="circle" backgroundColor="white" hasShadow={true}>
          <i className="fas fa-plus"></i>
        </Button>
      </CategoryWrapper>
      {categories && <CategoryList categories={categories} />}
    </section>
  </>
);

const CategoryWrapper = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })}
  margin-top: 3rem;
`;

const CategoryTitle = styled.h2`
  font-size: ${({ theme }) => theme.fontSize.semiLarge};
`;

export default MainPage;
