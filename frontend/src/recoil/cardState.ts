import { DefaultValue, atom, selector } from 'recoil';

import { CardResponse, CardsResponse } from './../types/index';
import { getCardsAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { getSessionStorage } from '../utils';
import { workbookIdState } from './workbookState';

interface CardState {
  data: CardsResponse;
  errorMessage: string | null;
}

const cardInitialState = {
  id: -1,
  workbookId: -1,
  question: '',
  answer: '',
  bookmark: false,
  encounterCount: -1,
  nextQuiz: false,
};

const cardsInitialState = {
  workbookId: -1,
  workbookName: '',
  cards: [cardInitialState],
};

const cardUpdateTrigger = atom({
  key: 'cardUpdateTrigger',
  default: 0,
});

export const cardIdState = atom({
  key: 'cardIdState',
  default: getSessionStorage(STORAGE_KEY.CARD_ID) ?? -1,
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

export const editedCardState = selector<CardResponse>({
  key: 'editedCardState',
  get: ({ get }) => {
    const cardId = get(cardIdState);
    const { data } = get(cardState);

    return data.cards.find((card) => card.id === cardId) || cardInitialState;
  },
});
