import styled from '@emotion/styled';
import React from 'react';

import { CardSkeleton, CardSkeletonList, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const MainLoadable = () => (
  <>
    <HeaderSkeleton />
    <PageTemplate isScroll={true}>
      <TopContent>
        <Greeting />
        <Guide />
      </TopContent>
      <Banner />
      <QuizStarterSkeleton />
      <section>
        <WorkbookHeader>
          <WorkbookTitle />
        </WorkbookHeader>
        <CardSkeletonList hasTag={true} hasFooter={true} count={8} />
      </section>
    </PageTemplate>
  </>
);

const TopContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-bottom: 1rem;
`;

const Greeting = styled.div`
  height: 1.5rem;
  width: 40%;

  ${loadContent}
`;

const Guide = styled.div`
  height: 2.625rem;
  width: 11.5rem;

  ${loadContent}
`;

const Banner = styled.div`
  height: 3rem;
  margin-bottom: 1rem;

  ${loadContent}
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
