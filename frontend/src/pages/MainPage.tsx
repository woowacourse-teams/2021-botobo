import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, QuizStarter, WorkbookList } from '../components';
import { useMain, useRouter } from '../hooks';
import { Flex } from '../styles';

const MainPage = () => {
  console.log('테스트');
  const { workbooks, userInfo } = useMain();
  const { routeWorkbookAdd, routeCards } = useRouter();

  return (
    <Container>
      {userInfo && <Greeting>안녕하세요, {userInfo.userName} 님!</Greeting>}
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
