import { ThemeProvider } from '@emotion/react';
import React from 'react';
import { useLocation } from 'react-router';

import { HeaderSkeleton, MainHeader, SsrSuspense } from './components';
import { ROUTE, theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => {
  const { pathname } = useLocation();

  const hasHeader =
    pathname !== ROUTE.GITHUB_CALLBACK.PATH &&
    pathname !== ROUTE.GOOGLE_CALLBACK.PATH;

  return (
    <ThemeProvider theme={theme}>
      <SsrSuspense fallback={<HeaderSkeleton />}>
        <SnackbarProvider>
          <ModalProvider>
            <GlobalStyle />
            {hasHeader && <MainHeader />}
            <Router />
          </ModalProvider>
        </SnackbarProvider>
      </SsrSuspense>
    </ThemeProvider>
  );
};

export default App;
