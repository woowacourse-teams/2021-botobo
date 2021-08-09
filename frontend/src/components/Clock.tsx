import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useState } from 'react';

import { Flex } from '../styles';
import { timeConverter } from '../utils';

interface Props {
  time: number;
}

interface ContainerStyleProps {
  time: string;
  isShowTime: boolean;
}

const Clock = ({ time }: Props) => {
  const [isShowTime, setIsShowTime] = useState(false);

  return (
    <Container
      onClick={() => setIsShowTime((prevValue) => !prevValue)}
      time={timeConverter(time)}
      isShowTime={isShowTime}
    >
      <ClockWrapper>
        <ClockUI />
      </ClockWrapper>
    </Container>
  );
};

const spinAnimation = keyframes`
  from {
      transform: rotate(-180deg)
  }
  to {
      transform: rotate(180deg)
  }
`;

const Container = styled.div<ContainerStyleProps>`
  position: relative;
  width: 6rem;
  height: 6rem;

  ${({ theme, time, isShowTime }) =>
    isShowTime &&
    css`
      &::after {
        content: '${time}';
        position: absolute;
        width: max-content;
        top: -1rem;
        left: 50%;
        transform: translateX(-50%);
        font-size: ${theme.fontSize.small};
      }
    `}
`;

const ClockWrapper = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  position: absolute;
  width: 100%;
  height: 100%;

  &::before {
    content: '';
    width: 0.5rem;
    height: 0.5rem;
    z-index: 2;

    ${({ theme }) => css`
      background-color: ${theme.color.white};
      border-radius: ${theme.borderRadius.circle};
      border: 2px solid ${theme.color.gray_9};
    `}
  }
`;

const ClockUI = styled.div`
  ${Flex({ justify: 'center' })};

  position: absolute;
  width: 100%;
  height: 100%;

  ${({ theme }) => css`
    background-color: ${theme.color.white};
    border: 4px solid ${theme.color.white};
    border-radius: ${theme.borderRadius.circle};
    box-shadow: inset 0 0 8px 0 ${theme.color.black}24;

    &::before {
      position: absolute;
      content: '';
      z-index: 1;
      top: 50%;
      height: 2.25rem;
      width: 3px;
      background-color: ${theme.color.red};
      animation: ${spinAnimation} 60s linear infinite;
      transform-origin: top;
    }

    &::after {
      position: absolute;
      content: '';
      top: 50%;
      height: 1.75rem;
      width: 4px;
      background-color: ${theme.color.gray_9};
      animation: ${spinAnimation} 3600s linear infinite;
      transform-origin: top;
    }
  `}
`;

export default Clock;
