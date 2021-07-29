import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import { useForm } from '../hooks';

type FocusColor = 'gray' | 'green';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
  name: string;
  focusColor?: FocusColor;
  maxLength?: number;
}

interface ContainerStyleProps {
  errorMessage: string | null;
}

interface InputStyleProps {
  isFocus: boolean;
  focusColor: FocusColor;
}

const InputField = ({
  name,
  focusColor = 'green',
  maxLength,
  ...props
}: Props) => {
  const { values, errorMessages, onChange, onBlur } = useForm();
  const [isFocus, setIsFocus] = useState(false);

  return (
    <Container errorMessage={errorMessages[name]}>
      <Input
        isFocus={isFocus}
        onFocus={() => setIsFocus(true)}
        onBlur={(event) => {
          onBlur(event);
          setIsFocus(false);
        }}
        focusColor={focusColor}
        name={name}
        value={values[name]}
        onChange={onChange}
        onKeyPress={(event) => {
          if (event.key === 'Enter') {
            event.preventDefault();
          }
        }}
        {...props}
      />
      {maxLength && (
        <Limiter>
          {values[name].length}/{maxLength}
        </Limiter>
      )}
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

const Limiter = styled.span`
  position: absolute;
  right: 0;
  bottom: -1.2rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
  `}
`;

export default InputField;
