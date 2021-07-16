import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';
import { CategoryResponse } from '../types';

type PickedCategory = Pick<CategoryResponse, 'name' | 'cardCount'>;

interface Props extends PickedCategory {
  isChecked?: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const Category = ({ name, cardCount, isChecked, onClick }: Props) => (
  <Container isChecked={isChecked} onClick={onClick}>
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 문제</CardCount>
  </Container>
);

const Container = styled.div<Pick<Props, 'isChecked'>>`
  ${Flex({ direction: 'column' })};
  cursor: pointer;
  padding: 1rem;
  height: 100%;

  ${({ theme, isChecked }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
  `}
`;

const Name = styled.span`
  margin: 0.3rem 0;
  word-wrap: break-word;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.semiLarge};
    font-weight: ${theme.fontWeight.normal};
  `};
`;

const CardCount = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.gray_6};
  `};
`;

export default Category;
