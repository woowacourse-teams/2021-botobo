import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import { RecoilRoot } from 'recoil';

import { HeaderSkeleton } from './components';
import { theme } from './constants';
import { SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

const App = () => (
  <ThemeProvider theme={theme}>
    <RecoilRoot>
      <SnackbarProvider>
        <Suspense fallback={<HeaderSkeleton />}>
          <GlobalStyle />
          <Router />
        </Suspense>
      </SnackbarProvider>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
