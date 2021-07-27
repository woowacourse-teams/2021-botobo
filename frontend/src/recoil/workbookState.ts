import { atom, selector } from 'recoil';

import { getWorkbooksAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { WorkbookResponse } from '../types';
import { getSessionStorage } from '../utils';
import { userState } from './userState';

interface WorkbookState {
  data: WorkbookResponse[];
  errorMessage: string | null;
}

const workbookInitialState = <never[]>[];

export const workbookIdState = atom<number>({
  key: 'workbookIdState',
  default: getSessionStorage(STORAGE_KEY.WORKBOOK_ID) ?? -1,
});

export const workbookState = atom<WorkbookState>({
  key: 'workbookState',
  default: selector({
    key: 'workbookRequest',
    get: async ({ get }) => {
      try {
        return {
          data: get(userState) ? await getWorkbooksAsync() : [],
          errorMessage: null,
        };
      } catch (error) {
        return {
          data: workbookInitialState,
          errorMessage: '문제집을 불러오지 못했습니다.',
        };
      }
    },
  }),
});
