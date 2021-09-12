import { ThemeProvider } from '@emotion/react';
import React from 'react';

import { HeaderSkeleton, SsrSuspense } from './components';
import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <SsrSuspense fallback={<HeaderSkeleton />}>
      <SnackbarProvider>
        <ModalProvider>
          <GlobalStyle />
          <Router />
        </ModalProvider>
      </SnackbarProvider>
    </SsrSuspense>
  </ThemeProvider>
);

export default App;
