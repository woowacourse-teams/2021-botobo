import '@testing-library/jest-dom/extend-expect';

import { ThemeProvider } from '@emotion/react';
import { RenderOptions, render } from '@testing-library/react';
import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import { SsrSuspense } from './components';
import { theme } from './constants';
import { ModalProvider, SnackbarProvider } from './contexts';
import GlobalStyle from './GlobalStyle';

// eslint-disable-next-line react/prop-types
const AllTheProviders: React.FC = ({ children }) => {
  return (
    <ThemeProvider theme={theme}>
      <ModalProvider>
        <SnackbarProvider>
          <RecoilRoot>
            <SsrSuspense fallback={<div></div>}>
              <GlobalStyle />
              <BrowserRouter>{children}</BrowserRouter>
            </SsrSuspense>
          </RecoilRoot>
        </SnackbarProvider>
      </ModalProvider>
    </ThemeProvider>
  );
};

const customRender = (
  ui: React.ReactElement,
  options?: Omit<RenderOptions, 'wrapper'>
) => render(ui, { wrapper: AllTheProviders, ...options });

export * from '@testing-library/react';

export { customRender as render };
