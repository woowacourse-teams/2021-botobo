import styled from '@emotion/styled';
import React from 'react';

import { Flex, loadContent } from '../styles';
import CardTemplate from './CardTemplate';

interface Props {
  className?: string;
}

const CardSkeleton = ({ className }: Props) => (
  <Container className={className}>
    <Name />
    <Description />
  </Container>
);

const Container = styled(CardTemplate)`
  ${Flex({ direction: 'column' })};
  position: relative;
  height: 7rem;
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
