import React, { Suspense } from 'react';
import {
  BrowserRouter,
  Redirect,
  Route,
  RouteProps,
  Switch,
} from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { PageHeader } from './components';
import { ROUTE } from './constants';
import {
  CardsLoadable,
  CardsPage,
  GithubCallbackPage,
  LoginPage,
  MainLoadable,
  MainPage,
  PublicCardsPage,
  PublicWorkbookPage,
  QuizPage,
  QuizResultPage,
  QuizSettingPage,
  WorkbookAddPage,
  WorkbookEditPage,
} from './pages';
import { userState } from './recoil';

interface PrivateRouteProps extends RouteProps {
  children: React.ReactElement | React.ReactElement[];
}

const PrivateRoute = ({ children, ...props }: PrivateRouteProps) => {
  const userInfo = useRecoilValue(userState);

  return (
    <Route {...props}>
      {userInfo ? children : <Redirect to={ROUTE.LOGIN.PATH} />}
    </Route>
  );
};

const Router = () => (
  <BrowserRouter>
    <Switch>
      <Route exact path={ROUTE.HOME.PATH}>
        <Suspense fallback={<MainLoadable />}>
          <MainPage />
        </Suspense>
      </Route>
      <Route exact path={ROUTE.LOGIN.PATH}>
        <PageHeader title={ROUTE.LOGIN.TITLE} />
        <LoginPage />
      </Route>
      <PrivateRoute exact path={ROUTE.WORKBOOK_ADD.PATH}>
        <WorkbookAddPage />
      </PrivateRoute>
      <PrivateRoute exact path={ROUTE.WORKBOOK_EDIT.PATH}>
        <WorkbookEditPage />
      </PrivateRoute>
      <PrivateRoute exact path={ROUTE.QUIZ_SETTING.PATH}>
        {/* TODO: 스켈레톤으로 변경 */}
        <Suspense fallback={<div>loading</div>}>
          <PageHeader title={ROUTE.QUIZ_SETTING.TITLE} />
          <QuizSettingPage />
        </Suspense>
      </PrivateRoute>
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
          <CardsPage />
        </Suspense>
      </Route>
      <PrivateRoute exact path={ROUTE.PUBLIC_WORKBOOK.PATH}>
        <PageHeader title={ROUTE.PUBLIC_WORKBOOK.TITLE} />
        <PublicWorkbookPage />
      </PrivateRoute>
      <PrivateRoute exact path={ROUTE.PUBLIC_CARDS.PATH}>
        <PublicCardsPage />
      </PrivateRoute>
      <Route exact path={ROUTE.GITHUB_CALLBACK.PATH}>
        <GithubCallbackPage />
      </Route>
    </Switch>
  </BrowserRouter>
);

export default Router;
