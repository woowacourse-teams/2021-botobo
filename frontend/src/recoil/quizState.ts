import { atom } from 'recoil';

import { QUIZ_MODE } from './../constants/index';
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
