import React, { Suspense } from 'react';
import {
  BrowserRouter,
  Redirect,
  Route,
  RouteProps,
  Switch,
} from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { MainHeader, PageHeader } from './components';
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
} from './pages';
import { userState } from './recoil';

interface PrivateRouteProps extends RouteProps {
  children: React.ReactElement;
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
          <MainHeader />
          <MainPage />
        </Suspense>
      </Route>
      <Route exact path={ROUTE.LOGIN.PATH}>
        <PageHeader title={ROUTE.LOGIN.TITLE} />
        <LoginPage />
      </Route>
      <PrivateRoute exact path={ROUTE.WORKBOOK_ADD.PATH}>
        <>
          <PageHeader title={ROUTE.WORKBOOK_ADD.TITLE} />
          <WorkbookAddPage />
        </>
      </PrivateRoute>
      <PrivateRoute exact path={ROUTE.QUIZ_SETTING.PATH}>
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
      <Route exact path={ROUTE.PUBLIC_WORKBOOK.PATH}>
        <PageHeader title={ROUTE.PUBLIC_WORKBOOK.TITLE} />
        <PublicWorkbookPage />
      </Route>
      <Route exact path={ROUTE.PUBLIC_CARDS.PATH}>
        <PublicCardsPage />
      </Route>
      <Route exact path={ROUTE.GITHUB_CALLBACK.PATH}>
        <GithubCallbackPage />
      </Route>
    </Switch>
  </BrowserRouter>
);

export default Router;
