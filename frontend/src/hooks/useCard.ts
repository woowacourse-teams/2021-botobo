import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { shouldWorkbookUpdateState } from './../recoil/workbookState';
import {
  deleteCardAsync,
  getCardsAsync,
  postCardAsync,
  putCardAsync,
} from '../api';
import { cardsInitialState } from '../recoil/initialState';
import { CardResponse, CardsResponse } from '../types';
import useErrorHandler from './useErrorHandler';
import useModal from './useModal';
import useSnackbar from './useSnackbar';
import { IdParam } from '../types/idParam';

const useCard = () => {
  const param = useParams<IdParam>();
  const workbookId = Number(param.id);

  const setShouldWorkbookUpdateState = useSetRecoilState(
    shouldWorkbookUpdateState
  );

  const [cardInfo, setCardInfo] = useState<CardsResponse>(cardsInitialState);
  const { workbookName, workbookOpened, cards, heartCount, tags } = cardInfo;

  const deletedCardId = useRef(-1);

  const [isLoading, setIsLoading] = useState(true);
  const showSnackbar = useSnackbar();
  const { openModal, closeModal } = useModal();
  const errorHandler = useErrorHandler();

  const getCards = async () => {
    try {
      const newCardInfo = await getCardsAsync(workbookId);
      setCardInfo(newCardInfo);
      setIsLoading(false);
    } catch (error) {
      errorHandler(error, getCards);
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
      errorHandler(error, createCard.bind(null, question, answer));
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
      errorHandler(error, editCard.bind(null, info));
    }
  };

  const deleteCard = async (id: number) => {
    if (deletedCardId.current === id) return;

    try {
      await deleteCardAsync(id);
      deletedCardId.current = id;
      setCardInfo({
        ...cardInfo,
        cards: cards.filter((card) => card.id !== id),
      });

      setShouldWorkbookUpdateState(true);
      showSnackbar({ message: '1장의 카드가 삭제되었어요.' });
    } catch (error) {
      errorHandler(error, deleteCard.bind(null, id));
    }
  };

  const toggleBookmark = async (info: CardResponse) => {
    try {
      await putCardAsync(info);
    } catch (error) {
      await errorHandler(error, toggleBookmark.bind(null, info));
    }
  };

  useEffect(() => {
    getCards();
  }, []);

  return {
    workbookId,
    workbookName,
    workbookOpened,
    cards,
    heartCount,
    tags,
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
