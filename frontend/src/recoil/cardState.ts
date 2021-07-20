import { atomFamily, selectorFamily } from 'recoil';

import { getCardsAsync } from '../api';
import { CardsResponse } from '../types';

interface CardState {
  data: CardsResponse;
  errorMessage: string | null;
}

const cardInitialState = { categoryName: '', cards: [] };

export const cardState = atomFamily<CardState, number>({
  key: 'cardState',
  default: selectorFamily({
    key: 'categoryRequest',
    get: (categoryId) => async () => {
      try {
        return { data: await getCardsAsync(categoryId), errorMessage: null };
      } catch (error) {
        return {
          data: cardInitialState,
          errorMessage: '카드를 불러오지 못했습니다.',
        };
      }
    },
  }),
});
