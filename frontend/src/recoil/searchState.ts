import { atom } from 'recoil';

import {
  getTagsWhenWorkbookSearchASync,
  getUsersWhenWorkbookSearchAsync,
} from '../api';
import { PublicWorkbookResponse } from '../types';
import { MultiFilter, SingleFilter } from '../types/filter';

interface PublicSearchState {
  isInitialLoading: boolean;
  startIndex: number;
  publicWorkbookResult: PublicWorkbookResponse[];
  singleFilters: SingleFilter[];
  multiFilters: MultiFilter[];
}

export const publicSearchResultState = atom<PublicSearchState>({
  key: 'publicSearchResultState',
  default: {
    isInitialLoading: true,
    startIndex: 0,
    publicWorkbookResult: [],
    singleFilters: [
      { id: 1, type: '최신순', criteria: 'date' },
      { id: 2, type: '좋아요 순', criteria: 'heart' },
      { id: 3, type: '이름 순', criteria: 'name' },
      { id: 4, type: '카드 개수 순', criteria: 'count' },
    ],
    multiFilters: [
      {
        id: 1,
        type: 'tags',
        name: '태그',
        values: [],
        getValues: getTagsWhenWorkbookSearchASync,
      },
      {
        id: 2,
        type: 'users',
        name: '작성자',
        values: [],
        getValues: getUsersWhenWorkbookSearchAsync,
      },
    ],
  },
});
