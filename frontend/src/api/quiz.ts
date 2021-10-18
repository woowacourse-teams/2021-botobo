import { QuizResponse } from './../types';
import { noAuthorization, request } from './request';

export const getQuizzesAsync = async (workbookId: number) => {
  const { data } = await request.get<QuizResponse[]>(`/quizzes/${workbookId}`, {
    headers: noAuthorization,
  });

  return data;
};

export const postQuizzesAsync = async (
  workbookIds: number[],
  count: number
) => {
  const { data } = await request.post<QuizResponse[]>('/quizzes', {
    workbookIds,
    count,
  });

  return data;
};

export const getGuestQuizzesAsync = async () => {
  const { data } = await request.get<QuizResponse[]>('/quizzes/guest', {
    headers: noAuthorization,
  });

  return data;
};

export const putNextQuizAsync = async (cardIds: number[]) => {
  await request.put('/cards/next-quiz', { cardIds });
};
