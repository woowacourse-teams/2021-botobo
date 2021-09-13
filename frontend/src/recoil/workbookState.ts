import { atom, selector } from 'recoil';

import { getPublicWorkbookAsync, getWorkbooksAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { PublicWorkbookResponse, WorkbookResponse } from '../types';
import { getSessionStorage } from '../utils';
import { workbookInitialState } from './initialState';
import { userState } from './userState';

export interface WorkbookState {
  data: WorkbookResponse[];
  errorMessage: string | null;
}

interface PublicWorkbookState {
  data: PublicWorkbookResponse[];
  errorMessage: string | null;
}

export const shouldWorkbookUpdateState = atom({
  key: 'shouldWorkbookUpdateState',
  default: false,
});

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
          data: [],
          errorMessage: '문제집을 불러오지 못했어요.',
        };
      }
    },
  }),
});

export const editedWorkbookState = selector<WorkbookResponse>({
  key: 'editedWorkbookState',
  get: ({ get }) => {
    const workbookId = get(workbookIdState);
    const { data } = get(workbookState);

    return (
      data.find((workbook) => workbook.id === workbookId) ||
      workbookInitialState
    );
  },
});

export const publicWorkbookState = selector<PublicWorkbookState>({
  key: 'publicWorkbookState',
  get: async () => {
    try {
      return {
        data: await getPublicWorkbookAsync(),
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: [],
        errorMessage: '문제집을 불러오지 못했어요.',
      };
    }
  },
});
