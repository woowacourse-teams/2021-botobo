import { useState } from 'react';
import { useRecoilValue } from 'recoil';

import { quizState } from '../recoil';

const useQuizResult = () => {
  const [quizResults, setQuizResults] = useState(
    useRecoilValue(quizState).map((quizResult) => ({
      ...quizResult,
      isChecked: false,
    }))
  );

  const checkQuizResult = (id: number) => {
    const newQuizResults = quizResults.map((quizResult) => {
      if (quizResult.id !== id) return quizResult;

      return {
        ...quizResult,
        isChecked: !quizResult.isChecked,
      };
    });

    setQuizResults(newQuizResults);
  };

  return { quizResults, checkQuizResult };
};

export default useQuizResult;
