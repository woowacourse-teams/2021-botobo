import { useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { categoryState, quizState } from './../recoil';
import { postQuizzesAsync } from '../api';
import { ROUTE } from '../constants';
import useSnackbar from './useSnackbar';

const useQuizSetting = () => {
  const { data, errorMessage } = useRecoilValue(categoryState);
  const [categories, setCategories] = useState(
    data
      .filter(({ cardCount }) => cardCount > 0)
      .map((category) => ({
        ...category,
        isChecked: false,
      }))
  );
  const setQuizState = useSetRecoilState(quizState);
  const history = useHistory();
  const showSnackbar = useSnackbar();

  const checkCategory = (id: number) => {
    const newCategories = categories.map((category) => {
      if (category.id !== id) return category;

      return {
        ...category,
        isChecked: !category.isChecked,
      };
    });

    setCategories(newCategories);
  };

  const startQuiz = async () => {
    const categoryIds = categories
      .filter(({ isChecked }) => isChecked)
      .map((category) => category.id);

    if (categoryIds.length === 0) {
      alert('카테고리를 선택해주세요!');

      return;
    }

    try {
      const quizzes = await postQuizzesAsync(categoryIds);

      setQuizState(quizzes);
      history.push(ROUTE.QUIZ.PATH);
    } catch (error) {
      showSnackbar({ message: '퀴즈 생성에 실패했습니다.', type: 'error' });
    }
  };

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { categories, checkCategory, startQuiz };
};

export default useQuizSetting;
