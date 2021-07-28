import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';

const TOGGLE_WIDTH = 40;
const TOGGLE_HEIGHT = 20;
const TOGGLE_GUTTER = 2;
const TOGGLE_SWITCH_SIZE = TOGGLE_HEIGHT - TOGGLE_GUTTER * 2;
const TOGGLE_RADIUS = TOGGLE_HEIGHT / 2;

interface Props {
  labelText: string;
  isChecked: boolean;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
}

const Toggle = ({ labelText, isChecked, onChange }: Props) => (
  <Container>
    <LabelText>{labelText}</LabelText>
    <HiddenCheckbox type="checkbox" onChange={onChange} checked={isChecked} />
    <Switch></Switch>
  </Container>
);

const Container = styled.label`
  ${Flex({ items: 'center' })};
  position: relative;
  cursor: pointer;
  user-select: none;
`;

const LabelText = styled.span`
  margin-right: 1rem;
`;

const HiddenCheckbox = styled.input`
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;

  &:checked ~ span {
    ${({ theme }) => css`
      background-color: ${theme.color.green};
    `};

    &::after {
      left: ${TOGGLE_WIDTH - TOGGLE_SWITCH_SIZE - TOGGLE_GUTTER}px;
    }
  }
`;

const Switch = styled.span`
  width: ${TOGGLE_WIDTH}px;
  height: ${TOGGLE_HEIGHT}px;
  display: inline-block;
  position: relative;

  border-radius: ${TOGGLE_RADIUS}px;
  transition: background-color 0.15s ease-in;

  ${({ theme }) => css`
    background-color: ${theme.color.gray_5};

    &::after {
      background-color: ${theme.color.white};
    }
  `}

  &::after {
    content: '';
    position: absolute;
    left: ${TOGGLE_GUTTER}px;
    top: ${TOGGLE_GUTTER}px;
    width: ${TOGGLE_SWITCH_SIZE}px;
    height: ${TOGGLE_SWITCH_SIZE}px;
    border-radius: ${TOGGLE_RADIUS}px;
    transition: left 0.15s ease-in;
  }
`;

export default Toggle;
