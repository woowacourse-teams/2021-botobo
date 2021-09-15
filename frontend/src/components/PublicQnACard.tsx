import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';
import { CardResponse } from '../types';
import CardTemplate from './CardTemplate';

type PickedCardResponse = Pick<CardResponse, 'question' | 'answer'>;

interface Props extends PickedCardResponse {
  isChecked?: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const PublicQnACard = ({ question, answer, isChecked, onClick }: Props) => (
  <CardTemplate isChecked={isChecked} onClick={onClick}>
    <Question>
      <span>Q.</span> {question}
    </Question>
    <Answer>
      <span>A.</span> {answer}
    </Answer>
  </CardTemplate>
);

const Question = styled.div`
  ${Flex()};
  padding: 1rem 0;
  line-height: 1.5;

  & > span {
    margin-right: 0.3rem;
  }

  ${({ theme }) => css`
    border-bottom: 2px solid ${theme.color.gray_3};
  `}
`;

const Answer = styled.div`
  ${Flex()};
  padding-top: 1.5rem;
  padding-bottom: 1rem;
  line-height: 1.5;

  & > span {
    margin-right: 0.3rem;
  }
`;

export default PublicQnACard;
