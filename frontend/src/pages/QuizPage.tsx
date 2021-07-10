import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { QnACard } from '../components';
import { Flex } from '../styles';

const QuizPage = () => (
  <Container>
    <Tooltip>
      <TooltipDescription>
        카드를 클릭해 질문과 정답을 확인할 수 있어요.
      </TooltipDescription>
    </Tooltip>
    <QnACard
      question="리버스 프록시란 무엇인가요?"
      answer="리버스 프록시란 어쩌고 저쩌고 입니다."
      categoryName="Network"
    />
    <PageNation>
      <button>
        <i className="fas fa-arrow-left"></i>
      </button>
      <Page>1 / 10</Page>
      <button>
        <i className="fas fa-arrow-right"></i>
      </button>
    </PageNation>
  </Container>
);

const Container = styled.div`
  ${Flex({ justify: 'center', items: 'center', direction: 'column' })};
  width: 100%;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      height: ${theme.pageSize.height};
    `}
`;

const Tooltip = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 100%;
  height: 2.5rem;
  margin-bottom: 0.5rem;
  ${({ theme }) => css`
    background-color: ${theme.color.indigo};
    border-radius: ${theme.borderRadius.square};
  `}
`;

const TooltipDescription = styled.span`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
    color: ${theme.color.white};
  `}
`;

const PageNation = styled.div`
  ${Flex({ items: 'center' })};
  margin-top: 5rem;

  & > button {
    background-color: transparent;
    ${({ theme }) =>
      css`
        font-size: ${theme.fontSize.medium};
      `}
  }

  svg {
    height: inherit;
  }
`;

// TODO: margin-bottom 안쓰고 맞추기
const Page = styled.span`
  margin: 0 1.5rem;
  margin-bottom: 3px;
`;

export default QuizPage;
