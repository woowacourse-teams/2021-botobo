import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';

const PublicCardsLoadable = () => (
  <>
    <HeaderSkeleton />
    <Container>
      <Heading />
      <Heading />
      <CardSkeletonList count={6} />
      <BottomContent>
        <CheckboxWrapper></CheckboxWrapper>
        <Button size="full" shape="rectangle" backgroundColor={'gray_4'}>
          {' '}
        </Button>
      </BottomContent>
    </Container>
  </>
);

const Container = styled.div`
  margin-bottom: 3rem;

  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1.5rem;
    `}
`;

const Heading = styled.div`
  height: 1.5rem;
  width: 50%;
  margin-bottom: 0.5rem;

  ${loadContent}
`;

const BottomContent = styled.div`
  ${Flex()};
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
`;

const CheckboxWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  width: 20%;
  min-width: 6rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `};
`;

export default PublicCardsLoadable;
