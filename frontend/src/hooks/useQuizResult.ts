import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { quizState } from '../recoil';
import useRouter from './useRouter';

const useQuizResult = () => {
  const [quizResults, setQuizResults] = useState(
    useRecoilValue(quizState).map((quizResult) => ({
      ...quizResult,
      isChecked: false,
    }))
  );
  const { routeMain } = useRouter();

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

  useEffect(() => {
    if (quizResults.length !== 0) return;
    routeMain();
  }, []);

  return { quizResults, checkQuizResult };
};

export default useQuizResult;
