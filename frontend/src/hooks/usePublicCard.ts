import React, { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { publicCardState } from '../recoil';
import useSnackbar from './useSnackbar';

const usePublicCard = () => {
  const {
    data: { workbookName, cards, cardCount, tags },
    errorMessage,
  } = useRecoilValue(publicCardState);

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

  useEffect(() => {
    setIsAllCardChecked(checkedCardCount === cardCount);
  }, [checkedCardCount]);

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return {
    workbookName,
    cards,
    cardCount,
    tags,
    publicCards,
    isAllCardChecked,
    checkedCardCount,
    checkCard,
    checkAllCard,
  };
};

export default usePublicCard;
