import { atom, selector } from 'recoil';

import { getUserInfoAsync } from './../api';

export const userState = atom({
  key: 'userState',
  default: selector({
    key: 'userRequest',
    get: async () => {
      console.log('userState');
      try {
        return await getUserInfoAsync();
      } catch (error) {
        return null;
      }
    },
  }),
});
