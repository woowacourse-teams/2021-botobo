import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';

const PublicSearchResultLoadable = () => (
  <>
    <HeaderSkeleton />
    <Container>
      <Title />
      <Filter>
        {[...Array(4)].map((_, index) => (
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
      <CardSkeletonList count={6} />
    </Container>
  </>
);

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1rem;
    `}
`;

const Title = styled.div`
  width: 40%;
  height: 1.5rem;
  margin-top: 0.5rem;
  margin-bottom: 1rem;

  ${loadContent};
`;

const Filter = styled.div`
  ${Flex()};
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1.5rem;

  & > button {
    width: 4rem;
    height: 2rem;
  }
`;

export default PublicSearchResultLoadable;
