import { DefaultValue, atom, selector } from 'recoil';

import { CardsResponse } from './../types/index';
import { getCardsAsync } from '../api';
import { workbookIdState } from './workbookState';

interface CardState {
  data: CardsResponse;
  errorMessage: string | null;
}

const cardInitialState = { workbookId: -1, workbookName: '', cards: [] };

const cardUpdateTrigger = atom({
  key: 'cardUpdateTrigger',
  default: 0,
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
            ? cardInitialState
            : await getCardsAsync(workbookId),
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: cardInitialState,
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
