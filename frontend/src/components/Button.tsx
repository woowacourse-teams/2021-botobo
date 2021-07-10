import { Color, css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: string | React.ReactElement;
  backgroundColor?: keyof Color;
  hasShadow?: boolean;
  size?: keyof typeof buttonSize;
  shape?: 'circle' | 'square' | 'rectangle';
}

type StyledProps = Required<
  Pick<Props, 'backgroundColor' | 'hasShadow' | 'size' | 'shape'>
>;

const Button = ({
  children,
  hasShadow = false,
  backgroundColor = 'pink',
  size = 'default',
  shape = 'square',
  ...props
}: Props) => (
  <StyledButton
    backgroundColor={backgroundColor}
    hasShadow={hasShadow}
    size={size}
    shape={shape}
    {...props}
  >
    {children}
  </StyledButton>
);

const buttonSize = {
  default: {
    rectangle: css`
      width: max-content;
    `,
    square: css`
      width: max-content;
    `,
    circle: css`
      width: 2.5rem;
      height: 2.5rem;
    `,
  },
  full: {
    rectangle: css`
      width: 100%;
      height: 3rem;
    `,
    square: css`
      width: 100%;
      height: 3rem;
    `,
    circle: null,
  },
};

const StyledButton = styled.button<StyledProps>`
  font-size: 1rem;
  line-height: 1rem;

  padding: 0.5rem;

  box-shadow: ${({ hasShadow, theme }) =>
    hasShadow ? theme.boxShadow.button : ''};

  ${({ theme, shape, backgroundColor, size }) => css`
    background-color: ${theme.color[backgroundColor]};

    border-radius: ${theme.borderRadius[shape]};

    color: ${backgroundColor === 'white'
      ? theme.color.green
      : theme.color.white};

    ${buttonSize[size][shape]};
  `}
`;

export default Button;
