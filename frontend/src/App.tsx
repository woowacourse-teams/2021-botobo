import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import ReactGA from 'react-ga';
import { RecoilRoot } from 'recoil';

import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';
import Router from './Router';

ReactGA.initialize('UA-203684869-1');
ReactGA.pageview(window.location.pathname + window.location.search);

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
