import { atom } from 'recoil';

import { PublicWorkbookResponse } from '../types';

export const publicSearchResultState = atom<PublicWorkbookResponse[]>({
  key: 'publicSearchResultState',
  default: [],
});

export const publicSearchInitialLoadState = atom({
  key: 'publicSearchInitialLoadState',
  default: true,
});
