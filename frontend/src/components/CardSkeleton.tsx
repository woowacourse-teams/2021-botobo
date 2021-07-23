import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex, loadContent } from '../styles';

interface Props {
  className?: string;
}

const CardSkeleton = ({ className }: Props) => (
  <Container className={className}>
    <Name />
    <Description />
  </Container>
);

const Container = styled.div`
  ${Flex({ direction: 'column' })};
  position: relative;
  padding: 1rem;
  height: 7rem;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.card};
  `}
`;

const Name = styled.span`
  width: 70%;
  height: 1.5rem;
  margin: 0.3rem 0;

  ${loadContent}
`;

const Description = styled.span`
  width: 30%;
  height: 1rem;
  margin-top: 0.3rem;

  ${loadContent}
`;

export default CardSkeleton;
