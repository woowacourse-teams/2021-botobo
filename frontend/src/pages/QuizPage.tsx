import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useRef, useState } from 'react';
import { useRecoilState } from 'recoil';

import LeftArrowIcon from '../assets/arrow-left.svg';
import RightArrowIcon from '../assets/arrow-right.svg';
import { MainHeader, Quiz, Timer } from '../components';
import { useInterval, useQuiz } from '../hooks';
import { quizTimeState } from '../recoil';
import { Flex } from '../styles';

interface QuizListProps {
  quizCount: number;
  currentIndex: number;
}

interface QuizItemProps {
  quizIndex: number;
}

interface QuizIndexButtonStyleProps {
  isSelected: boolean;
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
    setCurrentQuizIndex,
    showNextQuiz,
    showPrevQuiz,
  } = useQuiz();

  const quizListRef = useRef<HTMLUListElement>(null);
  const [xPosition, setXPosition] = useState('0');

  const [quizTime, setQuizTime] = useRecoilState(quizTimeState);

  useInterval(
    () => setQuizTime((prevValue) => prevValue + 1),
    1000,
    hasQuizTime
  );

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

  return (
    <>
      <MainHeader />
      <Container>
        {hasQuizTime && (
          <TimerWrapper>
            <Timer time={quizTime} />
          </TimerWrapper>
        )}
        <QuizWrapper>
          {currentQuizIndex === 0 && (
            <Tooltip>카드를 클릭해 질문과 정답을 확인할 수 있어요.</Tooltip>
          )}
          {quizzes && (
            <QuizList
              ref={quizListRef}
              quizCount={quizzes.length}
              currentIndex={currentQuizIndex}
              style={{ transform: `translateX(calc(${xPosition}))` }}
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
        {isMobile() ? (
          <MobilePageNation>
            <TouchBar
              onTouchStart={(event) => {
                cardSlideInfo.xPosition = event.touches[0].clientX;
              }}
              onTouchMove={(event) => {
                if (!quizListRef?.current) return;

                const targetStyle = quizListRef.current.style;
                const changedXPosition =
                  event.touches[0].clientX - cardSlideInfo.xPosition;

                if (changedXPosition > cardSlideInfo.width) return;
                if (changedXPosition < -cardSlideInfo.width) return;

                window.requestAnimationFrame(() => {
                  targetStyle.transform = `translateX(calc(${xPosition} + ${changedXPosition}px))`;
                });
              }}
              onTouchEnd={(event) => {
                if (!quizListRef?.current) return;

                const targetStyle = quizListRef?.current.style;
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
              {[...Array(quizzes.length)].map((_, index) => (
                <QuizIndexButton
                  type="button"
                  onClick={() => setCurrentQuizIndex(index)}
                  key={index}
                  isSelected={currentQuizIndex === index}
                >
                  {index + 1}
                </QuizIndexButton>
              ))}
            </TouchBar>
            {currentQuizIndex === 0 && (
              <SlideTooltip>상단 바를 좌우로 밀어보세요!</SlideTooltip>
            )}
            {currentQuizIndex !== 0 && (
              <PrevButton type="button" onClick={showPrevQuiz}>
                이전
              </PrevButton>
            )}
            <NextButton type="button" onClick={showNextQuiz}>
              {currentQuizIndex === quizzes.length - 1 ? '결과확인' : '다음'}
            </NextButton>
          </MobilePageNation>
        ) : (
          <DesktopPageNation>
            <ArrowButton type="button" onClick={showPrevQuiz}>
              <LeftArrowIcon width="1rem" height="1rem" />
            </ArrowButton>
            <Page>
              {currentQuizIndex + 1} / {quizzes.length}
            </Page>
            <ArrowButton type="button" onClick={showNextQuiz}>
              <RightArrowIcon width="1rem" height="1rem" />
            </ArrowButton>
          </DesktopPageNation>
        )}
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

const TimerWrapper = styled.div`
  position: absolute;
  top: 15%;
`;

const QuizWrapper = styled.div`
  position: relative;
  width: 100%;
`;

const Tooltip = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  position: absolute;
  width: 100%;
  top: -1.5rem;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.square};
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

const DesktopPageNation = styled.div`
  ${Flex({ items: 'center' })};
  position: relative;
  margin-top: 5rem;
  height: 1.25rem;
`;

const MobilePageNation = styled.div`
  position: relative;
  top: 2.5rem;
  padding: 0 0.5rem;
  width: 100%;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
  `}
`;

const TouchBar = styled.div`
  display: grid;
  grid-row-gap: 0.5rem;
  width: 100%;
  padding: 0.5rem 0;
  grid-template-columns: repeat(10, 1fr);
  justify-items: center;
  justify-content: space-between;
`;

const SlideTooltip = styled.div`
  position: absolute;
  width: max-content;
  transform: translateX(-50%);
  left: 50%;
  bottom: -1.25rem;

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

const QuizIndexButton = styled.button<QuizIndexButtonStyleProps>`
  ${({ theme, isSelected }) => css`
    color: ${isSelected ? theme.color.gray_9 : theme.color.gray_4};
  `}
`;

const MobileButtonStyle = styled.button`
  position: absolute;
  bottom: -2rem;
`;

const PrevButton = styled(MobileButtonStyle)`
  left: 0;
`;

const NextButton = styled(MobileButtonStyle)`
  right: 0;
`;

const Page = styled.span`
  margin: 0 1.5rem;
  height: 100%;
`;

export default QuizPage;
