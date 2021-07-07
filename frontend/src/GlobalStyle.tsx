import { Global, css, useTheme } from '@emotion/react';
import React from 'react';

const GlobalStyle = () => {
  const theme = useTheme();

  return (
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
          background-color: ${theme.color.gray_2};
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
};

export default GlobalStyle;
