import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { useForm } from '../hooks';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
  name: string;
  isFocus: boolean;
}

interface ContainerStyleProps {
  errorMessage: string | null;
}

interface InputStyleProps {
  isFocus: boolean;
}

const InputField = ({ name, isFocus, ...props }: Props) => {
  const { values, errorMessages, onChange } = useForm();

  return (
    <Container errorMessage={errorMessages[name]}>
      <Input
        isFocus={isFocus}
        name={name}
        value={values[name]}
        onChange={onChange}
        {...props}
      />
    </Container>
  );
};

const Container = styled.div<ContainerStyleProps>`
  position: relative;
  margin-bottom: 2rem;

  &::after {
    ${({ theme, errorMessage }) => css`
      content: ${`'${errorMessage ?? ''}'`};
      color: ${theme.color.red};
      position: absolute;
      left: 0;
      bottom: -1.2rem;
      font-size: ${theme.fontSize.small};
    `};
  }
`;

const Input = styled.input<InputStyleProps>`
  ${({ theme, isFocus }) => css`
    ${isFocus &&
    css`
      && {
        border-color: ${theme.color.gray_8};
      }
    `}
  `}
`;

export default InputField;
