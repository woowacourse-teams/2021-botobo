import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { forwardRef, useEffect, useState } from 'react';

import { CARD_TEXT_MAX_LENGTH } from '../constants';
import { useForm } from '../hooks';
import { Flex } from '../styles';
import CardTemplate from './CardTemplate';

interface Props extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  title: string;
  inputName: string;
}

interface ContainerStyleProps {
  errorMessage: string | null;
}

interface TextStyleProps {
  windowHeight: number;
}

const CardTextArea = forwardRef<HTMLTextAreaElement, Props>(
  ({ title, inputName, ...props }: Props, ref) => {
    const { values, errorMessages, onChange, onBlur } = useForm();
    const [isFocus, setIsFocus] = useState(false);
    const [windowHeight, setWindowHeight] = useState(window.innerHeight);

    useEffect(() => {
      const onResize = () => {
        setWindowHeight(window.innerHeight);
      };

      window.addEventListener('resize', onResize);

      return () => window.removeEventListener('resize', onResize);
    }, []);

    return (
      <Container errorMessage={errorMessages[inputName]}>
        <Header>
          <span>{title}</span>
          <Limiter>
            {values[inputName].length}/{CARD_TEXT_MAX_LENGTH}
          </Limiter>
        </Header>
        <CardTemplate isChecked={isFocus}>
          <Text
            ref={ref}
            name={inputName}
            value={values[inputName]}
            onChange={onChange}
            onFocus={({ currentTarget }) => {
              setIsFocus(true);
              currentTarget.setSelectionRange(
                currentTarget.value.length,
                currentTarget.value.length
              );
            }}
            onBlur={(event) => {
              onBlur(event);
              setIsFocus(false);
            }}
            windowHeight={windowHeight}
            {...props}
          />
        </CardTemplate>
      </Container>
    );
  }
);

const Container = styled.div<ContainerStyleProps>`
  position: relative;
  width: 100%;
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

const Header = styled.div`
  ${Flex({ justify: 'space-between', items: 'flex-end' })};
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.default};
    font-weight: ${theme.fontWeight.bold};
  `}
`;

const Limiter = styled.span`
  ${({ theme }) => css`
    font-size: ${theme.fontSize.small};
  `}
`;

const Text = styled.textarea<TextStyleProps>`
  width: 100%;
  border: none;
  outline: none;
  resize: none;
  overflow-y: auto;

  ${({ theme, windowHeight }) => css`
    font-size: ${theme.fontSize.default};
    height: ${(windowHeight - 360) * 0.5}px;
  `}
`;

CardTextArea.displayName = 'CardTextArea';
export default CardTextArea;
