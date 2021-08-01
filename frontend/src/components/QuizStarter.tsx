import styled from '@emotion/styled';
import React from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { getGuestQuizzesAsync } from '../api';
import QuizStarterIcon from '../assets/design-thinking.svg';
import { QUIZ_MODE } from '../constants';
import { useRouter, useSnackbar } from '../hooks';
import { quizState, userState } from '../recoil';
import { quizModeState } from '../recoil/quizState';
import { Flex } from '../styles';
import { WorkbookResponse } from '../types';
import Button from './Button';
import CardTemplate from './CardTemplate';

interface Props {
  workbooks: WorkbookResponse[];
}

const QuizStarter = ({ workbooks }: Props) => {
  const userInfo = useRecoilValue(userState);
  const setQuizState = useSetRecoilState(quizState);
  const setQuizMode = useSetRecoilState(quizModeState);
  const showSnackbar = useSnackbar();
  const { routeQuizSetting, routeQuiz } = useRouter();

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
      showSnackbar({ message: '퀴즈 생성에 실패했습니다.', type: 'error' });
    }
  };

  return (
    <Container>
      <Content>
        <span>
          이제까지 정리한 <br />
          지식을 검증해보고 싶다면?
        </span>
        <Button onClick={userInfo ? startQuiz : startGuestQuiz}>
          퀴즈 풀러 가기
        </Button>
      </Content>
      <ImageWrapper>
        <QuizStarterIcon width="5.5rem" height="5.5rem" />
      </ImageWrapper>
    </Container>
  );
};

const Container = styled(CardTemplate)`
  ${Flex()};
  height: 10rem;
`;

const Content = styled.div`
  ${Flex({ direction: 'column', justify: 'center' })};
  width: 70%;

  & > span {
    margin-bottom: 1rem;
  }
`;

const ImageWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 30%;
`;

export default QuizStarter;
