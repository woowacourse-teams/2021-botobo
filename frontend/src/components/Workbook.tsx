import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';
import { WorkbookResponse } from '../types';

type PickedWorkbook = Pick<WorkbookResponse, 'name' | 'cardCount'>;

interface Props extends PickedWorkbook {
  isChecked?: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
}

const Workbook = ({ name, cardCount, isChecked, onClick }: Props) => (
  <Container isChecked={isChecked} onClick={onClick}>
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 문제</CardCount>
  </Container>
);

const Container = styled.div<Pick<Props, 'isChecked'>>`
  ${Flex({ direction: 'column' })};
  cursor: pointer;
  padding: 1rem;
  height: 7rem;

  ${({ theme, isChecked }) => css`
    background-color: ${theme.color.white};
    border-radius: ${theme.borderRadius.square};
    box-shadow: ${isChecked
      ? `${theme.boxShadow.card}, ${theme.boxShadow.inset} ${theme.color.green}`
      : theme.boxShadow.card};
  `}
`;

const Name = styled.span`
  margin: 0.3rem 0;
  word-wrap: break-word;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

const CardCount = styled.span`
  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `};
`;

export default Workbook;
