import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';

const HeaderSkeleton = () => (
  <Container>
    <RightContent />
  </Container>
);

const Container = styled.div`
  ${Flex({ items: 'center', justify: 'flex-end' })}
  height: 3.75rem;
  padding: 0 0.75rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
  `};
`;

const RightContent = styled.div`
  width: 2rem;
  height: 2rem;

  ${({ theme }) => css`
    border-radius: ${theme.borderRadius.circle};
    background-color: ${theme.color.gray_1};
  `};
`;

export default HeaderSkeleton;
