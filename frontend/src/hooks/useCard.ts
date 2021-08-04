import { useEffect, useState } from 'react';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import {
  deleteCardAsync,
  getCardsAsync,
  postCardAsync,
  putCardAsync,
} from '../api';
import { workbookIdState, workbookState } from '../recoil';
import { CardResponse, CardsResponse } from '../types';
import useModal from './useModal';
import useSnackbar from './useSnackbar';

const cardsInitialState = {
  workbookId: -1,
  workbookName: '',
  cards: [],
};

const useCard = () => {
  const workbookId = useRecoilValue(workbookIdState);
  const updateWorkbooks = useResetRecoilState(workbookState);

  const [cardInfo, setCardInfo] = useState<CardsResponse>(cardsInitialState);
  const [isLoading, setIsLoading] = useState(false);
  const { workbookName, cards } = cardInfo;

  const showSnackbar = useSnackbar();
  const { openModal, closeModal } = useModal();

  const getCard = async () => {
    try {
      setIsLoading(true);
      const data = await getCardsAsync(workbookId);
      setCardInfo(data);
      setIsLoading(false);
    } catch (error) {
      showSnackbar({ message: '카드를 불러오지 못했어요.', type: 'error' });
      setIsLoading(false);
    }
  };

  const createCard = async (question: string, answer: string) => {
    try {
      const newCard = await postCardAsync({ workbookId, question, answer });
      setCardInfo({ ...cardInfo, cards: [newCard, ...cardInfo.cards] });
      closeModal();
      updateWorkbooks();
      showSnackbar({ message: '1장의 카드가 추가되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 추가하지 못했어요.', type: 'error' });
    }
  };

  const editCard = async (info: CardResponse) => {
    try {
      const editedCard = await putCardAsync(info);
      setCardInfo({
        ...cardInfo,
        cards: cards.map((card) => {
          if (card.id !== info.id) return card;

          return editedCard;
        }),
      });
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
      setCardInfo({
        ...cardInfo,
        cards: cards.filter((card) => card.id !== id),
      });
      updateWorkbooks();
      showSnackbar({ message: '1장의 카드가 삭제되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 삭제하지 못했어요.', type: 'error' });
    }
  };

  const toggleBookmark = async (info: CardResponse) => {
    try {
      await putCardAsync(info);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getCard();
  }, []);

  return {
    workbookId,
    workbookName,
    cards,
    getCard,
    createCard,
    editCard,
    deleteCard,
    toggleBookmark,
    openModal,
    isLoading,
  };
};

export default useCard;
