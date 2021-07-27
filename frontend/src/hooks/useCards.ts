import { useEffect } from 'react';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import { putCardAsync } from './../api/index';
import { cardState } from './../recoil/cardState';
import { CardResponse } from './../types/index';
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
  const updateCardInfo = useResetRecoilState(cardState);

  const createCard = async (question: string, answer: string) => {
    try {
      await postCardAsync({ workbookId, question, answer });
      updateCardInfo();
      showSnackbar({ message: '1장의 카드가 추가되었어요.' });
      routeCards();
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 생성하지 못했어요.', type: 'error' });
    }
  };

  const editCard = async (cardInfo: CardResponse) => {
    try {
      await putCardAsync(cardInfo);
      updateCardInfo();
      showSnackbar({ message: '1장의 카드가 수정되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 수정하지 못했어요.', type: 'error' });
    }
  };

  const toggleBookmark = async (cardInfo: CardResponse) => {
    try {
      await putCardAsync(cardInfo);
    } catch (error) {
      console.error(error);
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

  return {
    workbookName,
    cards,
    createCard,
    editCard,
    toggleBookmark,
    updateCardInfo,
  };
};

export default useCards;
