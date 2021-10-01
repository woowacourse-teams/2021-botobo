import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const CardsLoadable = () => (
  <>
    <HeaderSkeleton />
    <PageTemplate isScroll={true}>
      <WorkbookName />
      <Description />
      <TagList />
      <Filter>
        {[...Array(2)].map((_, index) => (
          <Button
            key={index}
            shape="round"
            backgroundColor={'gray_5'}
            inversion={true}
            disabled={true}
          >
            {''}
          </Button>
        ))}
      </Filter>
      <Button size="full" backgroundColor="blue" disabled={true}>
        {''}
      </Button>
      <CardSkeletonList
        count={6}
        hasHeader={true}
        hasFooter={true}
        type="QnA"
      />
    </PageTemplate>
  </>
);

const WorkbookName = styled.div`
  width: 30%;
  height: 1.75rem;
  margin-bottom: 1rem;

  ${loadContent};
`;

const Description = styled.div`
  width: 50%;
  height: 1.25rem;

  ${loadContent};
`;

const TagList = styled.div`
  height: 1rem;
  width: 70%;
  margin-top: 0.5rem;

  ${loadContent}
`;

const Filter = styled.div`
  height: 3rem;
  margin-top: 1rem;
  margin-bottom: 1rem;

  & > button {
    margin-right: 0.8rem;
    width: 3.5rem;
    height: 2rem;
  }
`;

export default CardsLoadable;
