import axios from 'axios';

import { STORAGE_KEY } from '../constants';
import {
  AccessTokenResponse,
  CardResponse,
  CardsResponse,
  PublicCardsResponse,
  PublicWorkbookResponse,
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

export const getQuizzesAsync = async (workbookId: number) => {
  const { data } = await request.get<QuizResponse[]>(`/quizzes/${workbookId}`);

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
  const { data } = await request.post<CardResponse>('/cards', params);

  return data;
};

export const putCardAsync = async (cardInfo: CardResponse) => {
  const { id, ...params } = cardInfo;

  const { data } = await request.put<CardResponse>(`/cards/${id}`, params);

  return data;
};

export const deleteCardAsync = async (id: number) => {
  await request.delete(`/cards/${id}`);
};

export const postWorkbookAsync = async (params: PostWorkbookAsync) => {
  await request.post('/workbooks', params);
};

export const putWorkbookAsync = async (workbookInfo: WorkbookResponse) => {
  const { id, ...params } = workbookInfo;

  await request.put(`/workbooks/${id}`, params);
};

export const deleteWorkbookAsync = async (id: number) => {
  await request.delete(`/workbooks/${id}`);
};

export const getPublicWorkbookAsync = async (keyword: string) => {
  const { data } = await request.get<PublicWorkbookResponse[]>(
    `/workbooks/public?search=${keyword}`
  );

  return data;
};

export const getPublicCardsAsync = async (publicWorkbookId: number) => {
  const { data } = await request.get<PublicCardsResponse>(
    `/workbooks/public/${publicWorkbookId}`
  );

  return data;
};

export const postPublicCardsAsync = async (
  workbookId: number,
  cardIds: number[]
) => {
  const { data } = await request.post<PublicCardsResponse>(
    `/workbooks/${workbookId}/cards`,
    { cardIds }
  );

  return data;
};

export const putNextQuizAsync = async (cardIds: number[]) => {
  await request.put(`/cards/next-quiz`, { cardIds });
};
