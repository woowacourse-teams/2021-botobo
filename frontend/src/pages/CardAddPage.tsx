import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Button, CardTextArea, PageHeader } from '../components';
import { CARD_TEXT_MAX_LENGTH, ROUTE } from '../constants';
import { FormProvider } from '../contexts';
import { useCards } from '../hooks';

const validateCardText = (value: string) => {
  if (value.length > CARD_TEXT_MAX_LENGTH) {
    throw new Error(`본문 내용은 ${CARD_TEXT_MAX_LENGTH}자 이하여야 합니다.`);
  }
};

const CardAddPage = () => {
  const { createCard } = useCards();

  return (
    <FormProvider
      initialValues={{ question: '', answer: '' }}
      validators={{ question: validateCardText, answer: validateCardText }}
      onSubmit={({ question, answer }) => createCard(question, answer)}
    >
      <PageHeader title={ROUTE.CARD_ADD.TITLE} />
      <Container>
        <WorkbookName>데이터베이스</WorkbookName>
        <CardTextArea title="질문" inputName="question" />
        <CardTextArea title="답변" inputName="answer" />
        <Button size="full">추가하기</Button>
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

export default CardAddPage;
