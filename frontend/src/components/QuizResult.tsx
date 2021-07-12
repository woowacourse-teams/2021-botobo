import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { QuizResponse } from '../types';

interface Props extends Omit<QuizResponse, 'id' | 'answer'> {
  isChecked: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

interface ContainerStyleProps {
  isChecked: boolean;
}

const QuizResult = ({ question, categoryName, isChecked, onClick }: Props) => (
  <Container isChecked={isChecked} onClick={onClick}>
    <CategoryName>{categoryName}</CategoryName>
    <span>Q. {question}</span>
  </Container>
);

const Container = styled.div<ContainerStyleProps>`
  cursor: pointer;
  height: 7rem;
  padding: 1rem;

  ${({ theme, isChecked }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.card};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
  `};
`;

const CategoryName = styled.span`
  display: block;
  margin-bottom: 1rem;

  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-size: ${theme.fontSize.small};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

export default QuizResult;
