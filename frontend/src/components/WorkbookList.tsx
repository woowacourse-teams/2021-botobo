import styled from '@emotion/styled';
import React from 'react';
import { useSetRecoilState } from 'recoil';

import { STORAGE_KEY } from '../constants';
import { useRouter } from '../hooks';
import useModal from '../hooks/useModal';
import { workbookIdState } from '../recoil';
import { WorkbookResponse } from '../types';
import { setSessionStorage } from '../utils';
import Confirm from './Confirm';
import Workbook from './Workbook';

interface WorkbookProp extends WorkbookResponse {
  isChecked?: boolean;
}

interface Props {
  workbooks: WorkbookProp[];
  onClickWorkbook: (id: number) => void;
  editable?: boolean;
  deleteWorkbook?: (id: number) => Promise<void>;
}

const WorkbookList = ({
  workbooks,
  onClickWorkbook,
  editable = false,
  deleteWorkbook,
}: Props) => {
  const { routeWorkbookEdit } = useRouter();
  const setWorkbookId = useSetRecoilState(workbookIdState);
  const { openModal } = useModal();

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
            onClickDeleteButton={(event) => {
              event.stopPropagation();

              openModal({
                content: (
                  <Confirm onConfirm={() => deleteWorkbook?.(id)}>
                    해당 문제집을 정말 삭제하시겠어요?
                  </Confirm>
                ),
                type: 'center',
              });
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
