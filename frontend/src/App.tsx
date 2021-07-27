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
      <Suspense fallback={<div>loading</div>}>
        <SnackbarProvider>
          <ModalProvider>
            <GlobalStyle />
            <Router />
          </ModalProvider>
        </SnackbarProvider>
      </Suspense>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
