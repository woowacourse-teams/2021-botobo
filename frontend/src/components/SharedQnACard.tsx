import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { CardResponse } from '../types';

type PickedCardResponse = Omit<CardResponse, 'id'>;

interface Props extends PickedCardResponse {
  isChecked?: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const SharedQnACard = ({ question, answer, isChecked, onClick }: Props) => (
  <Container isChecked={isChecked} onClick={onClick}>
    <Question>Q. {question}</Question>
    <Answer>A. {answer}</Answer>
  </Container>
);

const Container = styled.div<Pick<Props, 'isChecked'>>`
  padding: 2rem 1rem;
  margin-top: 2rem;
  word-break: break-all;

  ${({ theme, isChecked }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.card};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
  `};
`;

const Question = styled.div`
  padding-bottom: 1rem;

  ${({ theme }) => css`
    border-bottom: 2px solid ${theme.color.gray_3};
  `}
`;

const Answer = styled.div`
  padding-top: 1.5rem;
`;

export default SharedQnACard;
