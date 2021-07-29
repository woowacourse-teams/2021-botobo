import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import { QuizResponse } from '../types';

interface Props extends Omit<QuizResponse, 'id' | 'encounterCount'> {
  isChanged: boolean;
}

interface CardStyleProps {
  isFlipped: boolean;
}

const Quiz = ({ question, answer, workbookName, isChanged }: Props) => {
  const [isFlipped, setIsFlipped] = useState(false);

  useEffect(() => {
    setIsFlipped(false);
  }, [isChanged]);

  return (
    <Container onClick={() => setIsFlipped((prevValue) => !prevValue)}>
      <Card isFlipped={isFlipped}>
        <Question>
          <WorkbookName>{workbookName}</WorkbookName>
          <Text>
            <span>Q. {question}</span>
          </Text>
        </Question>
        <Answer>
          <WorkbookName>{workbookName}</WorkbookName>
          <Text>
            <span>A. {answer}</span>
          </Text>
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
    position: absolute;
    padding: 1rem;
    padding-top: 2.5rem;
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

const WorkbookName = styled.span`
  position: absolute;
  top: 1rem;
  left: 1rem;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;

  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-size: ${theme.fontSize.default};
    font-weight: ${theme.fontWeight.bold};
  `}
`;

const Question = styled.div`
  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `}
`;

const Answer = styled.div`
  transform: rotateX(180deg);

  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `}
`;

const Text = styled.div`
  overflow-y: auto;
  height: 9rem;
  line-height: 9rem;

  ${({ theme }) => css`
    ::-webkit-scrollbar {
      width: 2px;
    }
    ::-webkit-scrollbar-thumb {
      background-color: ${theme.color.gray_4};
    }
  `}

  & > span {
    display: inline-block;
    vertical-align: middle;
    line-height: 1.4rem;
  }
`;

export default Quiz;
