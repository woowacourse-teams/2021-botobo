import createCache from '@emotion/cache';
import { CacheProvider } from '@emotion/react';
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import App from './App';
import { STORAGE_KEY } from './constants';
import { initRecoilStateWithSsr } from './recoil';

const cache = createCache({ key: STORAGE_KEY.EMOTION_KEY });

const initialState = window.__INITIAL_STATE__;

delete window.__INITIAL_STATE__;

ReactDOM.hydrate(
  <BrowserRouter>
    <CacheProvider value={cache}>
      <RecoilRoot
        initializeState={({ set }) => initRecoilStateWithSsr(set, initialState)}
      >
        <App />
      </RecoilRoot>
    </CacheProvider>
  </BrowserRouter>,
  document.getElementById('root')
);
