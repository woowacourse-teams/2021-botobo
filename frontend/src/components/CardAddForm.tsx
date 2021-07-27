import styled from '@emotion/styled';
import React from 'react';

import { CARD_TEXT_MAX_LENGTH } from '../constants';
import { FormProvider } from '../contexts';
import { Button, CardTextArea } from '.';

interface Props {
  onSubmit: (question: string, answer: string) => Promise<void>;
}

const validateCardText = (value: string) => {
  if (value.length > CARD_TEXT_MAX_LENGTH) {
    throw new Error(`본문 내용은 ${CARD_TEXT_MAX_LENGTH}자 이하여야 합니다.`);
  }
};

const CardAddForm = ({ onSubmit }: Props) => {
  return (
    <FormProvider
      initialValues={{ question: '', answer: '' }}
      validators={{ question: validateCardText, answer: validateCardText }}
      onSubmit={({ question, answer }) => onSubmit(question, answer)}
    >
      <Container>
        <CardTextArea title="질문" inputName="question" />
        <CardTextArea title="답변" inputName="answer" />
        <Button size="full">추가하기</Button>
      </Container>
    </FormProvider>
  );
};

const Container = styled.div`
  padding: 0 1rem;
  padding-bottom: 1rem;
`;

export default CardAddForm;
