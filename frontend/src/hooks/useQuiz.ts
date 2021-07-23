import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { quizState } from '../recoil';
import useRouter from './useRouter';

const useQuiz = () => {
  const quizzes = useRecoilValue(quizState);
  const [currentQuizIndex, setCurrentQuizIndex] = useState(0);
  const [prevQuizId, setPrevQuizId] = useState<number | null>(null);
  const { routeQuizResult, routeMain } = useRouter();

  const showPrevQuiz = () => {
    if (currentQuizIndex === 0) return;

    setPrevQuizId(quizzes[currentQuizIndex].id);
    setCurrentQuizIndex((prevValue) => prevValue - 1);
  };

  const showNextQuiz = () => {
    if (currentQuizIndex === quizzes.length - 1) {
      routeQuizResult();

      return;
    }

    setPrevQuizId(quizzes[currentQuizIndex].id);
    setCurrentQuizIndex((prevValue) => prevValue + 1);
  };

  useEffect(() => {
    if (quizzes.length !== 0) return;

    routeMain();
  }, []);

  return { quizzes, currentQuizIndex, prevQuizId, showPrevQuiz, showNextQuiz };
};

export default useQuiz;
