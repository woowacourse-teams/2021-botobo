import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { useForm } from '../hooks';

type FocusColor = 'gray' | 'green';

interface Props extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  name: string;
  focusColor?: FocusColor;
  maxLength?: number;
}

interface ContainerStyleProps {
  errorMessage: string | null;
}

interface TextAreaStyleProps {
  focusColor: FocusColor;
}

const TextAreaField = ({
  name,
  focusColor = 'green',
  maxLength,
  ...props
}: Props) => {
  const { values, errorMessages, onChange, onBlur } = useForm();

  return (
    <Container errorMessage={errorMessages[name]}>
      <TextArea
        focusColor={focusColor}
        onBlur={onBlur}
        name={name}
        value={values[name]}
        onChange={onChange}
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
      bottom: 0;
      left: 0;
      font-size: ${theme.fontSize.small};
    `};
  }
`;

const TextArea = styled.textarea<TextAreaStyleProps>`
  outline: none;
  resize: none;
  overflow-y: auto;

  ${({ theme, focusColor }) => css`
    border-color: ${focusColor === 'gray'
      ? theme.color.gray_8
      : theme.color.green};
  `}
`;

const Limiter = styled.div`
  text-align: right;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
  `}
`;

export default TextAreaField;
