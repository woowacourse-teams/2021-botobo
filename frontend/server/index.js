import fs from 'fs';
import path from 'path';

import createCache from '@emotion/cache';
import { CacheProvider } from '@emotion/react';
import createEmotionServer from '@emotion/server/create-instance';
import history from 'connect-history-api-fallback';
import express from 'express';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import { StaticRouter } from 'react-router';
import { RecoilRoot } from 'recoil';

import { getUserInfoAsync } from '../src/api';
import { request } from '../src/api/request';
import App from '../src/App';
import { STORAGE_KEY } from '../src/constants';
import { userState } from '../src/recoil';

const PORT = process.env.PORT || 3000;
const app = express();

const key = 'custom';
const cache = createCache({ key });
const { extractCriticalToChunks, constructStyleTagsFromChunks } =
  createEmotionServer(cache);

const getCookie = (name, cookies) => {
  const key = `${name}=`;

  return cookies
    .split('; ')
    ?.find((cookie) => cookie.includes(key))
    ?.slice(name.length + 1);
};

app.get('/', async (req, res) => {
  const token = getCookie(STORAGE_KEY.TOKEN, req.headers.cookie);

  request.defaults.headers.common['Authorization'] = token
    ? `Bearer ${token}`
    : '';

  const userInfo = await getUserInfoAsync();

  const context = {};
  const app = (
    <StaticRouter location={req.url} context={context}>
      <CacheProvider value={cache}>
        <RecoilRoot initializeState={({ set }) => set(userState, userInfo)}>
          <App />
        </RecoilRoot>
      </CacheProvider>
    </StaticRouter>
  );

  const { html, styles } = extractCriticalToChunks(
    ReactDOMServer.renderToString(app)
  );

  const indexFile = path.resolve(__dirname, '../dist/index.html');
  fs.readFile(indexFile, 'utf8', (err, data) => {
    if (err) {
      console.error('Something went wrong:', err);
      return res.status(500).send('Oops, better luck next time!');
    }

    let newHTML = data.replace(
      '<style id="emotion"></style>',
      constructStyleTagsFromChunks({ html, styles })
    );

    newHTML = newHTML.replace(
      '<div id="root"></div>',
      `<div id="root">${html}</div>`
    );

    return res.send(newHTML);
  });
});

app.use(history());

app.use(express.static(path.resolve(__dirname, '../dist')));

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
