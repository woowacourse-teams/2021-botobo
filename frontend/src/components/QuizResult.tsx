import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { QuizResponse } from '../types/api';

interface Props extends Omit<QuizResponse, 'id' | 'answer'> {
  isChecked: boolean;
  setIsChecked: () => void;
}

interface ContainerStyleProps {
  isChecked: boolean;
}

const QuizResult = ({
  question,
  categoryName,
  isChecked,
  setIsChecked,
}: Props) => (
  <Container isChecked={isChecked} onClick={setIsChecked}>
    <CategoryName>{categoryName}</CategoryName>
    <span>Q. {question}</span>
  </Container>
);

const Container = styled.div<ContainerStyleProps>`
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
  font-weight: bold;
  display: block;
  margin-bottom: 1rem;

  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-size: ${theme.fontSize.small};
  `};
`;

export default QuizResult;
