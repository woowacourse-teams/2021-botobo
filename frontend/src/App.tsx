import { ThemeProvider } from '@emotion/react';
import React from 'react';
import { RecoilRoot } from 'recoil';

import { theme } from './constants';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <RecoilRoot>
      <GlobalStyle />
      <Router />
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
