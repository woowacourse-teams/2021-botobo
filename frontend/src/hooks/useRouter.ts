import { useHistory } from 'react-router-dom';

import {
  ROUTE,
  SEARCH_CRITERIA,
  SEARCH_ORDER,
  SEARCH_TYPE,
  STORAGE_KEY,
} from '../constants';
import { ValueOf } from '../types/utils';
import { setSessionStorage } from '../utils';

interface PublicSearchQuery {
  criteria?: ValueOf<typeof SEARCH_CRITERIA>;
  type?: ValueOf<typeof SEARCH_TYPE>;
  order?: ValueOf<typeof SEARCH_ORDER>;
  keyword?: string;
  size?: number;
  start?: number;
}

const useRouter = () => {
  const history = useHistory();

  const routeLogin = () => {
    setSessionStorage(STORAGE_KEY.REDIRECTED_PATH, window.location.pathname);
    history.push(ROUTE.LOGIN.PATH);
  };
  const routeLogout = () => history.push(ROUTE.LOGOUT.PATH);
  const routeMain = () => history.push(ROUTE.HOME.PATH);
  const routeProfile = () => history.push(ROUTE.PROFILE.PATH);
  const routeWorkbookAdd = () => history.push(ROUTE.WORKBOOK_ADD.PATH);
  const routeWorkbookEdit = () => history.push(ROUTE.WORKBOOK_EDIT.PATH);
  const routeQuizSetting = () => history.push(ROUTE.QUIZ_SETTING.PATH);
  const routeQuiz = () => history.push(ROUTE.QUIZ.PATH);
  const routeQuizResult = () => history.push(ROUTE.QUIZ_RESULT.PATH);
  const routeCards = () => history.push(ROUTE.CARDS.PATH);
  const routePublicSearch = () => history.push(ROUTE.PUBLIC_SEARCH.PATH);
  const routePublicSearchResult = () =>
    history.push(ROUTE.PUBLIC_SEARCH_RESULT.PATH);
  const routePublicCards = () => history.push(ROUTE.PUBLIC_CARDS.PATH);
  const routeGithubCallback = () => history.push(ROUTE.GITHUB_CALLBACK.PATH);

  const routePublicSearchQuery = ({
    type = 'name',
    keyword = '',
    size = 20,
  }: PublicSearchQuery = {}) =>
    history.replace({
      pathname: ROUTE.PUBLIC_SEARCH.PATH,
      search: `?type=${type}&keyword=${keyword}&size=${size}`,
    });

  const routePublicSearchResultQuery = ({
    criteria = 'date',
    type = 'name',
    keyword = '',
    size = 20,
  }: PublicSearchQuery = {}) =>
    history.replace({
      pathname: ROUTE.PUBLIC_SEARCH_RESULT.PATH,
      search: `?type=${type}&keyword=${keyword}&criteria=${criteria}&size=${size}`,
    });

  const routePrevPage = () => history.goBack();

  return {
    routeMain,
    routeLogin,
    routeLogout,
    routeProfile,
    routeWorkbookAdd,
    routeWorkbookEdit,
    routeQuizSetting,
    routeQuiz,
    routeQuizResult,
    routeCards,
    routePublicSearch,
    routePublicSearchResult,
    routePublicCards,
    routeGithubCallback,
    routePublicSearchQuery,
    routePublicSearchResultQuery,
    routePrevPage,
  };
};

export default useRouter;
