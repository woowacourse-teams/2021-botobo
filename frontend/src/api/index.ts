import axios from 'axios';

import { CardsResponse, QuizResponse, WorkbookResponse } from '../types';

const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}/api`,
});

export const getAccessTokenAsync = async (code: string) => {
  const { data } = await request.post<string>('/login', { code });

  return data;
};

export const getWorkbooksAsync = async () => {
  const { data } = await request.get<WorkbookResponse[]>('/workbooks');

  return data;
};

export const getCardsAsync = async (workbookId: number) => {
  const { data } = await request.get<CardsResponse>(
    `/workbooks/${workbookId}/cards`
  );

  return data;
};

export const postQuizzesAsync = async (workbookIds: number[]) => {
  const { data } = await request.post<QuizResponse[]>('/quizzes', {
    workbookIds,
  });

  return data;
};
