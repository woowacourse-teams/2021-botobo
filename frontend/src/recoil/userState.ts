import { atom, selector } from 'recoil';

import { getUserInfoAsync } from './../api';
import {
  handleAccessTokenRefreshError,
  isAccessTokenRefreshError,
} from '../utils/error';

export const userState = atom({
  key: 'userState',
  default: selector({
    key: 'userRequest',
    get: async () => {
      try {
        return await getUserInfoAsync();
      } catch (error) {
        if (!isAccessTokenRefreshError(error)) return null;

        return handleAccessTokenRefreshError({
          resolve: getUserInfoAsync,
          returnValue: null,
        });
      }
    },
  }),
});
