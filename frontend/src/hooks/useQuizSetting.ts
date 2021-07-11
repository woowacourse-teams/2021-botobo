import { useState } from 'react';
import { useRecoilValue } from 'recoil';

import { quizSettingState } from './../recoil';

const useQuizSetting = () => {
  const [categories, setCategories] = useState(
    useRecoilValue(quizSettingState)
  );

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

  return { categories, checkCategory };
};

export default useQuizSetting;
