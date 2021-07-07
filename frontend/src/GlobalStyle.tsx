import { Global, css } from '@emotion/react';
import React from 'react';

const GlobalStyle = () => (
  <Global
    styles={css`
      * {
        /* font-family: 'Noto Sans KR', sans-serif; */
        box-sizing: border-box;
        margin: 0;
        padding: 0;
      }
      html,
      body {
        height: 100%;
        cursor: default;
      }
      #root {
        height: 100%;
      }
      a {
        text-decoration: none;
        outline: none;
        &:visited {
          color: inherit;
        }
      }
      li {
        list-style: none;
      }
      button {
        cursor: pointer;
        background: transparent;
        border: 0;
      }
    `}
  />
);

export default GlobalStyle;
