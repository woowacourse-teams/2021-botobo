import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { QUIZ_MODE } from './../constants';
import { hasQuizTimeState, quizModeState } from './../recoil';
import { quizState } from '../recoil';
import useRouter from './useRouter';

const useQuiz = () => {
  const quizMode = useRecoilValue(quizModeState);
  const hasQuizTime = useRecoilValue(hasQuizTimeState);
  const quizzes =
    quizMode === 'GUEST'
      ? useRecoilValue(quizState).map((quiz, index) => ({
          ...quiz,
          id: index,
        }))
      : useRecoilValue(quizState);
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

  return {
    quizzes,
    hasQuizTime,
    currentQuizIndex,
    prevQuizId,
    showPrevQuiz,
    showNextQuiz,
  };
};

export default useQuiz;
