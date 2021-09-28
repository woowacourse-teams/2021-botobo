import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex, loadContent } from '../styles';
import { CardTemplate } from '.';

interface Props {
  hasHeader?: boolean;
  hasFooter?: boolean;
  className?: string;
}

const QnACardSkeleton = ({
  hasHeader = false,
  hasFooter = false,
  className,
}: Props) => (
  <Container className={className}>
    {hasHeader && <Header />}
    <Question />
    <Border />
    <Answer />
    {hasFooter && (
      <Footer>
        <Edit></Edit>
        <Delete></Delete>
      </Footer>
    )}
  </Container>
);

const Container = styled(CardTemplate)`
  ${Flex({ direction: 'column' })};
  position: relative;
`;

const Header = styled.div`
  height: 1.25rem;
  width: 20%;

  ${loadContent}
`;

const Question = styled.div`
  margin: 1rem 0;
  height: 1.5rem;
  width: 50%;

  ${loadContent}
`;

const Border = styled.div`
  ${({ theme }) => css`
    border-bottom: 2px solid ${theme.color.gray_3};
  `}
`;

const Answer = styled.div`
  margin-top: 1.5rem;
  margin-bottom: 1rem;
  height: 1.5rem;
  width: 80%;

  ${loadContent}
`;

const Footer = styled.div`
  ${Flex()};
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

export default QnACardSkeleton;
