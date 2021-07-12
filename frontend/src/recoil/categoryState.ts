import { atom, selector } from 'recoil';

import { getCategoriesAsync } from '../api';
import { CategoryResponse } from '../types';

export const categoryState = atom<CategoryResponse[]>({
  key: 'categoryState',
  default: selector({
    key: 'categoryRequest',
    get: () => getCategoriesAsync(),
  }),
});
