import { atom, selector } from 'recoil';

import {
  getSearchKeywordRankingsAsync,
  getWorkbookRankingsAsync,
} from '../api';
import {
  SearchKeywordRankingResponse,
  WorkbookRankingResponse,
} from '../types';

export const workbookRankingState = atom<WorkbookRankingResponse[]>({
  key: 'workbookRankingState',
  default: selector({
    key: 'workbookRankingRequest',
    get: async () => {
      try {
        return await getWorkbookRankingsAsync();
      } catch (error) {
        return [];
      }
    },
  }),
});

export const searchKeywordRankingState = atom<SearchKeywordRankingResponse[]>({
  key: 'searchKeywordRankingState',
  default: selector({
    key: 'searchKeywordRankingRequest',
    get: async () => {
      try {
        return await getSearchKeywordRankingsAsync();
      } catch (error) {
        return [];
      }
    },
  }),
});
