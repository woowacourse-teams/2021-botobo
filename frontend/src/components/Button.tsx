import { Color, css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

interface ButtonProps {
  children: string | React.ReactElement;
  hasShadow?: boolean;
  backgroundColor?: keyof Color;
  color?: keyof Color;
  size?: keyof typeof buttonSize;
  shape?: 'circle' | 'square' | 'rectangle' | 'round';
  inversion?: boolean;
}

type Props = ButtonProps & React.ButtonHTMLAttributes<HTMLButtonElement>;

type StyledProps = Required<Omit<ButtonProps, 'children'>>;

const Button = ({
  children,
  hasShadow = false,
  backgroundColor = 'green',
  color = 'white',
  size = 'default',
  shape = 'square',
  inversion = false,
  ...props
}: Props) => (
  <StyledButton
    hasShadow={hasShadow}
    backgroundColor={backgroundColor}
    color={color}
    size={size}
    shape={shape}
    inversion={inversion}
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
    round: css``,
  },
  full: {
    rectangle: css`
      width: 100%;
      height: 3.5rem;
    `,
    square: css`
      width: 100%;
      height: 2.5rem;
    `,
    circle: null,
    round: css``,
  },
};

const StyledButton = styled.button<StyledProps>`
  font-size: 1rem;
  line-height: 1rem;

  padding: 0.5rem;

  box-shadow: ${({ hasShadow, theme }) =>
    hasShadow ? theme.boxShadow.button : ''};

  ${({ theme, shape, backgroundColor, color, size, inversion }) => css`
    background-color: ${theme.color[backgroundColor]};
    color: ${theme.color[color]};

    ${inversion &&
    css`
      background-color: transparent;
      -webkit-box-shadow: ${`${theme.boxShadow.inset} ${theme.color[backgroundColor]}`};
      box-shadow: ${`${theme.boxShadow.inset} ${theme.color[backgroundColor]}`};
      color: ${theme.color[backgroundColor]};
    `}

    border-radius: ${theme.borderRadius[shape]};

    ${buttonSize[size][shape]};

    &:not(:disabled):hover {
      filter: brightness(95%);
    }
  `}
`;

export default Button;
