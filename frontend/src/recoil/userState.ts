import { atom } from 'recoil';

import { STORAGE_KEY } from '../constants';
import { getLocalStorage } from '../utils';

const token = getLocalStorage(STORAGE_KEY.TOKEN);

export const loginState = atom({
  key: 'loginState',
  default: Boolean(token),
});
