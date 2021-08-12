import styled from '@emotion/styled';
import React, { useState } from 'react';

import { postWorkbookAsync } from '../api';
import { Flex } from '../styles';
import { WorkbookResponse } from '../types';
import Button from './Button';
import Checkbox from './Checkbox';
import SelectBox from './SelectBox';

interface Props {
  publicWorkbookName: string;
  workbooks: WorkbookResponse[];
  takeCardsToMyWorkbook: (workbookId: number) => Promise<void>;
  closeModal: () => void;
}

const PublicCardsSelectBox = ({
  publicWorkbookName,
  workbooks,
  takeCardsToMyWorkbook,
  closeModal,
}: Props) => {
  const [selectedId, setSelectedId] = useState(workbooks[0]?.id || -1);
  const [isDefaultSelected, setIsDefaultSelected] = useState(false);

  return (
    <Container>
      <div>
        {workbooks.length !== 0 && (
          <SelectBox
            optionValues={
              isDefaultSelected
                ? [{ id: 0, name: publicWorkbookName }]
                : workbooks
            }
            setSelectedId={(id) => setSelectedId(id)}
            title="문제집 선택"
            disabled={isDefaultSelected}
          />
        )}
        <CheckBoxWrapper>
          <Checkbox
            labelText={`[공유] ${publicWorkbookName}(으)로 추가하기`}
            name="defaultAdd"
            checked={isDefaultSelected}
            onChange={({ target }) => setIsDefaultSelected(target.checked)}
          />
        </CheckBoxWrapper>
      </div>
      <Button
        type="button"
        size="full"
        onClick={async () => {
          if (isDefaultSelected) {
            const { id } = await postWorkbookAsync({
              name: publicWorkbookName,
              opened: true,
              tags: [],
            });

            takeCardsToMyWorkbook(id);
          } else {
            takeCardsToMyWorkbook(selectedId);
          }

          closeModal();
        }}
      >
        확인
      </Button>
    </Container>
  );
};

const Container = styled.div`
  ${Flex({ direction: 'column', justify: 'space-between' })};
  height: 18.75rem;
`;

const CheckBoxWrapper = styled.div`
  margin-top: 1rem;
`;

export default PublicCardsSelectBox;
