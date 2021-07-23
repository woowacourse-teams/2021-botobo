import styled from '@emotion/styled';
import React from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { getGuestQuizzesAsync } from '../api';
import QuizStarterIcon from '../assets/design-thinking.svg';
import { useRouter, useSnackbar } from '../hooks';
import { quizState, userState } from '../recoil';
import { Flex } from '../styles';
import Button from './Button';
import CardTemplate from './CardTemplate';

const QuizStarter = () => {
  const userInfo = useRecoilValue(userState);
  const setQuizState = useSetRecoilState(quizState);
  const showSnackbar = useSnackbar();
  const { routeQuizSetting, routeQuiz } = useRouter();

  const startGuestQuiz = async () => {
    try {
      const quizzes = await getGuestQuizzesAsync();

      setQuizState(quizzes);
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
        <Button onClick={userInfo ? routeQuizSetting : startGuestQuiz}>
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
