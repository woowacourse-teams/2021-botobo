import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { QUIZ_MODE } from './../constants';
import { quizModeState } from './../recoil/quizState';
import { quizState } from '../recoil';
import useRouter from './useRouter';

const useQuiz = () => {
  const quizzes = useRecoilValue(quizState);
  const quizMode = useRecoilValue(quizModeState);
  const [currentQuizIndex, setCurrentQuizIndex] = useState(0);
  const [prevQuizId, setPrevQuizId] = useState<number | null>(null);
  const { routeQuizResult, routeMain, routePrevPage } = useRouter();

  const showPrevQuiz = () => {
    if (currentQuizIndex === 0) return;

    setPrevQuizId(quizzes[currentQuizIndex].id);
    setCurrentQuizIndex((prevValue) => prevValue - 1);
  };

  const showNextQuiz = () => {
    const routeByQuizMode = {
      [QUIZ_MODE.DEFAULT]: routeQuizResult,
      [QUIZ_MODE.GUEST]: routeMain,
      [QUIZ_MODE.OTHERS]: routePrevPage,
    };

    if (currentQuizIndex === quizzes.length - 1) {
      routeByQuizMode[quizMode]();

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
