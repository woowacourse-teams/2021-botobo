import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { QUIZ_MODE } from '../constants';
import { quizModeState } from '../recoil';
import { Flex, scrollBarStyle } from '../styles';
import { QuizResponse } from '../types';

interface Props extends Omit<QuizResponse, 'id'> {
  isChanged: boolean;
}

interface CardStyleProps {
  isFlipped: boolean;
}

const Quiz = ({
  question,
  answer,
  workbookName,
  encounterCount,
  isChanged,
}: Props) => {
  const quizMode = useRecoilValue(quizModeState);
  const [isFlipped, setIsFlipped] = useState(false);

  useEffect(() => {
    setIsFlipped(false);
  }, [isChanged]);

  return (
    <Container onClick={() => setIsFlipped((prevValue) => !prevValue)}>
      <Card isFlipped={isFlipped}>
        <Question>
          <TopContent>
            <WorkbookName>{workbookName}</WorkbookName>
            {quizMode === QUIZ_MODE.DEFAULT && (
              <EncounterCount>풀어본 횟수: {encounterCount}</EncounterCount>
            )}
          </TopContent>
          <TextWrapper>
            <Text>
              <TextCenter>
                <span>Q.</span> {question}
              </TextCenter>
            </Text>
          </TextWrapper>
        </Question>
        <Answer>
          <TopContent>
            <WorkbookName>{workbookName}</WorkbookName>
          </TopContent>
          <TextWrapper>
            <Text>
              <TextCenter>
                <span>A.</span> {answer}
              </TextCenter>
            </Text>
          </TextWrapper>
        </Answer>
      </Card>
    </Container>
  );
};

const Container = styled.div`
  height: 35vh;
  width: 100%;
  perspective: 100rem;
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
    backface-visibility: hidden;
    overflow-y: auto;

    ${({ theme }) =>
      css`
        box-shadow: ${theme.boxShadow.card};
        border-radius: ${theme.borderRadius.square};
        background-color: ${theme.color.white};
      `}
  }
`;

const TopContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  position: absolute;
  width: 100%;
  top: 1rem;
  left: 0;
  padding: 0 1rem;
  backface-visibility: hidden;
`;

const WorkbookName = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.pink};
    font-weight: ${theme.fontWeight.bold};
  `}
`;

const EncounterCount = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `}
`;

const Question = styled.div`
  transform: rotateX(0deg);
`;

const Answer = styled.div`
  transform: rotateX(180deg);
`;

const TextWrapper = styled.div`
  overflow-y: auto;
  height: 100%;
  line-height: calc(35vh - 3.5rem);

  ${scrollBarStyle};
`;

const Text = styled.div`
  display: inline-block;
  vertical-align: middle;
  line-height: 1.5;
`;

const TextCenter = styled.div`
  ${Flex()};

  & > span {
    margin-right: 0.3rem;
  }
`;

export default Quiz;
