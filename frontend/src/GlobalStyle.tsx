import { Global, css, useTheme } from '@emotion/react';
import React from 'react';

const GlobalStyle = () => {
  const theme = useTheme();

  return (
    <Global
      styles={css`
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');
        * {
          -webkit-tap-highlight-color: rgba(0, 0, 0, 0.1);
          font-family: 'Roboto', sans-serif;
          box-sizing: border-box;
          margin: 0;
          padding: 0;
        }
        html,
        body {
          cursor: default;
          background-color: ${theme.color.gray_0};
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
          border: 0;
        }
      `}
    />
  );
};

export default GlobalStyle;
