import styled from '@emotion/styled';
import React from 'react';

import { CARD_TEXT_MAX_LENGTH } from '../constants';
import { FormProvider } from '../contexts';
import { CardResponse } from '../types';
import Button from './Button';
import CardTextArea from './CardTextArea';

interface Props {
  cardInfo: CardResponse;
  onSubmit: (cardInfo: CardResponse) => Promise<void>;
}

const validateCardText = (value: string) => {
  if (value.length > CARD_TEXT_MAX_LENGTH) {
    throw new Error(`본문 내용은 ${CARD_TEXT_MAX_LENGTH}자 이하여야 합니다.`);
  }
};

const CardEditForm = ({ cardInfo, onSubmit }: Props) => (
  <FormProvider
    initialValues={{ question: cardInfo.question, answer: cardInfo.answer }}
    validators={{ question: validateCardText, answer: validateCardText }}
    onSubmit={({ question, answer }) => {
      onSubmit({ ...cardInfo, question, answer });
    }}
  >
    <Container>
      <CardTextArea title="질문" inputName="question" />
      <CardTextArea title="답변" inputName="answer" />
      <Button size="full">수정하기</Button>
    </Container>
  </FormProvider>
);

const Container = styled.div`
  padding: 0 1rem;
  padding-bottom: 1rem;
`;

export default CardEditForm;
