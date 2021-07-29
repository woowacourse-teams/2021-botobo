import { useHistory } from 'react-router-dom';

import { ROUTE, STORAGE_KEY } from '../constants';
import { setSessionStorage } from '../utils';

const useRouter = () => {
  const history = useHistory();

  const routeMain = () => history.push(ROUTE.HOME.PATH);
  const routeLogin = () => {
    setSessionStorage(STORAGE_KEY.REDIRECTED_PATH, window.location.pathname);
    history.push(ROUTE.LOGIN.PATH);
  };
  const routeWorkbookAdd = () => history.push(ROUTE.WORKBOOK_ADD.PATH);
  const routeWorkbookEdit = () => history.push(ROUTE.WORKBOOK_EDIT.PATH);
  const routeQuizSetting = () => history.push(ROUTE.QUIZ_SETTING.PATH);
  const routeQuiz = () => history.push(ROUTE.QUIZ.PATH);
  const routeQuizResult = () => history.push(ROUTE.QUIZ_RESULT.PATH);
  const routeCards = () => history.push(ROUTE.CARDS.PATH);
  const routeCardEdit = () => history.push(ROUTE.CARD_EDIT.PATH);
  const routePublicWorkbook = () => history.push(ROUTE.PUBLIC_WORKBOOK.PATH);
  const routePublicCards = (workbookId: number) =>
    history.push(`${ROUTE.PUBLIC_CARDS.PATH}?id=${workbookId}`);
  const routeGithubCallback = () => history.push(ROUTE.GITHUB_CALLBACK.PATH);
  const routePrevPage = () => history.goBack();

  return {
    routeMain,
    routeLogin,
    routeWorkbookAdd,
    routeWorkbookEdit,
    routeQuizSetting,
    routeQuiz,
    routeQuizResult,
    routeCards,
    routeCardEdit,
    routePublicWorkbook,
    routePublicCards,
    routeGithubCallback,
    routePrevPage,
  };
};

export default useRouter;
