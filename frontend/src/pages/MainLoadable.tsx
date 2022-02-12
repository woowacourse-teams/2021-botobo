import styled from '@emotion/styled';
import React from 'react';

import {
  Button,
  CardSkeleton,
  CardSkeletonList,
  HeaderSkeleton,
} from '../components';
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
        <Title />
        <RankingButtonWrapper>
          {[...Array(3)].map((_, index) => (
            <Button
              key={index}
              shape="round"
              disabled={true}
              backgroundColor={'gray_3'}
            >
              {''}
            </Button>
          ))}
        </RankingButtonWrapper>
        <Title />
        <CardSkeletonList hasTag={true} hasAuthor={true} count={3} />
      </section>
      <section>
        <WorkbookHeader>
          <Title />
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
  height: 10rem;
  margin-bottom: 2rem;
`;

const WorkbookHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-top: 4rem;
`;

const Title = styled.div`
  height: 1.5rem;
  width: 20%;

  ${loadContent}
`;

const RankingButtonWrapper = styled.div`
  margin-top: 1rem;
  margin-bottom: 1.5rem;

  & > button {
    width: 5rem;
    height: 2rem;
    cursor: unset;
  }

  & > button:not(:last-of-type) {
    margin-right: 1rem;
  }
`;

export default MainLoadable;
