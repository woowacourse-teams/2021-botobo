import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import {
  Button,
  CardSkeleton,
  CardSkeletonList,
  HeaderSkeleton,
} from '../components';
import { Flex, loadContent } from '../styles';

const MainLoadable = () => (
  <>
    <HeaderSkeleton />
    <Container>
      <Greeting />
      <QuizStarterSkeleton />
      <section>
        <WorkbookHeader>
          <WorkbookTitle />
        </WorkbookHeader>
        <CardSkeletonList count={6} />
      </section>
    </Container>
  </>
);

const Greeting = styled.div`
  height: 1.5rem;
  width: 60%;
  margin-bottom: 1rem;

  ${loadContent}
`;

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const QuizStarterSkeleton = styled(CardSkeleton)`
  height: 9.5rem;
`;

const WorkbookHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-top: 3rem;
`;

const WorkbookTitle = styled.div`
  height: 1.5rem;
  width: 50%;

  ${loadContent}
`;

export default MainLoadable;
