import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { WorkbookResponse } from '../types';
import CardTemplate from './CardTemplate';

type PickedWorkbook = Pick<WorkbookResponse, 'name' | 'cardCount'>;

interface Props extends PickedWorkbook {
  isChecked?: boolean;
  path?: string;
  editable?: boolean;
  onClick?: React.MouseEventHandler<HTMLDivElement>;
  onClickEditButton?: React.MouseEventHandler<HTMLButtonElement>;
  onClickDeleteButton?: React.MouseEventHandler<HTMLButtonElement>;
}

const Workbook = ({
  name,
  cardCount,
  isChecked,
  path,
  editable = false,
  onClick,
  onClickEditButton,
  onClickDeleteButton,
}: Props) => (
  <CardTemplate
    editable={editable}
    isChecked={isChecked}
    path={path}
    onClick={onClick}
    onClickEditButton={onClickEditButton}
    onClickDeleteButton={onClickDeleteButton}
  >
    <Name>{name}</Name>
    <CardCount>{cardCount}개의 카드</CardCount>
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
