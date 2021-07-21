import axios from 'axios';

import { CardsResponse, CategoryResponse, QuizResponse } from '../types';

const request = axios.create({
  baseURL: `${process.env.REACT_APP_SERVER_URL}`,
});

export const getAccessTokenAsync = async (code: string) => {
  const accessToken = await request.post<string>('/login', { code });

  return accessToken;
};

export const getCategoriesAsync = async () => {
  const categories = await request.get<CategoryResponse[]>('/api/workbooks');

  return categories;
};

export const getCardsAsync = async (categoryId: number) => {
  const cards = await request.get<CardsResponse>(
    `/categories/${categoryId}/cards`
  );

  return cards;
};

export const postQuizzesAsync = async (categoryIds: number[]) => {
  const quizzes = await request.post<QuizResponse[]>('/quizzes', {
    categoryIds,
  });

  return quizzes;
};
