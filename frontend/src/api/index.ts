import axios from 'axios';

import { STORAGE_KEY } from '../constants';
import {
  AccessTokenResponse,
  CardResponse,
  CardsResponse,
  QuizResponse,
  TagResponse,
  UserInfoResponse,
  WorkbookResponse,
} from '../types';
import { getLocalStorage } from '../utils';

interface PostCardAsync {
  question: string;
  answer: string;
  workbookId: number;
}

interface PostWorkbookAsync {
  name: string;
  opened: boolean;
  tags: TagResponse[];
}

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

export const getGuestQuizzesAsync = async () => {
  const { data } = await request.get<QuizResponse[]>('/quizzes/guest');

  return data;
};

export const getUserInfoAsync = async () => {
  const { data } = await request.get<UserInfoResponse>('/users/me');

  return data;
};

export const postCardAsync = async (params: PostCardAsync) => {
  await request.post('/cards', params);
};

export const putCardAsync = async (cardInfo: CardResponse) => {
  const { id, ...params } = cardInfo;

  await request.put(`/cards/${id}`, params);
};

export const deleteCardAsync = async (id: number) => {
  await request.delete(`/cards/${id}`);
};

export const postWorkbookAsync = async (params: PostWorkbookAsync) => {
  await request.post('/workbook', params);
};

export const putWorkbookAsync = async (workbookInfo: WorkbookResponse) => {
  const { id, ...params } = workbookInfo;

  await request.put(`/workbook/${id}`, params);
};

export const deleteWorkbookAsync = async (id: number) => {
  await request.delete(`/workbook/${id}`);
};
