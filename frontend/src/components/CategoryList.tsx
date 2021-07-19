import styled from '@emotion/styled';
import React from 'react';

import { CategoryResponse } from '../types';
import Category from './Category';

interface CategoryProp extends CategoryResponse {
  isChecked?: boolean;
}

interface Props {
  categories: CategoryProp[];
  onClickCategory: (id: number) => void;
}

const CategoryList = ({ categories, onClickCategory }: Props) => (
  <StyledUl>
    {categories.map(({ id, name, cardCount, isChecked }) => (
      <li key={id}>
        <Category
          name={name}
          cardCount={cardCount}
          isChecked={isChecked}
          onClick={() => onClickCategory(id)}
        />
      </li>
    ))}
  </StyledUl>
);

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
  margin: 1rem 0;
`;

export default CategoryList;
