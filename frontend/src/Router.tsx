import React, { Suspense, useEffect } from 'react';
import {
  BrowserRouter,
  Redirect,
  Route,
  RouteProps,
  Switch,
  useLocation,
} from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { ROUTE } from './constants';
import {
  CardsLoadable,
  CardsPage,
  GithubCallbackPage,
  LoginPage,
  MainLoadable,
  MainPage,
  PublicCardsLoadable,
  PublicCardsPage,
  PublicWorkbookPage,
  QuizPage,
  QuizResultPage,
  QuizSettingPage,
  WorkbookAddPage,
  WorkbookEditPage,
} from './pages';
import { userState } from './recoil';

const routes = [
  {
    path: ROUTE.HOME.PATH,
    component: (
      <Suspense fallback={<MainLoadable />}>
        <MainPage />
      </Suspense>
    ),
    isPublic: true,
  },
  {
    path: ROUTE.LOGIN.PATH,
    component: <LoginPage />,
    isPublic: true,
  },
  {
    path: ROUTE.WORKBOOK_ADD.PATH,
    component: <WorkbookAddPage />,
    isPublic: false,
  },
  {
    path: ROUTE.WORKBOOK_EDIT.PATH,
    component: <WorkbookEditPage />,
    isPublic: false,
  },
  {
    path: ROUTE.QUIZ_SETTING.PATH,
    component: (
      <Suspense fallback={<div>loading</div>}>
        <QuizSettingPage />
      </Suspense>
    ),
    isPublic: false,
  },
  {
    path: ROUTE.QUIZ.PATH,
    component: <QuizPage />,
    isPublic: true,
  },
  {
    path: ROUTE.QUIZ_RESULT.PATH,
    component: <QuizResultPage />,
    isPublic: true,
  },
  {
    path: ROUTE.CARDS.PATH,
    component: (
      <Suspense fallback={<CardsLoadable />}>
        <CardsPage />
      </Suspense>
    ),
    isPublic: false,
  },
  {
    path: ROUTE.PUBLIC_WORKBOOK.PATH,
    component: <PublicWorkbookPage />,
    isPublic: false,
  },
  {
    path: ROUTE.PUBLIC_CARDS.PATH,
    component: (
      <Suspense fallback={<PublicCardsLoadable />}>
        <PublicCardsPage />
      </Suspense>
    ),
    isPublic: false,
  },
  {
    path: ROUTE.GITHUB_CALLBACK.PATH,
    component: <GithubCallbackPage />,
    isPublic: true,
  },
];

interface PrivateRouteProps extends RouteProps {
  children: React.ReactElement | React.ReactElement[];
}

const ScrollToTop = () => {
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  return null;
};

const PrivateRoute = ({ children, ...props }: PrivateRouteProps) => {
  const userInfo = useRecoilValue(userState);

  return (
    <Route {...props}>{userInfo ? children : <Redirect to="/login" />}</Route>
  );
};

const Router = () => (
  <BrowserRouter>
    <Switch>
      {routes.map(({ path, component, isPublic }, index) =>
        isPublic ? (
          <Route key={index} exact path={path}>
            <ScrollToTop />
            {component}
          </Route>
        ) : (
          <PrivateRoute key={index} exact path={path}>
            <ScrollToTop />
            {component}
          </PrivateRoute>
        )
      )}
    </Switch>
  </BrowserRouter>
);

export default Router;
