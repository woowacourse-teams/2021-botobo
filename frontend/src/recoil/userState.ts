import { atom, selector } from 'recoil';

import { getUserInfoAsync } from './../api/index';

export const userState = atom({
  key: 'userState',
  default: selector({
    key: 'userRequest',
    get: async () => {
      try {
        return await getUserInfoAsync();
      } catch (error) {
        return null;
      }
    },
  }),
});
