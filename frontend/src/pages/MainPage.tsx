import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useHistory } from 'react-router-dom';

import PlusIcon from '../assets/plus.svg';
import { Button, QuizStarter, WorkbookList } from '../components';
import { ROUTE } from '../constants';
import { useMain } from '../hooks';
import { Flex } from '../styles';

const MainPage = () => {
  const { workbooks } = useMain();
  const history = useHistory();

  return (
    <Container>
      <QuizStarter />
      <section>
        <WorkbookHeader>
          <WorkbookTitle>학습 중</WorkbookTitle>
          <Button
            shape="circle"
            backgroundColor="white"
            color="green"
            hasShadow={true}
            onClick={() => history.push(ROUTE.WORKBOOK_ADD.PATH)}
          >
            <StyledPlusIcon />
          </Button>
        </WorkbookHeader>
        <WorkbookList
          workbooks={workbooks}
          onClickWorkbook={(workbookId) =>
            history.push(`${ROUTE.CARDS.PATH}?id=${workbookId}`)
          }
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

// TODO: 타입 체크하기
const StyledPlusIcon = styled(PlusIcon)`
  width: 1rem;
  height: 1rem;
  vertical-align: middle;

  ${({ theme }) =>
    css`
      fill: ${theme.color.green};
    `}
`;

export default MainPage;
