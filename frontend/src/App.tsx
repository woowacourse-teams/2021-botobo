import { ThemeProvider } from '@emotion/react';
import React from 'react';
import { RecoilRoot } from 'recoil';

import { theme } from './constants';
import { SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <SnackbarProvider>
      <RecoilRoot>
        <GlobalStyle />
        <Router />
      </RecoilRoot>
    </SnackbarProvider>
  </ThemeProvider>
);

export default App;
