import { atom } from 'recoil';

import { QuizResponse } from './../types';

export const quizState = atom<QuizResponse[]>({
  key: 'quizState',
  default: [],
});
