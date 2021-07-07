import { ThemeProvider } from '@emotion/react';
import React from 'react';

import { Header } from './components';
import { theme } from './constants';
import GlobalStyle from './GlobalStyle';

const App = () => (
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <Header />
  </ThemeProvider>
);

export default App;
