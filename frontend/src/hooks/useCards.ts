import { useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { cardState } from './../recoil/cardState';
import { ROUTE } from '../constants';
import useSnackbar from './useSnackbar';

const useCards = () => {
  const history = useHistory();
  const { search } = useLocation();
  const categoryId = new URLSearchParams(search).get('categoryId');
  const showSnackbar = useSnackbar();

  if (!categoryId) {
    history.push(ROUTE.HOME.PATH);
  }

  const {
    data: { categoryName, cards },
    errorMessage,
  } = useRecoilValue(cardState(Number(categoryId)));

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { categoryName, cards };
};

export default useCards;
