import React, { lazy, useEffect } from 'react';
import {
  Redirect,
  Route,
  RouteProps,
  Switch,
  useLocation,
} from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { SsrSuspense } from './components';
import { ROUTE } from './constants';
import {
  MainLoadable,
  MainPage,
  PublicSearchLoadable,
  QuizSettingLoadable,
} from './pages';
import { userState } from './recoil';

const ProfilePage = lazy(() => import('./pages/ProfilePage'));
const LoginPage = lazy(() => import('./pages/LoginPage'));
const LogoutPage = lazy(() => import('./pages/LogoutPage'));
const WorkbookAddPage = lazy(() => import('./pages/WorkbookAddPage'));
const WorkbookEditPage = lazy(() => import('./pages/WorkbookEditPage'));
const QuizSettingPage = lazy(() => import('./pages/QuizSettingPage'));
const QuizPage = lazy(() => import('./pages/QuizPage'));
const QuizResultPage = lazy(() => import('./pages/QuizResultPage'));
const CardsPage = lazy(() => import('./pages/CardsPage'));
const PublicSearchPage = lazy(() => import('./pages/PublicSearchPage'));
const PublicSearchResultPage = lazy(
  () => import('./pages/PublicSearchResultPage')
);
const PublicCardsPage = lazy(() => import('./pages/PublicCardsPage'));
const OAuthCallbackPage = lazy(() => import('./pages/OAuthCallbackPage'));
const Guide = lazy(() => import('./pages/Guide'));

const routes = [
  {
    path: ROUTE.HOME.PATH,
    component: (
      <SsrSuspense fallback={<MainLoadable />}>
        <MainPage />
      </SsrSuspense>
    ),
    isPublic: true,
  },
  {
    path: ROUTE.PROFILE.PATH,
    component: <ProfilePage />,
    isPublic: false,
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
      <SsrSuspense fallback={<QuizSettingLoadable />}>
        <QuizSettingPage />
      </SsrSuspense>
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
    path: ROUTE.PUBLIC_SEARCH.PATH,
    component: (
      <SsrSuspense fallback={<PublicSearchLoadable />}>
        <PublicSearchPage />
      </SsrSuspense>
    ),
    isPublic: true,
  },
  {
    path: ROUTE.PUBLIC_SEARCH_RESULT.PATH,
    component: <PublicSearchResultPage />,
    isPublic: true,
  },
  {
    path: `${ROUTE.PUBLIC_CARDS.PATH}/:id`,
    component: <PublicCardsPage />,
    isPublic: true,
  },
  {
    path: `(${ROUTE.GITHUB_CALLBACK.PATH}|${ROUTE.GOOGLE_CALLBACK.PATH})`,
    component: <OAuthCallbackPage />,
    isPublic: true,
  },
  {
    path: ROUTE.GUIDE.PATH,
    component: <Guide />,
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
);

export default Router;
