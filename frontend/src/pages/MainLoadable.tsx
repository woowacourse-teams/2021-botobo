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
      <CardSkeleton />
      <section>
        <CategoryHeader>
          <CategoryTitle />
          <Button
            shape="circle"
            backgroundColor="white"
            color="green"
            hasShadow={true}
            disabled={true}
          >
            {''}
          </Button>
        </CategoryHeader>
        <CardSkeletonList count={6} />
      </section>
    </Container>
  </>
);

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const CategoryHeader = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  margin-top: 3rem;
`;

const CategoryTitle = styled.div`
  height: 1.5rem;
  width: 50%;

  ${loadContent}
`;

export default MainLoadable;
