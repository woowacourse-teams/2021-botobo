import { ThemeProvider } from '@emotion/react';
import React from 'react';
import { RecoilRoot } from 'recoil';

import { HeaderSkeleton, SsrSuspense } from './components';
import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <RecoilRoot>
      <SnackbarProvider>
        <ModalProvider>
          <SsrSuspense fallback={<HeaderSkeleton />}>
            <GlobalStyle />
            <Router />
          </SsrSuspense>
        </ModalProvider>
      </SnackbarProvider>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
