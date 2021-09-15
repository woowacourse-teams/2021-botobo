import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';
import { PublicWorkbookResponse } from '../types';
import CardTemplate from './CardTemplate';

type PickedPublicWorkbook = Pick<
  PublicWorkbookResponse,
  'name' | 'cardCount' | 'author'
>;

interface Props extends PickedPublicWorkbook {
  path: string;
}

const PublicWorkbook = ({ name, cardCount, author, path }: Props) => (
  <Container path={path}>
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 카드</CardCount>
    <Author>{author}</Author>
  </Container>
);

const Container = styled(CardTemplate)`
  ${Flex({ direction: 'column', justify: 'center', items: 'center' })};
`;

const Name = styled.span`
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

const CardCount = styled.span`
  margin-bottom: 0.2rem;

  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `};
`;

const Author = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `};
`;

export default PublicWorkbook;
