import { useHistory } from 'react-router-dom';

import { ROUTE, SEARCH_CRITERIA, STORAGE_KEY } from '../constants';
import { ValueOf } from '../types/utils';
import { setSessionStorage } from '../utils';

interface PublicSearchResultQuery {
  criteria?: ValueOf<typeof SEARCH_CRITERIA>;
  tags?: string | null;
  users?: string | null;
  keyword?: string;
  size?: number;
  method?: 'push' | 'replace';
}

const useRouter = () => {
  const history = useHistory();

  const routeLogin = () => {
    setSessionStorage(STORAGE_KEY.REDIRECTED_PATH, window.location.pathname);
    history.push(ROUTE.LOGIN.PATH);
  };
  const routeMain = () => history.push(ROUTE.HOME.PATH);
  const routeProfile = () => history.push(ROUTE.PROFILE.PATH);
  const routeWorkbookAdd = () => history.push(ROUTE.WORKBOOK_ADD.PATH);
  const routeWorkbookEdit = (id: number) =>
    history.push(`${ROUTE.WORKBOOK_EDIT.PATH}/${id}`);
  const routeQuizSetting = () => history.push(ROUTE.QUIZ_SETTING.PATH);
  const routeQuiz = () => history.push(ROUTE.QUIZ.PATH);
  const routeQuizResult = () => history.push(ROUTE.QUIZ_RESULT.PATH);
  const routePublicSearch = () => history.push(ROUTE.PUBLIC_SEARCH.PATH);
  const routePublicSearchResult = () =>
    history.push(ROUTE.PUBLIC_SEARCH_RESULT.PATH);
  const routeOAuthCallback = () => history.push(ROUTE.GITHUB_CALLBACK.PATH);
  const routeGuide = () => history.push(ROUTE.GUIDE.PATH);

  const routePublicSearchResultQuery = ({
    tags,
    users,
    criteria = 'date',
    keyword = '',
    size = 20,
    method = 'replace',
  }: PublicSearchResultQuery = {}) =>
    history[method]({
      pathname: ROUTE.PUBLIC_SEARCH_RESULT.PATH,
      search: `?keyword=${keyword}&criteria=${criteria}&size=${size}${
        tags ? `&tags=${tags}` : ''
      }${users ? `&users=${users}` : ''}`,
    });

  const routePrevPage = () => history.goBack();

  return {
    routeMain,
    routeLogin,
    routeProfile,
    routeWorkbookAdd,
    routeWorkbookEdit,
    routeQuizSetting,
    routeQuiz,
    routeQuizResult,
    routePublicSearch,
    routePublicSearchResult,
    routeOAuthCallback,
    routePublicSearchResultQuery,
    routePrevPage,
    routeGuide,
  };
};

export default useRouter;
