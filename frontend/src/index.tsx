import createCache from '@emotion/cache';
import { CacheProvider } from '@emotion/react';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import App from './App';

const key = 'custom';
const cache = createCache({ key });

ReactDOM.hydrate(
  <BrowserRouter>
    <CacheProvider value={cache}>
      <RecoilRoot>
        <App />
      </RecoilRoot>
    </CacheProvider>
  </BrowserRouter>,
  document.getElementById('root')
);
