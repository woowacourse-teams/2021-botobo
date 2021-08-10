import axios from 'axios';

import {
  SEARCH_CRITERIA,
  SEARCH_ORDER,
  SEARCH_TYPE,
  STORAGE_KEY,
} from '../constants';
import {
  AccessTokenResponse,
  CardResponse,
  CardsResponse,
  PublicCardsResponse,
  PublicWorkbookResponse,
  QuizResponse,
  SearchKeywordResponse,
  TagResponse,
  UserInfoResponse,
  WorkbookResponse,
} from '../types';
import { ValueOf } from '../types/utils';
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

export interface PublicWorkbookAsync {
  keyword: string;
  criteria?: ValueOf<typeof SEARCH_CRITERIA>;
  order?: ValueOf<typeof SEARCH_ORDER>;
  type?: ValueOf<typeof SEARCH_TYPE>;
  start?: number;
  size?: number;
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
  const { data } = await request.post<WorkbookResponse>('/workbooks', params);

  return data;
};

export const putWorkbookAsync = async (workbookInfo: WorkbookResponse) => {
  const { id, ...params } = workbookInfo;

  await request.put(`/workbooks/${id}`, params);
};

export const deleteWorkbookAsync = async (id: number) => {
  await request.delete(`/workbooks/${id}`);
};

export const getPublicWorkbookAsync = async ({
  keyword,
  criteria = 'date',
  order = 'desc',
  type = 'name',
  start = 0,
  size = 20,
}: PublicWorkbookAsync) => {
  const { data } = await request.get<PublicWorkbookResponse[]>(
    `/search/workbooks?type=${type}&criteria=${criteria}&order=${order}&keyword=${keyword}&start=${start}&size=${size}`
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
  await request.put('/cards/next-quiz', { cardIds });
};

export const putProfileAsync = async (
  userInfo: Omit<UserInfoResponse, 'id'>
) => {
  await request.put(`/users/me`, userInfo);
};

export const postProfileImageAsync = async (formData: FormData) => {
  const { data } = await request.post<Pick<UserInfoResponse, 'profileUrl'>>(
    '/users/profile',
    formData
  );

  return data;
};

export const postUserNameCheckAsync = async (userName: string) => {
  await request.post('/users/name-check', userName);
};

export const putHeartAsync = async (id: number) => {
  const { data } = await request.put<boolean>(`workbooks/${id}/hearts`);

  return data;
};

export const getUserKeywordAsync = async (keyword: string) => {
  const { data } = await request.get<SearchKeywordResponse[]>(
    `/search/users?keyword=${keyword}`
  );

  return data;
};

export const getTagKeywordAsync = async (keyword: string) => {
  const { data } = await request.get<SearchKeywordResponse[]>(
    `/search/tags?keyword=${keyword}`
  );

  return data;
};
