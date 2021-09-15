import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useSetRecoilState } from 'recoil';

import {
  getPublicCardsAsync,
  postPublicCardsAsync,
  putHeartAsync,
} from './../api';
import { shouldWorkbookUpdateState } from './../recoil';
import { PublicCardsResponse } from './../types';
import { CardResponse } from './../types/index';
import { IdParam } from '../types/idParam';
import useErrorHandler from './useErrorHandler';
import useSnackbar from './useSnackbar';

const cardsInitialState = {
  workbookId: -1,
  workbookName: '',
  cards: [],
};

const publicCardsInitialState = {
  ...cardsInitialState,
  heart: false,
  heartCount: 0,
  cardCount: -1,
  tags: [],
};

interface PublicCard extends CardResponse {
  isChecked: boolean;
}

interface PublicCardsInfo extends PublicCardsResponse {
  cards: PublicCard[];
}

const usePublicCard = () => {
  const param: IdParam = useParams();
  const publicWorkbookId = Number(param.id);

  const setShouldWorkbookUpdateState = useSetRecoilState(
    shouldWorkbookUpdateState
  );

  const [publicCardInfo, setPublicCardInfo] = useState<PublicCardsInfo>(
    publicCardsInitialState
  );
  const {
    workbookId,
    workbookName,
    cards,
    cardCount,
    tags,
    heart,
    heartCount,
  } = publicCardInfo;

  const [heartInfo, setHeartInfo] = useState({
    heart,
    heartCount,
    serverHeart: heart,
  });

  const [isAllCardChecked, setIsAllCardChecked] = useState(false);
  const checkedCardCount = cards.filter(({ isChecked }) => isChecked).length;

  const [isLoading, setIsLoading] = useState(true);
  const showSnackbar = useSnackbar();
  const errorHandler = useErrorHandler();

  const getPublicCards = async () => {
    try {
      const newPublicCardInfo = await getPublicCardsAsync(publicWorkbookId);
      const { heart, heartCount } = newPublicCardInfo;

      setPublicCardInfo({
        ...newPublicCardInfo,
        cards: newPublicCardInfo.cards.map((card) => ({
          ...card,
          isChecked: false,
        })),
      });

      setHeartInfo({ heart, heartCount, serverHeart: heart });
      setIsLoading(false);
    } catch (error) {
      errorHandler(error);
      setIsLoading(false);
    }
  };

  const toggleHeart = async () => {
    try {
      const { heart } = await putHeartAsync(workbookId);

      setHeartInfo((prevValue) => ({ ...prevValue, serverHeart: heart }));
    } catch (error) {
      errorHandler(error);
    }
  };

  const checkCard = (id: number) => {
    const newCards = cards.map((card) => {
      if (card.id !== id) return card;

      return {
        ...card,
        isChecked: !card.isChecked,
      };
    });

    setPublicCardInfo({ ...publicCardInfo, cards: newCards });
  };

  const checkAllCard: React.ChangeEventHandler<HTMLInputElement> = ({
    target,
  }) => {
    setIsAllCardChecked(target.checked);
    setPublicCardInfo({
      ...publicCardInfo,
      cards: cards.map((card) => ({ ...card, isChecked: target.checked })),
    });
  };

  const takeCardsToMyWorkbook = async (workbookId: number) => {
    const cardIds = cards
      .filter(({ isChecked }) => Boolean(isChecked))
      .map(({ id }) => id);

    try {
      await postPublicCardsAsync(workbookId, cardIds);

      setPublicCardInfo({
        ...publicCardInfo,
        cards: cards.map((card) => ({ ...card, isChecked: false })),
      });

      setShouldWorkbookUpdateState(true);
      showSnackbar({ message: '내 문제집에 추가 되었어요.' });
    } catch (error) {
      errorHandler(error);
    }
  };

  useEffect(() => {
    setIsAllCardChecked(checkedCardCount === cardCount);
  }, [checkedCardCount]);

  useEffect(() => {
    getPublicCards();
  }, []);

  return {
    workbookId,
    workbookName,
    cards,
    cardCount,
    tags,
    heartInfo,
    setHeartInfo,
    isAllCardChecked,
    checkedCardCount,
    checkCard,
    checkAllCard,
    takeCardsToMyWorkbook,
    isLoading,
    toggleHeart,
    showSnackbar,
  };
};

export default usePublicCard;
