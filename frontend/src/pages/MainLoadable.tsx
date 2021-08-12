import styled from '@emotion/styled';
import React from 'react';

import { CardSkeleton, CardSkeletonList, HeaderSkeleton } from '../components';
import { DEVICE } from '../constants';
import { Flex, loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const MainLoadable = () => (
  <>
    <HeaderSkeleton />
    <PageTemplate isScroll={true}>
      <Greeting />
      <Banner />
      <QuizStarterSkeleton />
      <section>
        <WorkbookHeader>
          <WorkbookTitle />
        </WorkbookHeader>
        <StyledCardSkeletonList count={8} />
      </section>
    </PageTemplate>
  </>
);

const Greeting = styled.div`
  height: 1.5rem;
  width: 60%;
  margin-bottom: 1rem;

  ${loadContent}
`;

const Banner = styled.div`
  height: 2.5rem;
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

const StyledCardSkeletonList = styled(CardSkeletonList)`
  @media ${DEVICE.TABLET} {
    grid-template-columns: repeat(2, 1fr);
  }
`;

export default MainLoadable;
