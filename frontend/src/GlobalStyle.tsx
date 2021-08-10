import { Global, css, useTheme } from '@emotion/react';
import React from 'react';

const GlobalStyle = () => {
  const theme = useTheme();

  return (
    <Global
      styles={css`
        * {
          -webkit-tap-highlight-color: rgba(0, 0, 0, 0.1);
          font-family: 'Pretendard';
          box-sizing: border-box;
          margin: 0;
          padding: 0;
        }
        html,
        body {
          cursor: default;
          background-color: ${theme.color.gray_0};
          color: ${theme.color.gray_9};
        }
        a {
          text-decoration: none;
          outline: none;
          color: ${theme.color.gray_9};

          &:visited {
            color: inherit;
          }
        }
        li {
          list-style: none;
        }
        button {
          cursor: pointer;
          border: 0;
          background-color: transparent;
          font-size: ${theme.fontSize.default};
          color: ${theme.color.gray_9};
        }
      `}
    />
  );
};

export default GlobalStyle;
