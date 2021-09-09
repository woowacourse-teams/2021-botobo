import { atom } from 'recoil';

import { PublicWorkbookResponse } from '../types';
import { MultiFilterValue } from '../types/filter';

interface PublicSearchState {
  isInitialLoading: boolean;
  startIndex: number;
  publicWorkbookResult: PublicWorkbookResponse[];
  tags: MultiFilterValue[];
  users: MultiFilterValue[];
}

export const publicSearchResultState = atom<PublicSearchState>({
  key: 'publicSearchResultState',
  default: {
    isInitialLoading: true,
    startIndex: 0,
    publicWorkbookResult: [],
    tags: [],
    users: [],
  },
});
