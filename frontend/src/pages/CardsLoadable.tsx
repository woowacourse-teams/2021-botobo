import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { loadContent } from '../styles';

const CardsLoadable = () => (
  <>
    <HeaderSkeleton />
    <Container>
      <CategoryName />
      <Description />
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
      <CardSkeletonList count={6} />
    </Container>
  </>
);

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const CategoryName = styled.div`
  width: 40%;
  height: 2rem;
  margin-bottom: 1rem;

  ${loadContent};
`;

const Description = styled.div`
  width: 60%;
  height: 1.25rem;

  ${loadContent};
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
