import { atom } from 'recoil';

import { QUIZ_MODE } from './../constants';
import { QuizResponse } from './../types';

type QuizMode = keyof typeof QUIZ_MODE;

export const quizModeState = atom<QuizMode>({
  key: 'quizModeState',
  default: 'DEFAULT',
});

export const quizState = atom<QuizResponse[]>({
  key: 'quizState',
  default: [],
});

export const hasQuizTimeState = atom({
  key: 'hasQuizTimeState',
  default: false,
});

export const quizTimeState = atom({
  key: 'quizTimeState',
  default: 0,
});
