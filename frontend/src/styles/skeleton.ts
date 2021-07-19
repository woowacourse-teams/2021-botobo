import { css, keyframes } from '@emotion/react';

const loadingAnimation = keyframes`
  0% {
    transform: translateX(0);
  }
  50%,
  100% {
    transform: translateX(460px);
  }
`;

const loadContent = css`
  position: relative;
  overflow: hidden;
  background-color: #f1f3f5;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 30px;
    height: 100%;
    background: linear-gradient(to right, #f2f2f2, #ddd, #f2f2f2);
    animation: ${loadingAnimation} 2s infinite linear;
  }
`;

export default loadContent;
