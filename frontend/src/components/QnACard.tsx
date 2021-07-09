import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import { Flex } from '../styles';
import { QuizResponse } from '../types/api';

interface Props extends Omit<QuizResponse, 'id'> {
  isChecked?: boolean;
  setIsChecked?: () => void;
}

interface CardStyleProps {
  isFlipped: boolean;
}

interface CheckStyleProps {
  isChecked: boolean;
}

const QnACard = ({
  question,
  answer,
  categoryName,
  isChecked,
  setIsChecked,
}: Props) => {
  const [isFlipped, setIsFlipped] = useState(false);

  return (
    <Container
      onClick={() => (setIsChecked ? setIsChecked() : setIsFlipped(!isFlipped))}
    >
      <Card isFlipped={isFlipped}>
        <Question>
          <CardHeader>
            <CategoryName>{categoryName}</CategoryName>
            {isChecked !== undefined && (
              <Check isChecked={isChecked}>
                <i className="fas fa-check"></i>
              </Check>
            )}
          </CardHeader>
          <span>Q. {question}</span>
        </Question>
        <Answer>
          <CardHeader>
            <CategoryName>{categoryName}</CategoryName>
          </CardHeader>
          <span>A. {answer}</span>
        </Answer>
      </Card>
    </Container>
  );
};

const Container = styled.div`
  height: 12.5rem;
  width: 100%;
  perspective: 50rem;
`;

const Card = styled.div<CardStyleProps>`
  height: 100%;
  width: 100%;
  transition: transform 0.5s ease;
  transform-style: preserve-3d;
  cursor: pointer;
  position: relative;

  ${({ isFlipped }) =>
    isFlipped &&
    css`
      transform: rotateX(180deg);
    `}

  & > div {
    ${Flex({ justify: 'center', items: 'center' })};
    position: absolute;
    padding: 1rem;
    padding-top: 2rem;
    width: 100%;
    height: 100%;
    -webkit-backface-visibility: hidden;
    backface-visibility: hidden;

    ${({ theme }) =>
      css`
        box-shadow: ${theme.boxShadow.card};
        border-radius: ${theme.borderRadius.square};
      `}
  }
`;

const CategoryName = styled.span`
  font-weight: bold;

  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-size: ${theme.fontSize.default};
  `}
`;

const Check = styled.span<CheckStyleProps>`
  ${({ theme, isChecked }) => css`
    color: ${isChecked ? theme.color.green : theme.color.gray_4};
  `};
`;

const Question = styled.div`
  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `}
`;

const CardHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  position: absolute;
  top: 1rem;
  width: 90%;
`;

const Answer = styled.div`
  transform: rotateX(180deg);

  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `}
`;

export default QnACard;
