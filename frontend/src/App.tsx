import { ThemeProvider } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';

import { Header } from './components';
import { theme } from './constants';
import GlobalStyle from './GlobalStyle';
import MainPage from './pages/MainPage';

const App = () => (
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <Header />
    <PageWrapper>
      <MainPage />
    </PageWrapper>
  </ThemeProvider>
);

const PageWrapper = styled.div`
  padding: 2rem 1.25rem;
  height: calc(100% - 3.75rem);
`;

export default App;
