import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import { useForm } from '../hooks';

type FocusColor = 'gray' | 'green';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
  name: string;
  focusColor?: FocusColor;
}

interface ContainerStyleProps {
  errorMessage: string | null;
}

interface InputStyleProps {
  isFocus: boolean;
  focusColor: FocusColor;
}

const InputField = ({ name, focusColor = 'green', ...props }: Props) => {
  const { values, errorMessages, onChange } = useForm();
  const [isFocus, setIsFocus] = useState(false);

  return (
    <Container errorMessage={errorMessages[name]}>
      <Input
        isFocus={isFocus}
        onFocus={() => setIsFocus(true)}
        onBlur={() => setIsFocus(false)}
        focusColor={focusColor}
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
  ${({ theme, isFocus, focusColor }) => css`
    ${isFocus &&
    css`
      && {
        border-color: ${focusColor === 'gray'
          ? theme.color.gray_8
          : theme.color.green};
      }
    `}
  `}
`;

export default InputField;
