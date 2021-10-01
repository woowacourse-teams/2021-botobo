import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import FillHeartIcon from '../assets/heart-solid.svg';
import { theme } from '../constants';
import { Flex } from '../styles';
import { WorkbookResponse } from '../types';
import CardTemplate from './CardTemplate';

interface Props extends Omit<WorkbookResponse, 'id' | 'opened'> {
  path?: string;
  editable?: boolean;
  author?: string;
  onClickEditButton?: React.MouseEventHandler<HTMLButtonElement>;
  onClickDeleteButton?: React.MouseEventHandler<HTMLButtonElement>;
}

const Workbook = ({
  name,
  cardCount,
  author,
  heartCount,
  tags,
  path,
  editable,
  onClickEditButton,
  onClickDeleteButton,
}: Props) => (
  <Container
    path={path}
    editable={editable}
    onClickEditButton={onClickEditButton}
    onClickDeleteButton={onClickDeleteButton}
  >
    <TopContent>
      <InnerLeftContent>
        <Name>{name}</Name>
        <CardCount>{cardCount}개의 카드</CardCount>
        {author && <Author>{author}</Author>}
      </InnerLeftContent>
      <InnerRightContent>
        <FillHeartIcon width="1rem" height="1rem" fill={theme.color.red} />
        <span>{heartCount}</span>
      </InnerRightContent>
    </TopContent>
    <BottomContent>
      {tags.map(({ id, name }) => (
        <li key={id}>
          <Hash>#</Hash>
          {name}
        </li>
      ))}
    </BottomContent>
  </Container>
);

const Container = styled(CardTemplate)`
  ${Flex({ direction: 'column' })};
  position: relative;
  padding: 1.2rem;
`;

const TopContent = styled.div`
  ${Flex({ justify: 'space-between', items: 'flex-start' })};
  margin-bottom: 0.5rem;
`;

const InnerLeftContent = styled.div`
  width: calc(100% - 3rem);
  ${Flex({ direction: 'column', items: 'flex-start' })};
`;

const InnerRightContent = styled.div`
  ${Flex({ items: 'center' })};

  & > svg {
    margin-right: 0.3rem;
  }
`;

const Name = styled.span`
  width: 100%;
  margin-bottom: 0.5rem;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;

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
  width: 100%;
  height: 1.25rem;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;

  & > li {
    display: inline;
    margin-right: 0.2rem;

    ${({ theme }) => css`
      color: ${theme.color.gray_8};
      font-size: ${theme.fontSize.small};
    `};
  }
`;

const Hash = styled.span`
  margin-right: 0.1rem;
`;

export default Workbook;
