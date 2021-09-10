import createCache from '@emotion/cache';
import { CacheProvider } from '@emotion/react';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';

import App from './App';

const key = 'custom';
const cache = createCache({ key });

ReactDOM.hydrate(
  <BrowserRouter>
    <CacheProvider value={cache}>
      <App />
    </CacheProvider>
  </BrowserRouter>,
  document.getElementById('root')
);
