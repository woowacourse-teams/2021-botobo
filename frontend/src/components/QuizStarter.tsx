import styled from '@emotion/styled';
import React from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { getGuestQuizzesAsync } from '../api';
import { CLOUD_FRONT_DOMAIN, QUIZ_MODE } from '../constants';
import { useErrorHandler, useRouter, useSnackbar } from '../hooks';
import { quizState, userState } from '../recoil';
import { quizModeState } from '../recoil/quizState';
import { Flex } from '../styles';
import { WorkbookResponse } from '../types';
import Button from './Button';
import CardTemplate from './CardTemplate';

const puzzleSrc = `${CLOUD_FRONT_DOMAIN}/puzzle-piece.png`;

interface Props {
  workbooks: WorkbookResponse[];
}

const QuizStarter = ({ workbooks }: Props) => {
  const userInfo = useRecoilValue(userState);
  const setQuizState = useSetRecoilState(quizState);
  const setQuizMode = useSetRecoilState(quizModeState);
  const showSnackbar = useSnackbar();
  const { routeQuizSetting, routeQuiz } = useRouter();
  const errorHandler = useErrorHandler();

  const startQuiz = () => {
    if (workbooks.find((workbook) => workbook.cardCount > 0)) {
      routeQuizSetting();

      return;
    }

    showSnackbar({
      message: '우선 문제집과 카드를 추가해주세요.',
    });
  };

  const startGuestQuiz = async () => {
    try {
      const quizzes = await getGuestQuizzesAsync();

      setQuizState(quizzes);
      setQuizMode(QUIZ_MODE.GUEST);
      routeQuiz();
    } catch (error) {
      errorHandler(error);
    }
  };

  return (
    <Container>
      <Inner>
        <Content>
          <span>
            이제까지 정리한 <br />
            지식을 검증해보고 싶다면?
          </span>
          <Button onClick={userInfo ? startQuiz : startGuestQuiz}>
            퀴즈 풀러 가기
          </Button>
        </Content>
        <ImageWrapper />
      </Inner>
    </Container>
  );
};

const Container = styled(CardTemplate)`
  height: 10rem;
`;

const Inner = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  max-width: 37.5rem;
  height: 100%;
  margin: 0 auto;
`;

const Content = styled.div`
  ${Flex({ direction: 'column', justify: 'center' })};

  & > span {
    margin-bottom: 1rem;
  }
`;

const ImageWrapper = styled.div`
  width: 5.5rem;
  height: 5.5rem;
  background-image: url(${puzzleSrc});
  background-repeat: no-repeat;
  background-size: contain;
`;

export default QuizStarter;
