import React, { Suspense } from 'react';
import {
  BrowserRouter,
  Redirect,
  Route,
  RouteProps,
  Switch,
} from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { Button, MainHeader, PageHeader } from './components';
import { ROUTE } from './constants';
import {
  CardsLoadable,
  CardsPage,
  GithubCallbackPage,
  LoginPage,
  MainLoadable,
  MainPage,
  QuizPage,
  QuizResultPage,
  QuizSettingPage,
  SharedCardsPage,
  SharedWorkbookPage,
} from './pages';
import { loginState } from './recoil';

interface PrivateRouteProps extends RouteProps {
  children: React.ReactElement;
}

const PrivateRoute = ({ children, ...props }: PrivateRouteProps) => {
  const isLogin = useRecoilValue(loginState);

  return (
    <Route {...props}>
      {isLogin ? children : <Redirect to={ROUTE.LOGIN.PATH} />}
    </Route>
  );
};

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
      <PrivateRoute exact path={ROUTE.QUIZ_SETTING.PATH}>
        {/* TODO: 비 로그인 상태일 때, QuizStarter에서 분기 처리 */}
        <Suspense fallback={<div>loading</div>}>
          <PageHeader title={ROUTE.QUIZ_SETTING.TITLE} />
          <QuizSettingPage />
        </Suspense>
      </PrivateRoute>
      {/* TODO: Quiz, QuizResult에 진입 시, length로 분기 처리 */}
      <Route exact path={ROUTE.QUIZ.PATH}>
        <PageHeader title={ROUTE.QUIZ.TITLE} />
        <QuizPage />
      </Route>
      <Route exact path={ROUTE.QUIZ_RESULT.PATH}>
        <PageHeader title={ROUTE.QUIZ_RESULT.TITLE} />
        <QuizResultPage />
      </Route>
      <Route exact path={ROUTE.CARDS.PATH}>
        <Suspense fallback={<CardsLoadable />}>
          <PageHeader title={ROUTE.CARDS.TITLE} />
          <CardsPage />
        </Suspense>
      </Route>
      <Route exact path={ROUTE.SHARED_WORKBOOK.PATH}>
        <PageHeader title={ROUTE.SHARED_WORKBOOK.TITLE} />
        <SharedWorkbookPage />
      </Route>
      <Route exact path={ROUTE.SHARED_CARDS.PATH}>
        <SharedCardsPage />
      </Route>
      <Route exact path={ROUTE.GITHUB_CALLBACK.PATH}>
        <GithubCallbackPage />
      </Route>
    </Switch>
  </BrowserRouter>
);

export default Router;
