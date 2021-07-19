import { CardsResponse, CategoryResponse, QuizResponse } from '../types';
import { REQUEST } from './request';

export const getAccessTokenAsync = async (code: string) => {
  const accessToken = await REQUEST.POST<string>({
    path: '/login',
    data: { code },
  });

  return accessToken;
};

export const getCategoriesAsync = async () => {
  const categories = await REQUEST.GET<CategoryResponse[]>({
    path: '/categories',
  });

  return categories;
};

export const getCardsAsync = async (categoryId: number) => {
  const cards = await REQUEST.GET<CardsResponse>({
    path: `/categories/${categoryId}/cards`,
  });

  return cards;
};

export const postQuizzesAsync = async (categoryIds: number[]) => {
  const quizzes = await REQUEST.POST<QuizResponse[]>({
    path: '/quizzes',
    data: { categoryIds },
  });

  return quizzes;
};
