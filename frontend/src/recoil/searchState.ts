import { atom } from 'recoil';

import {
  getTagsWhenWorkbookSearchASync,
  getUsersWhenWorkbookSearchAsync,
} from '../api';
import { PublicWorkbookResponse } from '../types';
import { MultiFilter } from '../types/filter';

interface PublicSearchState {
  isInitialLoading: boolean;
  startIndex: number;
  publicWorkbookResult: PublicWorkbookResponse[];
  multiFilters: MultiFilter[];
}

export const publicSearchResultState = atom<PublicSearchState>({
  key: 'publicSearchResultState',
  default: {
    isInitialLoading: true,
    startIndex: 0,
    publicWorkbookResult: [],
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
