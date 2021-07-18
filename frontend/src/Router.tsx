import React, { Suspense } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import { MainHeader, PageHeader } from './components';
import { ROUTE } from './constants';
import {
  CardsPage,
  GithubCallbackPage,
  LoginPage,
  MainLoadable,
  MainPage,
  QuizPage,
  QuizResultPage,
  QuizSettingPage,
  SearchPage,
} from './pages';

const Router = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path={ROUTE.HOME.PATH}>
        <Suspense fallback={<MainLoadable />}>
          <MainHeader />
          <MainPage />
        </Suspense>
      </Route>
      <Route exact path={ROUTE.LOGIN.PATH}>
        <PageHeader title={ROUTE.LOGIN.TITLE} />
        <LoginPage />
      </Route>
      <Route exact path={ROUTE.QUIZ_SETTING.PATH}>
        <PageHeader title={ROUTE.QUIZ_SETTING.TITLE} />
        <QuizSettingPage />
      </Route>
      <Route exact path={ROUTE.QUIZ.PATH}>
        <Suspense fallback={<div>loading</div>}>
          <PageHeader title={ROUTE.QUIZ.TITLE} />
          <QuizPage />
        </Suspense>
      </Route>
      <Route exact path={ROUTE.QUIZ_RESULT.PATH}>
        <PageHeader title={ROUTE.QUIZ_RESULT.TITLE} />
        <QuizResultPage />
      </Route>
      <Route exact path={ROUTE.CARDS.PATH}>
        <Suspense fallback={<div>loading</div>}>
          <PageHeader title={ROUTE.CARDS.TITLE} />
          <CardsPage />
        </Suspense>
      </Route>
      <Route exact path={ROUTE.SEARCH.PATH}>
        <PageHeader title={ROUTE.SEARCH.TITLE} />
        <SearchPage />
      </Route>
      <Route exact path={ROUTE.GITHUB_CALLBACK.PATH}>
        <GithubCallbackPage />
      </Route>
    </Switch>
  </BrowserRouter>
);

export default Router;
