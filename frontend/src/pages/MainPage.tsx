import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import ForwardIcon from '../assets/chevron-right-solid.svg';
import { Button, QuizStarter, WorkbookList } from '../components';
import { useMain, useRouter } from '../hooks';
import { Flex } from '../styles';

const MainPage = () => {
  const { workbooks, userInfo } = useMain();
  const { routeWorkbookAdd, routeCards, routePublicWorkbook } = useRouter();

  return (
    <Container>
      {userInfo && <Greeting>안녕하세요, {userInfo.userName} 님!</Greeting>}
      <Banner onClick={routePublicWorkbook}>
        <BannerText>다양한 문제집 보러 가기</BannerText>
        <StyledButton backgroundColor="white" color="gray_8" shape="circle">
          <ForwardIcon width="1rem" height="1rem" />
        </StyledButton>
      </Banner>
      <QuizStarter />
      <section>
        <WorkbookHeader>
          <WorkbookTitle>학습 중</WorkbookTitle>
          <Button shape="square" onClick={routeWorkbookAdd}>
            문제집 추가
          </Button>
        </WorkbookHeader>
        <WorkbookList
          workbooks={workbooks}
          onClickWorkbook={(workbookId) => routeCards(workbookId)}
          editable={true}
        />
      </section>
    </Container>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const Greeting = styled.div`
  margin-bottom: 1rem;
`;

const Banner = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  cursor: pointer;
  height: 4rem;
  margin-bottom: 1rem;
  padding: 0 1rem;

  ${({ theme }) => css`
    color: ${theme.color.white};
    background-color: ${theme.color.blue};
    border-radius: ${theme.borderRadius.square};
    font-weight: ${theme.fontWeight.semiBold};
  `}
`;

const BannerText = styled.span`
  word-spacing: -2px;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

const StyledButton = styled(Button)`
  width: 2rem;
  height: 2rem;

  & > svg {
    margin-left: 0.07rem;
  }
`;

const WorkbookHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-top: 3rem;
`;

const WorkbookTitle = styled.h2`
  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.semiLarge};
    `};
`;

export default MainPage;
