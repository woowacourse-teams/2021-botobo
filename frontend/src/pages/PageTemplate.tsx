import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

interface Props {
  isScroll: boolean;
  className?: string;
  children: React.ReactNode;
}

const PageTemplate = ({ isScroll, className, children }: Props) => (
  <Container isScroll={isScroll} className={className}>
    {children}
  </Container>
);

const Container = styled.div<Pick<Props, 'isScroll'>>`
  max-width: 768px;
  margin: 0 auto;
  padding: 3rem 1.25rem;

  ${({ isScroll }) => css`
    ${!isScroll &&
    css`
      height: calc(100vh - 3.75rem);
    `}
  `};
`;

export default PageTemplate;
