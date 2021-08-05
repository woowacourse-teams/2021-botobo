import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, MainHeader, WorkbookList } from '../components';
import { ROUTE } from '../constants';
import { useQuizSetting } from '../hooks';

const QuizSettingPage = () => {
  const { workbooks, checkWorkbook, startQuiz } = useQuizSetting();

  return (
    <>
      <MainHeader />
      <Container>
        <Title>{ROUTE.QUIZ_SETTING.TITLE}</Title>
        <span>어떤 문제를 풀어볼까요?</span>
        <WorkbookWrapper>
          <WorkbookList workbooks={workbooks} onClickWorkbook={checkWorkbook} />
        </WorkbookWrapper>
        <Button onClick={startQuiz} size="full">
          시작!
        </Button>
      </Container>
    </>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const Title = styled.h2`
  margin-bottom: 1rem;
`;

const WorkbookWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 2.5rem;
`;

export default QuizSettingPage;
