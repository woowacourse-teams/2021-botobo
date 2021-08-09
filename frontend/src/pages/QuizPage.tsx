import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';
import { useRecoilState } from 'recoil';

import LeftArrowIcon from '../assets/arrow-left.svg';
import RightArrowIcon from '../assets/arrow-right.svg';
import { Clock, MainHeader, Quiz } from '../components';
import { useQuiz } from '../hooks';
import { quizTimeState } from '../recoil';
import { Flex } from '../styles';

interface TooltipProps {
  isTooltipVisible: boolean;
}
interface QuizListProps {
  quizCount: number;
  currentIndex: number;
}

interface QuizItemProps {
  quizIndex: number;
}

const cardSlideInfo = {
  xPosition: 0,
  width: 0,
  pointOfChange: 0,
};

const isMobile = () => {
  const PC_DEVICE = ['win16', 'win32', 'win64', 'mac', 'macintel'];
  const platform = navigator.platform;

  if (!platform) return false;

  return !PC_DEVICE.includes(platform.toLocaleLowerCase());
};

const QuizPage = () => {
  const {
    quizzes,
    hasQuizTime,
    prevQuizId,
    currentQuizIndex,
    showNextQuiz,
    showPrevQuiz,
  } = useQuiz();

  const quizListRef = useRef<HTMLUListElement>(null);
  const [xPosition, setXPosition] = useState('0');

  const [quizTime, setQuizTime] = useRecoilState(quizTimeState);

  useEffect(() => {
    setXPosition(
      `-${cardSlideInfo.width * currentQuizIndex}px - ${
        1.25 * currentQuizIndex
      }rem`
    );
  }, [currentQuizIndex]);

  useEffect(() => {
    if (!quizListRef?.current) return;

    const quizListWidth = quizListRef.current.clientWidth;

    cardSlideInfo.width = quizListWidth;
    cardSlideInfo.pointOfChange = quizListWidth / 5;
  }, []);

  useEffect(() => {
    if (!hasQuizTime) return;

    const intervalId = window.setInterval(
      () => setQuizTime((prevValue) => prevValue + 1),
      1000
    );

    return () => window.clearInterval(intervalId);
  }, []);

  return (
    <>
      <MainHeader />
      <Container>
        {hasQuizTime && (
          <ClockWrapper>
            <Clock time={quizTime} />
          </ClockWrapper>
        )}
        <QuizWrapper>
          <Tooltip isTooltipVisible={currentQuizIndex === 0}>
            카드를 클릭해 질문과 정답을 확인할 수 있어요.
          </Tooltip>
          {quizzes && (
            <QuizList
              ref={quizListRef}
              quizCount={quizzes.length}
              currentIndex={currentQuizIndex}
              style={{ transform: `translateX(calc(${xPosition}))` }}
              onTouchStart={(event) => {
                cardSlideInfo.xPosition = event.touches[0].clientX;
              }}
              onTouchMove={(event) => {
                const targetStyle = event.currentTarget.style;
                const changedXPosition =
                  event.touches[0].clientX - cardSlideInfo.xPosition;

                if (changedXPosition > cardSlideInfo.width) return;
                if (changedXPosition < -cardSlideInfo.width) return;

                window.requestAnimationFrame(() => {
                  targetStyle.transform = `translateX(calc(${xPosition} + ${changedXPosition}px))`;
                });
              }}
              onTouchEnd={(event) => {
                const targetStyle = event.currentTarget.style;
                const changedXPosition =
                  event.changedTouches[0].clientX - cardSlideInfo.xPosition;

                if (changedXPosition === 0) return;

                if (changedXPosition < -cardSlideInfo.pointOfChange) {
                  window.requestAnimationFrame(showNextQuiz);

                  return;
                }

                if (
                  changedXPosition > cardSlideInfo.pointOfChange &&
                  currentQuizIndex !== 0
                ) {
                  window.requestAnimationFrame(showPrevQuiz);

                  return;
                }

                window.requestAnimationFrame(() => {
                  targetStyle.transform = `translateX(calc(${xPosition}))`;
                });
              }}
            >
              {quizzes.map(({ id, question, answer, workbookName }, index) => (
                <QuizItem key={id} quizIndex={index}>
                  <Quiz
                    question={question}
                    answer={answer}
                    workbookName={workbookName}
                    isChanged={id === prevQuizId}
                  />
                </QuizItem>
              ))}
            </QuizList>
          )}
        </QuizWrapper>
        <PageNation>
          {currentQuizIndex === 0 && isMobile() && (
            <SlideTooltip>카드를 좌우로 넘겨보세요.</SlideTooltip>
          )}
          <ArrowButton onClick={showPrevQuiz}>
            <LeftArrowIcon width="1rem" height="1rem" />
          </ArrowButton>
          <Page>
            {currentQuizIndex + 1} / {quizzes.length}
          </Page>
          <ArrowButton onClick={showNextQuiz}>
            <RightArrowIcon width="1rem" height="1rem" />
          </ArrowButton>
        </PageNation>
      </Container>
    </>
  );
};

const Container = styled.div`
  ${Flex({ justify: 'center', items: 'center', direction: 'column' })};
  width: 100%;
  overflow: hidden;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      height: ${theme.pageSize.height};
    `}
`;

const ClockWrapper = styled.div`
  position: absolute;
  top: 15%;
`;

const QuizWrapper = styled.div`
  position: relative;
  width: 100%;
  margin-top: 3rem;
`;

const Tooltip = styled.div<TooltipProps>`
  ${Flex({ justify: 'center', items: 'center' })};
  position: absolute;
  width: 100%;
  top: -1.5rem;
  transition: opacity 0.1s ease;

  ${({ theme, isTooltipVisible }) => css`
    border-radius: ${theme.borderRadius.square};
    opacity: ${isTooltipVisible ? 1 : 0};
    font-size: ${theme.fontSize.small};
  `}
`;

const QuizList = styled.ul<QuizListProps>`
  ${Flex()};
  transition: transform 0.1s ease-out;
  column-gap: 1.25rem;
`;

const QuizItem = styled.li<QuizItemProps>`
  width: 100%;
  flex-shrink: 0;
`;

const PageNation = styled.div`
  ${Flex({ items: 'center' })};
  position: relative;
  margin-top: 5rem;
  height: 1.25rem;
`;

const SlideTooltip = styled.div`
  position: absolute;
  width: max-content;
  transform: translateX(-50%);
  left: 50%;
  top: -4.5rem;

  ${({ theme }) => css`
    color: ${theme.color.gray_7};
    font-size: ${theme.fontSize.small};
  `}
`;

const ArrowButton = styled.button`
  height: 100%;

  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.medium};
    `}
`;

const Page = styled.span`
  margin: 0 1.5rem;
  height: 100%;
`;

export default QuizPage;
