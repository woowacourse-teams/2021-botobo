import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { ROUTE } from '../constants';
import { quizState } from '../recoil';

const useQuiz = () => {
  const quizzes = useRecoilValue(quizState);
  const [currentQuizIndex, setCurrentQuizIndex] = useState(0);
  const [prevQuizId, setPrevQuizId] = useState<number | null>(null);
  const history = useHistory();

  const showPrevQuiz = () => {
    if (currentQuizIndex === 0) return;

    setPrevQuizId(quizzes[currentQuizIndex].id);
    setCurrentQuizIndex((prevValue) => prevValue - 1);
  };

  const showNextQuiz = () => {
    if (currentQuizIndex === quizzes.length - 1) {
      history.push(ROUTE.QUIZ_RESULT.PATH);

      return;
    }

    setPrevQuizId(quizzes[currentQuizIndex].id);
    setCurrentQuizIndex((prevValue) => prevValue + 1);
  };

  return { quizzes, currentQuizIndex, prevQuizId, showPrevQuiz, showNextQuiz };
};

export default useQuiz;
