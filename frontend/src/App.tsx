import { ThemeProvider } from '@emotion/react';
import React from 'react';

import { theme } from './constants';
import GlobalStyle from './GlobalStyle';

const App = () => (
  <ThemeProvider theme={theme}>
    <GlobalStyle />
  </ThemeProvider>
);

export default App;
