import { ThemeProvider } from '@emotion/react';
import React, { Suspense } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import { GlobalHeader, PageHeader } from './components';
import { ROUTE, theme } from './constants';
import GlobalStyle from './GlobalStyle';
import {
  CardsPage,
  LoginPage,
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
          <Switch>
            <Route exact path={ROUTE.HOME}>
              <GlobalHeader />
              <MainPage />
            </Route>
            <Route exact path={ROUTE.LOGIN}>
              <PageHeader title={'로그인'} />
              <LoginPage />
            </Route>
            <Route exact path={ROUTE.QUIZ_SETTING}>
              <GlobalHeader />
              <QuizSettingPage />
            </Route>
            <Route exact path={ROUTE.QUIZ}>
              <GlobalHeader />
              <QuizPage />
            </Route>
            <Route exact path={ROUTE.QUIZ_RESULT}>
              <GlobalHeader />
              <QuizResultPage />
            </Route>
            <Route exact path={ROUTE.CARDS}>
              <GlobalHeader />
              <CardsPage />
            </Route>
            <Route exact path={ROUTE.SEARCH}>
              <GlobalHeader />
              <SearchPage />
            </Route>
          </Switch>
        </BrowserRouter>
      </Suspense>
    </RecoilRoot>
  </ThemeProvider>
);

export default App;
