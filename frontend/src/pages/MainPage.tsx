import styled from '@emotion/styled';
import React from 'react';

import { Category, QuizStarter } from '../components';

const categories = [
  { id: 1, name: 'Java', cardCount: 3 },
  { id: 2, name: 'React', cardCount: 5 },
  { id: 3, name: 'JS', cardCount: 14 },
];

const MainPage = () => (
  <>
    <QuizStarter />
    <EntireCategory>전체 카테고리</EntireCategory>
    <CategoryList>
      {categories.map(({ id, name, cardCount }) => (
        <li key={id}>
          <Category name={name} cardCount={cardCount} />
        </li>
      ))}
    </CategoryList>
  </>
);

const EntireCategory = styled.h2`
  font-size: ${({ theme }) => theme.fontSize.semiLarge};
  margin-top: 3rem;
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
