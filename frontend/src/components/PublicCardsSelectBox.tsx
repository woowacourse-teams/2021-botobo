import styled from '@emotion/styled';
import React, { useState } from 'react';

import { Flex } from '../styles';
import { WorkbookResponse } from '../types';
import Button from './Button';
import SelectBox from './SelectBox';

interface Props {
  workbooks: WorkbookResponse[];
  takeCardsToMyWorkbook: (workbookId: number) => Promise<void>;
  closeModal: () => void;
}

const PublicCardsSelectBox = ({
  workbooks,
  takeCardsToMyWorkbook,
  closeModal,
}: Props) => {
  const [selectedId, setSelectedId] = useState(workbooks[0]?.id || -1);
  return (
    <Container>
      <SelectBox
        optionValues={workbooks}
        setSelectedId={(id) => setSelectedId(id)}
        title="문제집 선택"
      />
      <Button
        type="button"
        size="full"
        onClick={() => {
          takeCardsToMyWorkbook(selectedId);
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

export default PublicCardsSelectBox;
