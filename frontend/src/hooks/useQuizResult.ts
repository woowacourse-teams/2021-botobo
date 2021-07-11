import { useState } from 'react';
import { useRecoilValue } from 'recoil';

import { quizResultState } from '../recoil';

const useQuizResult = () => {
  const [quizResults, setQuizResults] = useState(
    useRecoilValue(quizResultState)
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
