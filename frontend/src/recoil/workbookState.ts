import { atom, selector } from 'recoil';

import { getPublicWorkbookAsync, getWorkbooksAsync } from '../api';
import { PublicWorkbookResponse, WorkbookResponse } from '../types';
import {
  handleAccessTokenRefreshError,
  isAccessTokenRefreshError,
} from '../utils/error';
import { userState } from './userState';

export interface WorkbookState {
  data: WorkbookResponse[];
  errorMessage: string | null;
}

interface PublicWorkbookState {
  data: PublicWorkbookResponse[];
  errorMessage: string | null;
}

const workbookErrorState = {
  data: [],
  errorMessage: '문제집을 불러오지 못했어요.',
};

export const shouldWorkbookUpdateState = atom({
  key: 'shouldWorkbookUpdateState',
  default: false,
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
        if (!isAccessTokenRefreshError(error)) return workbookErrorState;

        return await handleAccessTokenRefreshError({
          resolve: async () => ({
            data: await getWorkbooksAsync(),
            errorMessage: null,
          }),
          returnValue: workbookErrorState,
        });
      }
    },
  }),
});

export const publicWorkbookState = atom<PublicWorkbookState>({
  key: 'publicWorkbookState',
  default: selector({
    key: 'publicWorkbookRequest',
    get: async () => {
      try {
        return {
          data: await getPublicWorkbookAsync(),
          errorMessage: null,
        };
      } catch (error) {
        return workbookErrorState;
      }
    },
  }),
});
