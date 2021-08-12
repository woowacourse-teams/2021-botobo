import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Flex } from '../styles';

const LoadingSpinner = () => (
  <Container>
    <LoadSvg width="36px" height="36px">
      <LoadCircle cx="50%" cy="50%" r="16px" fill="transparent" />
    </LoadSvg>
  </Container>
);

const rotateCircleAnimation = keyframes`  
  100% {
    transform: rotate(360deg);
  }
`;
const loadingCircleAnimation = keyframes`
  0% {
    stroke-dashoffset: 101;
  }
  80% {
    stroke-dashoffset: -90;
  }
  100% {
    stroke-dashoffset: -101;
  }
`;

const Container = styled.div`
  ${Flex({ justify: 'center', items: 'center' })};
  position: relative;
`;

const LoadSvg = styled.svg`
  animation: ${rotateCircleAnimation} 3s infinite;
`;

const LoadCircle = styled.circle`
  stroke-width: 4;
  stroke-dasharray: 101;
  animation: ${loadingCircleAnimation} 1.3s infinite;

  ${({ theme }) => css`
    stroke: ${theme.color.gray_6};
  `}
`;

export default LoadingSpinner;
