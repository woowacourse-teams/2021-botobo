import { atom, selector } from 'recoil';

import { getWorkbooksAsync } from '../api';
import { WorkbookResponse } from '../types';

interface WorkbookState {
  data: WorkbookResponse[];
  errorMessage: string | null;
}

const workbookInitialState = <never[]>[];

export const workbookState = atom<WorkbookState>({
  key: 'workbookState',
  default: selector({
    key: 'workbookRequest',
    get: async () => {
      try {
        return { data: await getWorkbooksAsync(), errorMessage: null };
      } catch (error) {
        return {
          data: workbookInitialState,
          errorMessage: '문제집을 불러오지 못했습니다.',
        };
      }
    },
  }),
});
