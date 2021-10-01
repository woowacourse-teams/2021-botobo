import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const PublicSearchResultLoadable = () => (
  <>
    <HeaderSkeleton />
    <StyledPageTemplate isScroll={true}>
      <Title />
      <Filter>
        {[...Array(6)].map((_, index) => (
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
      <CardSkeletonList count={12} hasAuthor={true} hasTag={true} />
    </StyledPageTemplate>
  </>
);

const StyledPageTemplate = styled(PageTemplate)`
  padding-top: 2rem;
`;

const Title = styled.div`
  width: 40%;
  height: 1.5rem;
  margin: 0 auto;
  margin-bottom: 1rem;

  ${loadContent};
`;

const Filter = styled.div`
  ${Flex()};
  gap: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 2rem;
  overflow-x: hidden;

  & > button {
    width: 4rem;
    height: 2rem;
    flex-shrink: 0;
  }
`;

export default PublicSearchResultLoadable;
