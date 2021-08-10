import React, { useEffect, useState } from 'react';
import { useSetRecoilState } from 'recoil';

import {
  getPublicCardsAsync,
  postPublicCardsAsync,
  putHeartAsync,
} from './../api';
import { shouldWorkbookUpdateState } from './../recoil';
import { PublicCardsResponse } from './../types';
import { CardResponse } from './../types/index';
import { getSessionStorage } from './../utils/storage';
import { STORAGE_KEY } from '../constants';
import useRouter from './useRouter';
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
  const publicWorkbookId =
    getSessionStorage(STORAGE_KEY.PUBLIC_WORKBOOK_ID) ?? -1;
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

  const [isLoading, setIsLoading] = useState(false);
  const showSnackbar = useSnackbar();
  const { routeMain } = useRouter();

  const getPublicCards = async () => {
    try {
      setIsLoading(true);
      const newPublicCardInfo = await getPublicCardsAsync(publicWorkbookId);

      setPublicCardInfo({
        ...newPublicCardInfo,
        cards: newPublicCardInfo.cards.map((card) => ({
          ...card,
          isChecked: false,
        })),
      });

      setIsLoading(false);
    } catch (error) {
      showSnackbar({ message: '카드를 불러오지 못했어요.', type: 'error' });
      setIsLoading(false);
    }
  };

  const toggleHeart = async () => {
    try {
      const isHeart = await putHeartAsync(workbookId);

      setHeartInfo((prevValue) => ({ ...prevValue, serverHeart: isHeart }));
    } catch (error) {
      console.error(error);
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
      console.error(error);
      showSnackbar({ message: '카드를 추가하지 못했어요.', type: 'error' });
    }
  };

  useEffect(() => {
    setIsAllCardChecked(checkedCardCount === cardCount);
  }, [checkedCardCount]);

  useEffect(() => {
    if (publicWorkbookId === -1) {
      routeMain();

      return;
    }

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
  };
};

export default usePublicCard;
