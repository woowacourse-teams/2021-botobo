import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';

interface Props {
  name: string;
  cardCount: number;
  isChecked?: boolean;
  onClick: () => void;
}

const Category = ({ name, cardCount, isChecked, onClick }: Props) => (
  <Container isChecked={isChecked} onClick={onClick}>
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 문제</CardCount>
  </Container>
);

const Container = styled.div<Pick<Props, 'isChecked'>>`
  ${Flex({ direction: 'column' })}
  cursor: pointer;
  background-color: ${({ theme }) => theme.color.white};
  padding: 1rem;
  height: 100%;
  border-radius: ${({ theme }) => theme.borderRadius.square};
  box-shadow: ${({ theme, isChecked }) =>
    isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
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
  color: ${({ theme }) => theme.color.gray_6};
`;

export default Category;
