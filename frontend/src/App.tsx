import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import { RecoilRoot } from 'recoil';

import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <RecoilRoot>
      <SnackbarProvider>
        <ModalProvider>
          <Suspense fallback={<div>loading</div>}>
            <GlobalStyle />
            <Router />
          </Suspense>
        </ModalProvider>
      </SnackbarProvider>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
