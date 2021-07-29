import styled from '@emotion/styled';
import React from 'react';
import { useSetRecoilState } from 'recoil';

import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import { workbookIdState } from '../recoil';
import { WorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import Workbook from './Workbook';

interface WorkbookProp extends WorkbookResponse {
  isChecked?: boolean;
}

interface Props {
  workbooks: WorkbookProp[];
  onClickWorkbook: (id: number) => void;
  editable?: boolean;
}

const WorkbookList = ({
  workbooks,
  onClickWorkbook,
  editable = false,
}: Props) => {
  const { routeWorkbookEdit } = useRouter();
  const setWorkbookId = useSetRecoilState(workbookIdState);

  return (
    <StyledUl>
      {workbooks.map(({ id, name, cardCount, isChecked }) => (
        <li key={id}>
          <Workbook
            name={name}
            cardCount={cardCount}
            isChecked={isChecked}
            onClick={() => onClickWorkbook(id)}
            editable={editable}
            onClickEditButton={async (event) => {
              event.stopPropagation();

              await setWorkbookId(id);
              setSessionStorage(STORAGE_KEY.WORKBOOK_ID, id);
              routeWorkbookEdit();
            }}
          />
        </li>
      ))}
    </StyledUl>
  );
};

const StyledUl = styled.ul`
  display: grid;
  grid-template-columns: repeat(1);
  gap: 1rem;
  margin: 1rem 0;
`;

export default WorkbookList;
