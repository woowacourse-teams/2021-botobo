import axios from 'axios';

import { STORAGE_KEY } from '../constants';
import {
  AccessTokenResponse,
  CardsResponse,
  QuizResponse,
  WorkbookResponse,
} from '../types';
import { getLocalStorage } from '../utils';

const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}/api`,
});

const token = getLocalStorage(STORAGE_KEY.TOKEN);

request.defaults.headers.common['Authorization'] = `Bearer ${token}`;

export const getAccessTokenAsync = async (code: string) => {
  const {
    data: { accessToken },
  } = await request.post<AccessTokenResponse>('/login', { code });

  request.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

  return accessToken;
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
