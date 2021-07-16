import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { categoryState, quizState } from './../recoil';
import { postQuizzesAsync } from '../api';
import { ROUTE } from '../constants';

const useQuizSetting = () => {
  const [categories, setCategories] = useState(
    useRecoilValue(categoryState).map((category) => ({
      ...category,
      isChecked: false,
    }))
  );
  const setQuizState = useSetRecoilState(quizState);
  const history = useHistory();

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

    const quizzes = await postQuizzesAsync(categoryIds);

    setQuizState(quizzes);
    history.push(ROUTE.QUIZ);
  };

  return { categories, checkCategory, startQuiz };
};

export default useQuizSetting;
