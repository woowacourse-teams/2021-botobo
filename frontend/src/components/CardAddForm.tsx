import styled from '@emotion/styled';
import React, { useRef } from 'react';

import { CARD_TEXT_MAX_LENGTH } from '../constants';
import { FormProvider } from '../contexts';
import { useTimeout } from '../hooks';
import Button from './Button';
import CardTextArea from './CardTextArea';

interface Props {
  onSubmit: (question: string, answer: string) => Promise<void>;
}

const validateCardText = (value: string) => {
  if (value.length > CARD_TEXT_MAX_LENGTH) {
    throw new Error(`본문 내용은 ${CARD_TEXT_MAX_LENGTH}자를 넘길 수 없어요.`);
  }
};

const CardAddForm = ({ onSubmit }: Props) => {
  const inputRef = useRef<HTMLTextAreaElement>(null);

  //TODO: 딜레이를 넣어야 하는 이유를 알아 보기.
  useTimeout(() => inputRef?.current?.focus(), 100);

  return (
    <FormProvider
      initialValues={{ question: '', answer: '' }}
      keysWithNoValueAllowed={['answer']}
      validators={{ question: validateCardText, answer: validateCardText }}
      onSubmit={({ question, answer }) => onSubmit(question, answer)}
    >
      <Container>
        <CardTextArea title="질문" inputName="question" ref={inputRef} />
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
