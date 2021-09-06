import { DefaultValue, atom, selector } from 'recoil';

import { getPublicWorkbookAsync, getWorkbooksAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { PublicWorkbookResponse, WorkbookResponse } from '../types';
import { getSessionStorage } from '../utils';
import { workbookInitialState } from './initialState';
import { userState } from './userState';

interface WorkbookState {
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

const workbookUpdateTrigger = atom({
  key: 'workbookUpdateTrigger',
  default: 0,
});

export const workbookIdState = atom<number>({
  key: 'workbookIdState',
  default: getSessionStorage(STORAGE_KEY.WORKBOOK_ID) ?? -1,
});

export const workbookState = selector<WorkbookState>({
  key: 'workbookState',
  get: async ({ get }) => {
    get(workbookUpdateTrigger);

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
  set: ({ set }, value) => {
    if (value instanceof DefaultValue) {
      set(workbookUpdateTrigger, (prevState) => prevState + 1);
      set(shouldWorkbookUpdateState, false);
    }
  },
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

const publicWorkbookUpdateTrigger = atom({
  key: 'publicWorkbookUpdateTrigger',
  default: 0,
});

export const publicWorkbookState = selector<PublicWorkbookState>({
  key: 'publicWorkbookState',
  get: async ({ get }) => {
    get(publicWorkbookUpdateTrigger);

    try {
      return {
        data: await getPublicWorkbookAsync(),
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: [],
        errorMessage: '문제집을 불러오지 못했습니다.',
      };
    }
  },
  set: ({ set }, value) => {
    if (value instanceof DefaultValue) {
      set(publicWorkbookUpdateTrigger, (prevState) => prevState + 1);
    }
  },
});
