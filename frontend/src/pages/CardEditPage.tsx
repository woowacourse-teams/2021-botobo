import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { useRecoilValue } from 'recoil';

import { Button, CardTextArea, PageHeader } from '../components';
import { CARD_TEXT_MAX_LENGTH, ROUTE } from '../constants';
import { FormProvider } from '../contexts';
import { useCards } from '../hooks';
import { editedCardState } from '../recoil';

const validateCardText = (value: string) => {
  if (value.length > CARD_TEXT_MAX_LENGTH) {
    throw new Error(`본문 내용은 ${CARD_TEXT_MAX_LENGTH}자 이하여야 합니다.`);
  }
};

const CardEditPage = () => {
  const cardInfo = useRecoilValue(editedCardState);
  const { workbookName, editCard } = useCards();

  return (
    <FormProvider
      initialValues={{ question: cardInfo.question, answer: cardInfo.answer }}
      validators={{ question: validateCardText, answer: validateCardText }}
      onSubmit={({ question, answer }) => {
        editCard({ ...cardInfo, question, answer });
      }}
    >
      <PageHeader title={ROUTE.CARD_EDIT.TITLE} />
      <Container>
        <WorkbookName>{workbookName}</WorkbookName>
        <CardTextArea title="질문" inputName="question" />
        <CardTextArea title="답변" inputName="answer" />
        <Button size="full">수정하기</Button>
      </Container>
    </FormProvider>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
    `}
`;

const WorkbookName = styled.h2`
  margin-bottom: 2rem;
`;

export default CardEditPage;
