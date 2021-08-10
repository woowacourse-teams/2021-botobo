import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import MenuIcon from '../assets/menu.svg';
import { Flex } from '../styles';

const HeaderSkeleton = () => (
  <Container>
    <StyleMenuIcon width="1.3rem" height="1.3rem" />
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

const StyleMenuIcon = styled(MenuIcon)`
  ${({ theme }) => css`
    fill: ${theme.color.gray_5};
  `};
`;

export default HeaderSkeleton;
