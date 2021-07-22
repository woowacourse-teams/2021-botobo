import { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { cardState } from './../recoil/cardState';
import { ROUTE } from '../constants';
import useSnackbar from './useSnackbar';

const useCards = () => {
  const history = useHistory();
  const { search } = useLocation();
  const workbookId = new URLSearchParams(search).get('workbookId');
  const showSnackbar = useSnackbar();

  if (!workbookId) {
    history.push(ROUTE.HOME.PATH);
  }

  const {
    data: { workbookName, cards },
    errorMessage,
  } = useRecoilValue(cardState(Number(workbookId)));

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { workbookName, cards };
};

export default useCards;
