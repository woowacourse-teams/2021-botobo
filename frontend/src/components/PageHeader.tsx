import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';

interface Props {
  title: string;
  rightContent?: React.ReactElement;
}

const PageHeader = ({ title, rightContent }: Props) => (
  <StyledHeader>
    <Title>{title}</Title>
    {rightContent}
  </StyledHeader>
);

const StyledHeader = styled.header`
  ${Flex({ items: 'center', justify: 'space-between' })};
  position: sticky;
  top: 0;
  height: 3rem;
  padding: 0 0.75rem;
  z-index: 1;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    box-shadow: ${theme.boxShadow.header};
  `};
`;

const Title = styled.h2`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

export default PageHeader;
