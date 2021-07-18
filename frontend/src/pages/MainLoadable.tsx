import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeleton, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';

const MainLoadable = () => (
  <>
    <HeaderSkeleton />
    <Container>
      <CardSkeleton />
      <section>
        <CategoryHeader>
          <CategoryTitle></CategoryTitle>
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
        <StyledUl>
          {[...Array(5)].map((_, i) => (
            <CardSkeleton key={i} />
          ))}
        </StyledUl>
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

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
  margin: 1rem 0;
`;

export default MainLoadable;
