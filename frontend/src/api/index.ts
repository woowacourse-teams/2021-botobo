import { CategoryResponse, QuizResponse } from '../types';
import { REQUEST } from './request';

interface QuizSettingData {
  categoryIds: number[];
}

export const getCategoriesAsync = async () => {
  const categories = await REQUEST.GET<CategoryResponse[]>({
    path: '/categories',
  });

  return categories;
};

export const postQuizzesAsync = async ({ categoryIds }: QuizSettingData) => {
  const quizzes = await REQUEST.POST<QuizResponse[]>({
    path: '/quizzes',
    data: { categoryIds },
  });

  return quizzes;
};
