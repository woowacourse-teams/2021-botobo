import styled from '@emotion/styled';
import React from 'react';

import { Flex, loadContent } from '../styles';
import CardTemplate from './CardTemplate';

interface Props {
  hasAuthor?: boolean;
  hasTag?: boolean;
  hasFooter?: boolean;
  className?: string;
}

const CardSkeleton = ({
  hasAuthor = false,
  hasTag = false,
  hasFooter = false,
  className,
}: Props) => (
  <Container className={className}>
    <Name />
    <Description />
    {hasAuthor && <Author />}
    {hasTag && <TagList />}
    {hasFooter && (
      <Footer>
        <Edit />
        <Delete />
      </Footer>
    )}
  </Container>
);

const Container = styled(CardTemplate)`
  ${Flex({ direction: 'column' })};
  padding: 1.2rem;
  position: relative;
`;

const Name = styled.span`
  width: 70%;
  height: 1.5rem;
  margin-bottom: 0.5rem;

  ${loadContent}
`;

const Description = styled.span`
  width: 30%;
  height: 1rem;
  margin-bottom: 0.2rem;

  ${loadContent}
`;

const Author = styled.span`
  height: 1rem;
  width: 20%;

  ${loadContent}
`;

const TagList = styled.div`
  height: 1rem;
  width: 90%;
  margin-top: 0.5rem;

  ${loadContent}
`;

const Footer = styled.div`
  ${Flex()};
  margin-top: 0.5rem;
  margin-left: auto;
`;

const Edit = styled.span`
  height: 1.25rem;
  width: 2rem;

  ${loadContent}
`;

const Delete = styled.span`
  height: 1.25rem;
  width: 2rem;
  margin-left: 1rem;

  ${loadContent}
`;

export default CardSkeleton;
