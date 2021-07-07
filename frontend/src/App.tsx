import { ThemeProvider } from '@emotion/react';
import React from 'react';

import { Header } from './components';
import { theme } from './constants';
import GlobalStyle from './GlobalStyle';
import MainPage from './pages/MainPage';

const App = () => (
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <Header />
    <MainPage />
  </ThemeProvider>
);

export default App;
