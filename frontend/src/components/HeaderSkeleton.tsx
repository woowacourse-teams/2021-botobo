import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import MenuIcon from '../assets/menu.svg';
import { CLOUD_FRONT_DOMAIN } from '../constants';
import { Flex } from '../styles';

const logoSrc = `${CLOUD_FRONT_DOMAIN}/logo.png`;

const HeaderSkeleton = () => (
  <Container>
    <InnerContent>
      <h1>
        <Logo>
          <span>보고 또 보고</span>
        </Logo>
      </h1>
      <StyleMenuIcon width="1.3rem" height="1.3rem" />
    </InnerContent>
  </Container>
);

const Container = styled.div`
  height: 3.75rem;
  padding: 0 0.75rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
  `};
`;

const InnerContent = styled.div`
  ${Flex({ items: 'center', justify: 'space-between' })};
  height: 100%;
  margin: 0 auto;

  ${({ theme }) => css`
    max-width: ${theme.responsive.maxWidth};
  `}
`;

const Logo = styled.div`
  display: inline-block;
  position: relative;
  height: 1.5rem;

  &::before {
    content: '';
    display: inline-block;
    vertical-align: middle;
    height: 2rem;
    width: 8rem;
    background-repeat: no-repeat;
    background-size: contain;
    background-image: url(${logoSrc});
  }

  & > span {
    position: absolute;
    clip: rect(0 0 0 0);
    width: 1px;
    height: 1px;
    margin: -1px;
    overflow: hidden;
  }
`;

const StyleMenuIcon = styled(MenuIcon)`
  ${({ theme }) => css`
    fill: ${theme.color.gray_5};
  `};
`;

export default HeaderSkeleton;
