import styled from '@emotion/styled';
import React from 'react';

import CardSkeleton from './CardSkeleton';

interface Props {
  count: number;
  className?: string;
}

const CardSkeletonList = ({ count, className }: Props) => (
  <StyledUl className={className}>
    {[...Array(count)].map((_, index) => (
      <CardSkeleton key={index} />
    ))}
  </StyledUl>
);

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 1rem;
  margin: 1rem 0;
`;

export default CardSkeletonList;
