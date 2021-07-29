import { selector } from 'recoil';

import { getPublicCardsAsync } from './../api/index';
import { PublicCardsResponse } from './../types/index';
import { publicWorkbookIdState } from './publicWorkbookState';

interface PublicCardState {
  data: PublicCardsResponse;
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

const tagInitialState = {
  id: -1,
  name: '',
};

const cardsInitialState = {
  workbookId: -1,
  workbookName: '',
  cards: [cardInitialState],
  cardCount: -1,
  tags: [tagInitialState],
};

export const publicCardState = selector<PublicCardState>({
  key: 'publicCardState',
  get: async ({ get }) => {
    try {
      const workbookId = get(publicWorkbookIdState);

      return {
        data:
          workbookId === -1
            ? cardsInitialState
            : await getPublicCardsAsync(workbookId),
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: cardsInitialState,
        errorMessage: '카드를 불러오지 못했습니다.',
      };
    }
  },
});
