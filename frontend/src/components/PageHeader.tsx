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
    <InnerContent>
      <Title>{title}</Title>
      {rightContent}
    </InnerContent>
  </StyledHeader>
);

const StyledHeader = styled.header`
  position: sticky;
  top: 0;
  height: 3rem;
  padding: 0 0.75rem;
  z-index: 1;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-bottom: 1px solid ${theme.color.gray_3};
  `};
`;

const InnerContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'center' })};
  height: 100%;
  margin: 0 auto;

  ${({ theme }) => css`
    max-width: ${theme.responsive.maxWidth};
  `}
`;

const Title = styled.h2`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

export default PageHeader;
