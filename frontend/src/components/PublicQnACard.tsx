import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { CardResponse } from '../types';
import CardTemplate from './CardTemplate';

type PickedCardResponse = Pick<CardResponse, 'question' | 'answer'>;

interface Props extends PickedCardResponse {
  isChecked?: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const PublicQnACard = ({ question, answer, isChecked, onClick }: Props) => (
  <CardTemplate isChecked={isChecked} onClick={onClick}>
    <Question>Q. {question}</Question>
    <Answer>A. {answer}</Answer>
  </CardTemplate>
);

const Question = styled.div`
  padding: 1rem 0;

  ${({ theme }) => css`
    border-bottom: 2px solid ${theme.color.gray_3};
  `}
`;

const Answer = styled.div`
  padding-top: 1.5rem;
  padding-bottom: 1rem;
`;

export default PublicQnACard;
