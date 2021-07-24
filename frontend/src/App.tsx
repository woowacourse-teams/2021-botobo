import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import { RecoilRoot } from 'recoil';

import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <ModalProvider>
      <SnackbarProvider>
        <RecoilRoot>
          <GlobalStyle />
          <Suspense fallback={<div>loading</div>}>
            <Router />
          </Suspense>
        </RecoilRoot>
      </SnackbarProvider>
    </ModalProvider>
  </ThemeProvider>
);

export default App;
