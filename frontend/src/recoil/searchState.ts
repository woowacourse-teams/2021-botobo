import { atom } from 'recoil';

import { SEARCH_TYPE } from '../constants';
import { ValueOf } from '../types/utils';

export const searchKeywordState = atom<string>({
  key: 'searchKeywordState',
  default: '',
});

export const searchTypeState = atom<ValueOf<typeof SEARCH_TYPE>>({
  key: 'searchTypeState',
  default: 'name',
});
