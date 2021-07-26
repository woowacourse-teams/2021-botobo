import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import { cardState } from './../recoil/cardState';
import { postCardAsync } from '../api';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useCards = () => {
  const { routeMain, routeCards } = useRouter();
  const showSnackbar = useSnackbar();
  const {
    data: { workbookId, workbookName, cards },
    errorMessage,
  } = useRecoilValue(cardState);

  const createCard = async (question: string, answer: string) => {
    try {
      await postCardAsync({ workbookId, question, answer });
      showSnackbar({ message: '1장의 카드가 추가되었어요.' });
      routeCards();
    } catch (error) {
      showSnackbar({ message: '카드를 생성하지 못했어요.', type: 'error' });
    }
  };

  useEffect(() => {
    if (workbookId === -1) {
      routeMain();
    }
  }, []);

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { workbookName, cards, createCard };
};

export default useCards;
