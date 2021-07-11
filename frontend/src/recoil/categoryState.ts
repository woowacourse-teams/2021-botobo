import { selector } from 'recoil';

import { CategoryResponse, QuizSetting } from '../types';

const dummy = [
  {
    id: 1,
    name: 'Java',
    description: '',
    cardCount: 2,
    logoUrl: '',
  },
  {
    id: 2,
    name: 'React',
    description: '',
    cardCount: 12,
    logoUrl: '',
  },
  {
    id: 3,
    name: 'JS',
    description: '',
    cardCount: 34,
    logoUrl: '',
  },
];

export const categoryState = selector<CategoryResponse[]>({
  key: 'categoryState',
  get: () => dummy,
});

export const quizSettingState = selector<QuizSetting[]>({
  key: 'quizSettingState',
  get: ({ get }) => {
    return get(categoryState).map((category) => ({
      ...category,
      isChecked: false,
    }));
  },
});
