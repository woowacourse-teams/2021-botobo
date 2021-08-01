import React, { useEffect, useState } from 'react';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import { postPublicCardsAsync } from './../api/index';
import { workbookState } from './../recoil/workbookState';
import { publicCardState } from '../recoil';
import useSnackbar from './useSnackbar';

const usePublicCard = () => {
  const {
    data: { workbookId, workbookName, cards, cardCount, tags },
    errorMessage,
  } = useRecoilValue(publicCardState);
  const updateWorkbooks = useResetRecoilState(workbookState);

  const [publicCards, setPublicCards] = useState(
    cards.map((card) => ({ ...card, isChecked: false }))
  );
  const [isAllCardChecked, setIsAllCardChecked] = useState(false);

  const showSnackbar = useSnackbar();

  const checkedCardCount = publicCards.filter(
    ({ isChecked }) => isChecked
  ).length;

  const checkCard = (id: number) => {
    const newCards = publicCards.map((card) => {
      if (card.id !== id) return card;

      return {
        ...card,
        isChecked: !card.isChecked,
      };
    });

    setPublicCards(newCards);
  };

  const checkAllCard: React.ChangeEventHandler<HTMLInputElement> = ({
    target,
  }) => {
    setIsAllCardChecked(target.checked);
    setPublicCards(
      publicCards.map((card) => ({ ...card, isChecked: target.checked }))
    );
  };

  const takeCardsToMyWorkbook = async (workbookId: number) => {
    const cardIds = publicCards
      .filter(({ isChecked }) => Boolean(isChecked))
      .map(({ id }) => id);

    try {
      await postPublicCardsAsync(workbookId, cardIds);
      updateWorkbooks();
      setPublicCards(
        publicCards.map((card) => ({ ...card, isChecked: false }))
      );
      showSnackbar({ message: '내 문제집에 추가 되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '카드를 추가하지 못했어요.', type: 'error' });
    }
  };

  useEffect(() => {
    setIsAllCardChecked(checkedCardCount === cardCount);
  }, [checkedCardCount]);

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return {
    workbookId,
    workbookName,
    cards,
    cardCount,
    tags,
    publicCards,
    isAllCardChecked,
    checkedCardCount,
    checkCard,
    checkAllCard,
    takeCardsToMyWorkbook,
  };
};

export default usePublicCard;
