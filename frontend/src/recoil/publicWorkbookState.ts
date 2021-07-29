import { atom } from 'recoil';

import { getSessionStorage } from './../utils/storage';
import { STORAGE_KEY } from '../constants';

export const searchKeywordState = atom<string>({
  key: 'searchKeywordState',
  default: '',
});

export const publicWorkbookIdState = atom<number>({
  key: 'publicWorkbookIdState',
  default: getSessionStorage(STORAGE_KEY.PUBLIC_WORKBOOK_ID) ?? -1,
});
