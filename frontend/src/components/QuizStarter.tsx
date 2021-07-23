import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { getGuestQuizzesAsync } from '../api';
import QuizStarterIcon from '../assets/design-thinking.svg';
import { ROUTE } from '../constants';
import { useSnackbar } from '../hooks';
import { loginState, quizState } from '../recoil';
import { Flex } from '../styles';
import Button from './Button';

const QuizStarter = () => {
  const history = useHistory();
  const isLogin = useRecoilValue(loginState);
  const setQuizState = useSetRecoilState(quizState);
  const showSnackbar = useSnackbar();

  const startGuestQuiz = async () => {
    try {
      const quizzes = await getGuestQuizzesAsync();

      setQuizState(quizzes);
      history.push(ROUTE.QUIZ.PATH);
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
        <Button
          onClick={
            isLogin
              ? () => history.push(ROUTE.QUIZ_SETTING.PATH)
              : () => startGuestQuiz()
          }
        >
          퀴즈 풀러 가기
        </Button>
      </Content>
      <ImageWrapper>
        <QuizStarterIcon width="5.5rem" height="5.5rem" />
      </ImageWrapper>
    </Container>
  );
};

const Container = styled.div`
  ${Flex()};
  height: 10rem;
  padding: 0 1.25rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.card};
  `}
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
