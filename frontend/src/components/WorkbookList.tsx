import styled from '@emotion/styled';
import React from 'react';

import { WorkbookResponse } from '../types';
import Workbook from './Workbook';

interface WorkbookProp extends WorkbookResponse {
  isChecked?: boolean;
}

interface Props {
  workbooks: WorkbookProp[];
  onClickWorkbook: (id: number) => void;
}

const WorkbookList = ({ workbooks, onClickWorkbook }: Props) => (
  <StyledUl>
    {workbooks.map(({ id, name, cardCount, isChecked }) => (
      <li key={id}>
        <Workbook
          name={name}
          cardCount={cardCount}
          isChecked={isChecked}
          onClick={() => onClickWorkbook(id)}
        />
      </li>
    ))}
  </StyledUl>
);

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
  margin: 1rem 0;
`;

export default WorkbookList;
