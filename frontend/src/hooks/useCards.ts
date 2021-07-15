import { useLocation } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { cardState } from './../recoil/cardState';

const useCards = () => {
  const { search } = useLocation();
  const categoryId = new URLSearchParams(search).get('categoryId');

  if (!categoryId) throw new Error('잘못된 접근입니다.'); //TODO: 에러 바운더리에서 처리 (메시지)

  const { categoryName, cards } = useRecoilValue(cardState(Number(categoryId)));

  return { categoryName, cards };
};

export default useCards;
