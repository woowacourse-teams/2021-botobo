import { CardsResponse } from './../types/index';
import { CategoryResponse, QuizResponse } from '../types';
import { REQUEST } from './request';

interface CardData {
  categoryId: number;
}

interface QuizSettingData {
  categoryIds: number[];
}

export const getCategoriesAsync = async () => {
  const categories = await REQUEST.GET<CategoryResponse[]>({
    path: '/categories',
  });

  return categories;
};

export const getCardsAsync = async ({ categoryId }: CardData) => {
  const cards = await REQUEST.GET<CardsResponse>({
    path: `/categories/${categoryId}/cards`,
  });

  return cards;
};

export const postQuizzesAsync = async ({ categoryIds }: QuizSettingData) => {
  const quizzes = await REQUEST.POST<QuizResponse[]>({
    path: '/quizzes',
    data: { categoryIds },
  });

  return quizzes;
};
