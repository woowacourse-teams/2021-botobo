import { atom, selector } from 'recoil';

import { getRankingSearchKeywordAsync, getRankingWorkbooksAsync } from '../api';
import {
  RankingSearchKeywordResponse,
  RankingWorkbookResponse,
} from '../types';

export const rankingWorkbooksState = atom<RankingWorkbookResponse[]>({
  key: 'rankingWorkbooksState',
  default: selector({
    key: 'rankingWorkbooksRequest',
    get: async () => {
      try {
        return await getRankingWorkbooksAsync();
      } catch (error) {
        return [];
      }
    },
  }),
});

export const rankingSearchKeywordsState = atom<RankingSearchKeywordResponse[]>({
  key: 'rankingSearchKeywordsState',
  default: selector({
    key: 'rankingSearchKeywordsRequest',
    get: async () => {
      try {
        return await getRankingSearchKeywordAsync();
      } catch (error) {
        return [];
      }
    },
  }),
});
