import { useHistory, useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { cardState } from './../recoil/cardState';
import { ROUTE } from '../constants';

const useCards = () => {
  const history = useHistory();
  const { search } = useLocation();
  const categoryId = new URLSearchParams(search).get('categoryId');

  if (!categoryId) {
    history.push(ROUTE.HOME);
  } //TODO: 에러 바운더리에서 처리 (메시지)

  const { categoryName, cards } = useRecoilValue(cardState(Number(categoryId)));

  return { categoryName, cards };
};

export default useCards;
