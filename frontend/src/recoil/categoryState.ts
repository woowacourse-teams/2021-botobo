import { atom, selector } from 'recoil';

import { getCategoriesAsync } from '../api';
import { CategoryResponse } from '../types';

interface CategoryState {
  data: CategoryResponse[];
  errorMessage: string | null;
}

const categoryInitialState = <never[]>[];

export const categoryState = atom<CategoryState>({
  key: 'categoryState',
  default: selector({
    key: 'categoryRequest',
    get: async () => {
      try {
        return { data: await getCategoriesAsync(), errorMessage: null };
      } catch (error) {
        return {
          data: categoryInitialState,
          errorMessage: '문제집을 불러오지 못했습니다.',
        };
      }
    },
  }),
});
