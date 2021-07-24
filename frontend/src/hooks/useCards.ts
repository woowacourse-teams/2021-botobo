import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { cardState } from './../recoil/cardState';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useCards = () => {
  const { routeMain } = useRouter();
  const { search } = useLocation();
  const workbookId = new URLSearchParams(search).get('id');
  const showSnackbar = useSnackbar();

  if (!workbookId) {
    routeMain();
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
