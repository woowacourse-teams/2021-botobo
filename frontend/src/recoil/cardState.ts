import { selector } from 'recoil';

import { getCardsAsync } from '../api';
import { workbookIdState } from './workbookState';

const cardInitialState = { workbookId: -1, workbookName: '', cards: [] };

export const cardState = selector({
  key: 'cardState',
  get: async ({ get }) => {
    try {
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
});
