import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import CheckIcon from '../assets/check.svg';
import { Flex } from '../styles';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
  labelText: string;
}

const Checkbox = ({ name, labelText, checked, onChange }: Props) => (
  <Container>
    <StyledInput
      type="checkbox"
      id={name}
      name={name}
      checked={checked}
      onChange={onChange}
    />
    <Label htmlFor={name}>
      <CheckboxShape>
        <CheckSvg />
      </CheckboxShape>
      <LabelText>{labelText}</LabelText>
    </Label>
  </Container>
);

const waveAnimation = keyframes`
  50% {
    transform: scale(0.9);
  }
`;

const Container = styled.div`
  ${Flex({ items: 'center' })};

  input:checked + label span:first-of-type {
    animation: ${waveAnimation} 0.4s ease;

    ${({ theme }) => css`
      border-color: ${theme.color.green};
      background: ${theme.color.green};
    `}

    svg {
      stroke-dashoffset: 0;
    }
  }
`;

const StyledInput = styled.input`
  display: none;
`;

const Label = styled.label`
  user-select: none;
  cursor: pointer;

  &:hover span:first-of-type {
    ${({ theme }) => css`
      border-color: ${theme.color.green};
    `}
  }
`;

const CheckboxShape = styled.span`
  display: inline-block;
  position: relative;
  width: 18px;
  height: 18px;
  border-radius: 3px;
  transform: scale(1);
  vertical-align: middle;
  transition: all 0.3s ease;

  ${({ theme }) => css`
    border: 1px solid ${theme.color.gray_7};
  `}
`;

const CheckSvg = styled(CheckIcon)`
  position: absolute;
  top: 3px;
  left: 2px;
  transition: all 0.3s ease;
  transition-delay: 0.1s;
`;

const LabelText = styled.span`
  vertical-align: middle;
  padding-left: 8px;
`;

export default Checkbox;
