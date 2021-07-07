import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

interface Props {
  children: string;
  backgroundColor?: 'pink';
  hasShadow?: boolean;
  size?: keyof typeof buttonSize;
}

const buttonSize = {
  default: 'max-content',
  full: '100%',
};

const Button = ({
  children,
  backgroundColor,
  hasShadow,
  size = 'default',
}: Props) => (
  <StyledButton
    backgroundColor={backgroundColor}
    hasShadow={hasShadow}
    size={size}
  >
    {children}
  </StyledButton>
);

const StyledButton = styled.button<Omit<Props, 'children'>>`
  font-size: 1rem;
  padding: 0.5rem;
  width: ${({ size }) => size && buttonSize[size]};
  box-shadow: ${({ hasShadow, theme }) => hasShadow && theme.boxShadow.button};
  border-radius: ${({ theme }) => theme.borderRadius.square_1};
  ${({ backgroundColor, theme }) =>
    backgroundColor &&
    css`
      color: ${theme.color.white};
      background-color: ${theme.color.pink};
    `}
`;

export default Button;
