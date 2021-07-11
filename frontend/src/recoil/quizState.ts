import { selector } from 'recoil';

import { QuizResponse, QuizResult } from '../types';

const dummy = [
  {
    id: 1,
    question: '하하하하하하하하하하하하하하하하하하하하하하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 2,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 4,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 5,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 6,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
  {
    id: 7,
    question: '하하하',
    answer: '호호호',
    categoryName: '카테고리',
  },
];

export const quizState = selector<QuizResponse[]>({
  key: 'quizState',
  get: () => dummy,
});

export const quizResultState = selector<QuizResult[]>({
  key: 'quizResultState',
  get: ({ get }) => {
    return get(quizState).map((quiz) => ({
      ...quiz,
      isChecked: false,
    }));
  },
});
