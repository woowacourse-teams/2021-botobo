import { DefaultValue, atom, selector } from 'recoil';

import { getWorkbooksAsync } from '../api';
import { STORAGE_KEY } from '../constants';
import { WorkbookResponse } from '../types';
import { getSessionStorage } from '../utils';
import { userState } from './userState';

interface WorkbookState {
  data: WorkbookResponse[];
  errorMessage: string | null;
}

const tagInitialState = {
  id: -1,
  name: '',
};

const workbookInitialState = {
  id: -1,
  name: '',
  cardCount: -1,
  opened: true,
  tags: [tagInitialState],
};

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
    try {
      return {
        data: get(userState) ? await getWorkbooksAsync() : [],
        errorMessage: null,
      };
    } catch (error) {
      return {
        data: [workbookInitialState],
        errorMessage: '문제집을 불러오지 못했습니다.',
      };
    }
  },
  set: ({ set }, value) => {
    if (value instanceof DefaultValue) {
      set(workbookUpdateTrigger, (prevState) => prevState + 1);
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
