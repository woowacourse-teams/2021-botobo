import React, { Suspense, useEffect } from 'react';
import ReactGA from 'react-ga';
import {
  BrowserRouter,
  Redirect,
  Route,
  RouteProps,
  Switch,
  useLocation,
} from 'react-router-dom';
import { withRouter } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { ROUTE } from './constants';
import {
  CardsPage,
  GithubCallbackPage,
  LoginPage,
  LogoutPage,
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
    path: ROUTE.LOGOUT.PATH,
    component: <LogoutPage />,
    isPublic: false,
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
    component: <CardsPage />,
    isPublic: false,
  },
  {
    path: ROUTE.PUBLIC_WORKBOOK.PATH,
    component: <PublicWorkbookPage />,
    isPublic: false,
  },
  {
    path: ROUTE.PUBLIC_CARDS.PATH,
    component: <PublicCardsPage />,
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

const RouteChangeTracker = withRouter(({ history }) => {
  history.listen((location) => {
    ReactGA.set({ page: location.pathname + location.search });
    ReactGA.pageview(location.pathname + location.search);
  });

  return null;
});

const Router = () => (
  <BrowserRouter>
    <Switch>
      {routes.map(({ path, component, isPublic }, index) =>
        isPublic ? (
          <Route key={index} exact path={path}>
            <ScrollToTop />
            <RouteChangeTracker />
            {component}
          </Route>
        ) : (
          <PrivateRoute key={index} exact path={path}>
            <ScrollToTop />
            <RouteChangeTracker />
            {component}
          </PrivateRoute>
        )
      )}
    </Switch>
  </BrowserRouter>
);

export default Router;
