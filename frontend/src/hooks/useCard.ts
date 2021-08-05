import { useEffect, useState } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { shouldWorkbookUpdateState } from './../recoil/workbookState';
import {
  deleteCardAsync,
  getCardsAsync,
  postCardAsync,
  putCardAsync,
} from '../api';
import { workbookIdState } from '../recoil';
import { CardResponse, CardsResponse } from '../types';
import useModal from './useModal';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const cardsInitialState = {
  workbookId: -1,
  workbookName: '',
  cards: [],
};

const useCard = () => {
  const workbookId = useRecoilValue(workbookIdState);
  const setShouldWorkbookUpdateState = useSetRecoilState(
    shouldWorkbookUpdateState
  );

  const [cardInfo, setCardInfo] = useState<CardsResponse>(cardsInitialState);
  const { workbookName, cards } = cardInfo;

  const [isLoading, setIsLoading] = useState(false);
  const showSnackbar = useSnackbar();
  const { routeMain } = useRouter();
  const { openModal, closeModal } = useModal();

  const getCards = async () => {
    try {
      setIsLoading(true);
      const newCardInfo = await getCardsAsync(workbookId);
      setCardInfo(newCardInfo);
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
      setShouldWorkbookUpdateState(true);
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

      setShouldWorkbookUpdateState(true);
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
    if (workbookId === -1) {
      routeMain();

      return;
    }

    getCards();
  }, []);

  return {
    workbookId,
    workbookName,
    cards,
    getCards,
    createCard,
    editCard,
    deleteCard,
    toggleBookmark,
    openModal,
    isLoading,
  };
};

export default useCard;
