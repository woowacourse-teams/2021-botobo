import { ThemeProvider } from '@emotion/react';
import React from 'react';

import { HeaderSkeleton, SsrSuspense } from './components';
import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <SnackbarProvider>
      <ModalProvider>
        <SsrSuspense fallback={<HeaderSkeleton />}>
          <GlobalStyle />
          <Router />
        </SsrSuspense>
      </ModalProvider>
    </SnackbarProvider>
  </ThemeProvider>
);

export default App;
