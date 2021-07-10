import { ThemeProvider } from '@emotion/react';
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
  </ThemeProvider>
);

export default App;
