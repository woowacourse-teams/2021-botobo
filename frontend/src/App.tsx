import { ThemeProvider } from '@emotion/react';
import styled from '@emotion/styled';
import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import { Header } from './components';
import { ROUTE, theme } from './constants';
import GlobalStyle from './GlobalStyle';
import { MainPage, QuizPage, QuizResultPage, QuizSettingPage } from './pages';

const App = () => (
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <Header />
    <PageWrapper>
      <BrowserRouter>
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
        </Switch>
      </BrowserRouter>
    </PageWrapper>
  </ThemeProvider>
);

const PageWrapper = styled.div`
  padding: 2rem 1.25rem;
  height: calc(100% - 3.75rem);
`;

export default App;
