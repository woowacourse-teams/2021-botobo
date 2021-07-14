import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import { Header } from './components';
import { ROUTE, theme } from './constants';
import GlobalStyle from './GlobalStyle';
import {
  CardsPage,
  MainPage,
  QuizPage,
  QuizResultPage,
  QuizSettingPage,
  SearchPage,
} from './pages';

const App = () => (
  <ThemeProvider theme={theme}>
    <RecoilRoot>
      <Suspense fallback={<div>loading</div>}>
        <GlobalStyle />
        <BrowserRouter>
          <Header />
          <Switch>
            <Route exact path={ROUTE.HOME}>
              <MainPage />
            </Route>
            <Route exact path={ROUTE.QUIZ_SETTING}>
              <QuizSettingPage />
            </Route>
            <Route exact path={ROUTE.QUIZ}>
              <QuizPage />
            </Route>
            <Route exact path={ROUTE.QUIZ_RESULT}>
              <QuizResultPage />
            </Route>
            <Route exact path={`/:categoryId${ROUTE.CARDS}`}>
              <CardsPage />
            </Route>
            <Route exact path={ROUTE.SEARCH}>
              <SearchPage />
            </Route>
          </Switch>
        </BrowserRouter>
      </Suspense>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
