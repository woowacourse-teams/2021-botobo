import { DefaultValue, atom, selector } from 'recoil';

import {
  CardResponse,
  CardsResponse,
  PublicCardsResponse,
} from './../types/index';
import { getCardsAsync, getPublicCardsAsync } from '../api';
import {
  cardInitialState,
  cardsInitialState,
  publicCardsInitialState,
} from './initialState';
import { publicWorkbookIdState, workbookIdState } from './workbookState';

interface CardState {
  data: CardsResponse;
  errorMessage: string | null;
}

interface PublicCardState {
  data: PublicCardsResponse;
  errorMessage: string | null;
}

const cardUpdateTrigger = atom({
  key: 'cardUpdateTrigger',
  default: 0,
});

export const cardIdState = atom({
  key: 'cardIdState',
  default: -1,
});

export const cardState = selector<CardState>({
  key: 'cardState',
  get: async ({ get }) => {
    try {
      get(cardUpdateTrigger);
      const workbookId = get(workbookIdState);

      return {
        data:
          workbookId === -1
            ? cardsInitialState
            : await getCardsAsync(workbookId),
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: cardsInitialState,
        errorMessage: '카드를 불러오지 못했습니다.',
      };
    }
  },
  set: ({ set }, value) => {
    if (value instanceof DefaultValue) {
      set(cardUpdateTrigger, (prevState) => prevState + 1);
    }
  },
});

export const publicCardState = selector<PublicCardState>({
  key: 'publicCardState',
  get: async ({ get }) => {
    try {
      const workbookId = get(publicWorkbookIdState);

      return {
        data:
          workbookId === -1
            ? publicCardsInitialState
            : await getPublicCardsAsync(workbookId),
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: publicCardsInitialState,
        errorMessage: '카드를 불러오지 못했습니다.',
      };
    }
  },
});

export const editedCardState = selector<CardResponse>({
  key: 'editedCardState',
  get: ({ get }) => {
    const cardId = get(cardIdState);
    const { data } = get(cardState);

    return data.cards.find((card) => card.id === cardId) || cardInitialState;
  },
});
