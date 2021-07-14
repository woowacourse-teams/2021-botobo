import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import EmptyStarIcon from '../assets/star-empty.svg';
import FillStarIcon from '../assets/star-fill.svg';
import { CardResponse } from '../types';

const QnACard = ({
  question,
  answer,
  isBookmark,
}: Omit<CardResponse, 'id'>) => (
  <Container>
    <Header>
      <BookmarkButton>
        {isBookmark ? <FillStarIcon /> : <EmptyStarIcon />}
      </BookmarkButton>
    </Header>
    <Question>Q. {question}</Question>
    <Answer>A. {answer}</Answer>
    <Footer>
      <button>수정</button>
      <button>삭제</button>
    </Footer>
  </Container>
);

const Container = styled.div`
  min-height: 3rem;
  padding: 1rem;
  margin-top: 2rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.card};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.card};
  `};
`;

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

const Footer = styled.div`
  margin-top: 1rem;
  text-align: right;

  & > button {
    margin-left: 1rem;
  }
`;

export default QnACard;
