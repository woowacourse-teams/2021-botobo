import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import EmptyStarIcon from '../assets/star-empty.svg';
// import FillStarIcon from '../assets/star-fill.svg';
import { CardResponse } from '../types';
import CardTemplate from './CardTemplate';

const QnACard = ({
  question,
  answer,
}: Pick<CardResponse, 'question' | 'answer'>) => (
  <CardTemplate editable={true}>
    <Header>
      <BookmarkButton>
        <EmptyStarIcon />
      </BookmarkButton>
    </Header>
    <Question>Q. {question}</Question>
    <Answer>A. {answer}</Answer>
  </CardTemplate>
);

const Header = styled.div`
  text-align: right;
  height: 1.5rem;
  margin-bottom: 1rem;
`;

const BookmarkButton = styled.button`
  & > svg {
    height: 1.5rem;
    width: 1.5rem;
  }
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

export default QnACard;
