import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';
import { useRecoilState, useSetRecoilState } from 'recoil';

import {
  getPublicCardsAsync,
  postPublicCardsAsync,
  putHeartAsync,
} from './../api';
import {
  publicSearchResultState,
  publicWorkbookState,
  shouldWorkbookUpdateState,
} from './../recoil';
import { PublicCardsResponse } from './../types';
import { CardResponse } from './../types/index';
import { publicCardsInitialState } from '../recoil/initialState';
import { IdParam } from '../types/idParam';
import useErrorHandler from './useErrorHandler';
import useSnackbar from './useSnackbar';

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
  const [publicWorkbook, setPublicWorkbook] =
    useRecoilState(publicWorkbookState);
  const setPublicSearchResult = useSetRecoilState(publicSearchResultState);

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

      setPublicWorkbook({
        ...publicWorkbook,
        data: publicWorkbook.data.map((prevData) => {
          if (prevData.id !== workbookId) return prevData;

          return {
            ...prevData,
            heartCount: heart
              ? prevData.heartCount + 1
              : prevData.heartCount - 1,
          };
        }),
      });

      setPublicSearchResult((prevValue) => ({
        ...prevValue,
        publicWorkbookResult: prevValue.publicWorkbookResult.map((prevData) => {
          if (prevData.id !== workbookId) return prevData;

          return {
            ...prevData,
            heartCount: heart
              ? prevData.heartCount + 1
              : prevData.heartCount - 1,
          };
        }),
      }));

      setShouldWorkbookUpdateState(true);
    } catch (error) {
      errorHandler(error, toggleHeart);
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
      errorHandler(error, takeCardsToMyWorkbook.bind(null, workbookId));
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
