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

import App from '../src/App';
import { STORAGE_KEY } from '../src/constants';
import { initRecoilStateWithSsr } from '../src/recoil';
import {
  getRankingSearchKeywords,
  getRankingWorkbooks,
  getUserInfo,
  getWorkbook,
  initRequest,
} from './initialState';

const PORT = process.env.PORT || 3000;
const app = express();

const setHeaders = ({ res, isIndexHtml }) => {
  if (isIndexHtml) {
    res.set({
      'Cache-Control': 'no-store',
      'Keep-Alive': 'timeout=0',
    });

    return;
  }

  res.set({
    'Cache-Control': 'max-age=31536000',
    'Keep-Alive': 'timeout=0',
  });
};

const cache = createCache({ key: STORAGE_KEY.EMOTION_KEY });
const { extractCriticalToChunks, constructStyleTagsFromChunks } =
  createEmotionServer(cache);

app.get('/', async (req, res) => {
  const indexFile = path.resolve(__dirname, '../dist/index.html');

  try {
    initRequest(req.headers.cookie);
    const userState = await getUserInfo(res);
    const workbookState = await getWorkbook(userState, res);
    const rankingWorkbooksState = await getRankingWorkbooks();
    const rankingSearchKeywordsState = await getRankingSearchKeywords();

    const initialState = {
      userState,
      workbookState,
      rankingWorkbooksState,
      rankingSearchKeywordsState,
    };

    const context = {};
    const app = (
      <StaticRouter location={req.url} context={context}>
        <CacheProvider value={cache}>
          <RecoilRoot
            initializeState={({ set }) =>
              initRecoilStateWithSsr(set, initialState)
            }
          >
            <App />
          </RecoilRoot>
        </CacheProvider>
      </StaticRouter>
    );

    const { html, styles } = extractCriticalToChunks(
      ReactDOMServer.renderToString(app)
    );

    fs.readFile(indexFile, 'utf8', (err, data) => {
      if (err) {
        console.error('page not found: ', err);
        return res
          .status(404)
          .send('페이지를 찾을 수 없습니다. 다시 시도해주세요.');
      }

      let newHTML = data.replace(
        '<style id="emotion"></style>',
        constructStyleTagsFromChunks({ html, styles })
      );

      newHTML = newHTML.replace(
        '<script id="initial-state"></script>',
        `<script id="initial-state">window.__INITIAL_STATE__=${JSON.stringify(
          initialState
        ).replace(/</g, '\\u003c')}</script>`
      );

      newHTML = newHTML.replace(
        '<div id="root"></div>',
        `<div id="root">${html}</div>`
      );

      setHeaders({ res, isIndexHtml: true });

      return res.send(newHTML);
    });
  } catch (err) {
    console.error('Something went wrong:', err);

    fs.readFile(indexFile, 'utf-8', (err, data) => {
      if (err) {
        console.error('page not found: ', err);
        return res
          .status(404)
          .send('페이지를 찾을 수 없습니다. 다시 시도해주세요.');
      }

      setHeaders({ res, isIndexHtml: true });

      return res.send(data);
    });
  }
});

app.use(history());

app.use(
  express.static(path.resolve(__dirname, '../dist'), {
    setHeaders: (res, path) => {
      if (/index.html/.test(path)) {
        setHeaders({ res, isIndexHtml: true });

        return;
      }

      setHeaders({ res, isIndexHtml: false });
    },
  })
);

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
