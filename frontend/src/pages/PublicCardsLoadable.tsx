import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CardSkeletonList, HeaderSkeleton } from '../components';
import { Flex, loadContent } from '../styles';
import PageTemplate from './PageTemplate';

const PublicCardsLoadable = () => (
  <>
    <HeaderSkeleton />
    <StyledPageTemplate isScroll={true}>
      <Heading />
      <Heading />
      <CardSkeletonList count={6} />
      <BottomContent>
        <Button size="full" shape="rectangle" backgroundColor={'gray_4'}>
          {' '}
        </Button>
      </BottomContent>
    </StyledPageTemplate>
  </>
);

const StyledPageTemplate = styled(PageTemplate)`
  margin-bottom: 3rem;
  padding-top: 1.5rem;
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
  width: 100%;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);

  ${({ theme }) => css`
    max-width: ${theme.responsive.maxWidth};
  `}
`;

export default PublicCardsLoadable;
