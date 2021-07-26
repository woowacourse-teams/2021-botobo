import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { useForm } from '../hooks';
import CardTemplate from './CardTemplate';

interface Props extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  title: string;
  inputName: string;
}

const CardTextArea = ({ title, inputName, ...props }: Props) => {
  const { values, onChange } = useForm();

  return (
    <Container>
      <Header>{title}</Header>
      <CardTemplate>
        <Text
          name={inputName}
          value={values[inputName]}
          onChange={onChange}
          {...props}
        />
      </CardTemplate>
    </Container>
  );
};

const Container = styled.div`
  width: 100%;
  margin-bottom: 2rem;
`;

const Header = styled.div`
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    font-weight: ${theme.fontWeight.bold};
  `}
`;

const Text = styled.textarea`
  width: 100%;
  border: none;
  outline: none;
  resize: none;
  height: 11rem;
  overflow-y: auto;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
  `}
`;

export default CardTextArea;
