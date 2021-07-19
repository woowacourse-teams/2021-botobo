import { atomFamily, selectorFamily } from 'recoil';

import { getCardsAsync } from '../api';
import { CardsResponse } from '../types';

export const cardState = atomFamily<CardsResponse, number>({
  key: 'cardState',
  default: selectorFamily({
    key: 'categoryRequest',
    get: (categoryId) => () => getCardsAsync(categoryId),
  }),
});
