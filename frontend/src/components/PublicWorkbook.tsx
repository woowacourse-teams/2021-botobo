import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import FillHeartIcon from '../assets/heart-solid.svg';
import { theme } from '../constants';
import { Flex } from '../styles';
import { PublicWorkbookResponse } from '../types';
import CardTemplate from './CardTemplate';

interface Props extends Omit<PublicWorkbookResponse, 'id'> {
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const PublicWorkbook = ({
  name,
  cardCount,
  author,
  heartCount,
  tags,
  onClick,
}: Props) => (
  <Container onClick={onClick}>
    <TopContent>
      <Name>{name}</Name>
      <CardCount>{cardCount}개의 카드</CardCount>
      <Author>{author}</Author>
    </TopContent>
    <BottomContent>
      {tags.map(({ id, name }) => (
        <li key={id}>#{name}</li>
      ))}
    </BottomContent>
    <HeartWrapper>
      <FillHeartIcon width="1rem" height="1rem" fill={theme.color.red} />
      <span>{heartCount}</span>
    </HeartWrapper>
  </Container>
);

const Container = styled(CardTemplate)`
  ${Flex({ direction: 'column' })};
  position: relative;
  padding: 1.2rem;
`;

const TopContent = styled.div`
  ${Flex({ direction: 'column', items: 'flex-start' })};
  margin-bottom: 1rem;
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

const BottomContent = styled.ul`
  & > li {
    display: inline;
    margin-right: 0.2rem;

    ${({ theme }) => css`
      color: ${theme.color.gray_8};
      font-size: ${theme.fontSize.small};
    `};
  }
`;

const HeartWrapper = styled.div`
  ${Flex({ items: 'center' })};
  position: absolute;
  top: 1.3rem;
  right: 1.3rem;

  & > svg {
    margin-right: 0.3rem;
  }
`;

export default PublicWorkbook;
