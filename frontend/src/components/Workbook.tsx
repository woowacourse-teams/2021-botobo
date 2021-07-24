import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { WorkbookResponse } from '../types';
import CardTemplate from './CardTemplate';

type PickedWorkbook = Pick<WorkbookResponse, 'name' | 'cardCount'>;

interface Props extends PickedWorkbook {
  isChecked?: boolean;
  onClick: React.MouseEventHandler<HTMLDivElement>;
  editable: boolean;
}

const Workbook = ({ name, cardCount, isChecked, editable, onClick }: Props) => (
  <CardTemplate editable={editable} isChecked={isChecked} onClick={onClick}>
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 문제</CardCount>
  </CardTemplate>
);

const Name = styled.div`
  margin: 0.3rem 0;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
    font-weight: ${theme.fontWeight.bold};
  `};
`;

const CardCount = styled.div`
  margin-bottom: 1rem;

  ${({ theme }) => css`
    color: ${theme.color.gray_6};
    font-size: ${theme.fontSize.small};
  `};
`;

export default Workbook;
