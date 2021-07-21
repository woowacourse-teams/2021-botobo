import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';
import { SharedWorkbookResponse } from '../types';

type PickedSharedWorkbook = Pick<
  SharedWorkbookResponse,
  'name' | 'cardCount' | 'author'
>;

interface Props extends PickedSharedWorkbook {
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const SharedWorkbook = ({ name, cardCount, author, onClick }: Props) => (
  <Container onClick={onClick}>
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 카드</CardCount>
    <Author>{author}</Author>
  </Container>
);

const Container = styled.div`
  ${Flex({ direction: 'column', justify: 'center', items: 'center' })};
  cursor: pointer;
  padding: 1rem;
  word-break: break-all;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${theme.boxShadow.card};
  `}
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

export default SharedWorkbook;
