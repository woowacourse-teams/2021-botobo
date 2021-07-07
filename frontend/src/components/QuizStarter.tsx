import styled from '@emotion/styled';
import React from 'react';

import quizStarterImage from '../assets/design-thinking.png';
import { Flex } from '../styles';

const QuizStarter = () => (
  <Container>
    <Content>
      <span>
        이제까지 정리한 <br />
        지식을 검증해보고 싶다면?
      </span>
      <Button>퀴즈 풀러 가기</Button>
    </Content>
    <ImageWrapper>
      <Image src={quizStarterImage} />
    </ImageWrapper>
  </Container>
);

const Container = styled.div`
  ${Flex()}
  background-color: ${({ theme }) => theme.color.white};
  height: 10rem;
  padding: 0 0.75rem;
  border-radius: ${({ theme }) => theme.borderRadius.square_1};
  box-shadow: ${({ theme }) => theme.boxShadow.card};
`;

const Content = styled.div`
  ${Flex({ direction: 'column', justify: 'center' })}
  width: 70%;
`;

const Button = styled.button`
  background-color: ${({ theme }) => theme.color.pink};
  margin-top: 1rem;
  width: 6.25rem;
`;

const ImageWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })}
  width: 30%;
`;

const Image = styled.img`
  width: 6.25rem;
`;

export default QuizStarter;
