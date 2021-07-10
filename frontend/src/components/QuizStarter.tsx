import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import quizStarterImage from '../assets/design-thinking.png';
import { ROUTE } from '../constants';
import { Flex } from '../styles';
import Button from './Button';

const QuizStarter = () => {
  const history = useHistory();

  return (
    <Container>
      <Content>
        <span>
          이제까지 정리한 <br />
          지식을 검증해보고 싶다면?
        </span>
        <Button
          backgroundColor="green"
          onClick={() => history.push(ROUTE.QUIZ_SETTING)}
        >
          퀴즈 풀러 가기
        </Button>
      </Content>
      <ImageWrapper>
        <Image src={quizStarterImage} />
      </ImageWrapper>
    </Container>
  );
};

const Container = styled.div`
  ${Flex()}
  background-color: ${({ theme }) => theme.color.white};
  height: 10rem;
  padding: 0 0.75rem;
  border-radius: ${({ theme }) => theme.borderRadius.square};
  box-shadow: ${({ theme }) => theme.boxShadow.card};
`;

const Content = styled.div`
  ${Flex({ direction: 'column', justify: 'center' })}
  width: 70%;

  & > span {
    margin-bottom: 1rem;
  }
`;

const ImageWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })}
  width: 30%;
`;

const Image = styled.img`
  width: 6.25rem;
`;

export default QuizStarter;
