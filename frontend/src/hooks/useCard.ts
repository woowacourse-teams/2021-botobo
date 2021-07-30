import { useEffect } from 'react';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import { deleteCardAsync, postCardAsync, putCardAsync } from '../api';
import { cardState } from '../recoil';
import { CardResponse } from '../types';
import useModal from './useModal';
import useSnackbar from './useSnackbar';

const useCard = () => {
  const {
    data: { workbookId, workbookName, cards },
    errorMessage,
  } = useRecoilValue(cardState);
  const updateCardInfo = useResetRecoilState(cardState);

  const showSnackbar = useSnackbar();
  const { openModal, closeModal } = useModal();

  const createCard = async (question: string, answer: string) => {
    try {
      await postCardAsync({ workbookId, question, answer });
      updateCardInfo();
      closeModal();
      showSnackbar({ message: '1장의 카드가 추가되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 추가하지 못했어요.', type: 'error' });
    }
  };

  const editCard = async (cardInfo: CardResponse) => {
    try {
      await putCardAsync(cardInfo);
      updateCardInfo();
      closeModal();
      showSnackbar({ message: '1장의 카드가 수정되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 수정하지 못했어요.', type: 'error' });
    }
  };

  const deleteCard = async (id: number) => {
    try {
      await deleteCardAsync(id);
      updateCardInfo();
      showSnackbar({ message: '1장의 카드가 삭제되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 삭제하지 못했어요.', type: 'error' });
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
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return {
    workbookId,
    workbookName,
    cards,
    createCard,
    editCard,
    deleteCard,
    toggleBookmark,
    updateCardInfo,
    openModal,
  };
};

export default useCard;
