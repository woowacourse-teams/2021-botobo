import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import ReactGA from 'react-ga';
import { RecoilRoot } from 'recoil';

import { HeaderSkeleton } from './components';
import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

ReactGA.initialize(`${process.env.REACT_APP_GA_CODE}`);

const App = () => (
  <ThemeProvider theme={theme}>
    <RecoilRoot>
      <SnackbarProvider>
        <ModalProvider>
          <Suspense fallback={<HeaderSkeleton />}>
            <GlobalStyle />
            <Router />
          </Suspense>
        </ModalProvider>
      </SnackbarProvider>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
