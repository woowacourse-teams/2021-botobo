import styled from '@emotion/styled';
import React from 'react';

import { Button, Category, QuizStarter } from '../components';
import { Flex } from '../styles';

const categories = [
  { id: 1, name: 'Java', cardCount: 3 },
  { id: 2, name: 'React', cardCount: 5 },
  { id: 3, name: 'JS', cardCount: 14 },
];

const MainPage = () => (
  <>
    <QuizStarter />
    <CategoryWrapper>
      <CategoryTitle>전체 카테고리</CategoryTitle>
      <Button shape="circle" backgroundColor="white" hasShadow={true}>
        <i className="fas fa-plus"></i>
      </Button>
    </CategoryWrapper>
    <CategoryList>
      {categories.map(({ id, name, cardCount }) => (
        <li key={id}>
          <Category name={name} cardCount={cardCount} />
        </li>
      ))}
    </CategoryList>
  </>
);

const CategoryWrapper = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })}
  margin-top: 3rem;
`;

const CategoryTitle = styled.h2`
  font-size: ${({ theme }) => theme.fontSize.semiLarge};
`;

const CategoryList = styled.ul`
  display: grid;
  grid-template-columns: repeat(2, calc(50% - 0.5rem));
  gap: 1rem;
  margin: 1rem 0;

  & > li {
    height: 9.5rem;
  }
`;

export default MainPage;
