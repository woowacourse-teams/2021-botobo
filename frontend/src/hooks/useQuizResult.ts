import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { putNextQuizAsync } from './../api';
import { hasQuizTimeState, quizState, quizTimeState } from '../recoil';
import useErrorHandler from './useErrorHandler';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useQuizResult = () => {
  const quizzes = useRecoilValue(quizState);
  const quizTime = useRecoilValue(quizTimeState);
  const hasQuizTime = useRecoilValue(hasQuizTimeState);
  const [quizResults, setQuizResults] = useState(
    quizzes.map((quiz) => ({ ...quiz, isChecked: false }))
  );
  const checkedCount = quizResults.filter(({ isChecked }) =>
    Boolean(isChecked)
  ).length;

  const showSnackbar = useSnackbar();
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

  const setNextQuiz = async () => {
    const cardIds = quizResults
      .filter(({ isChecked }) => Boolean(isChecked))
      .map(({ id }) => id);

    if (cardIds.length === 0) return;

    try {
      await putNextQuizAsync(cardIds);
      showSnackbar({ message: '다음에 볼 카드가 설정되었어요.' });
      routeMain();
    } catch (error) {
      useErrorHandler(error);
    }
  };

  useEffect(() => {
    if (quizResults.length !== 0) return;
    routeMain();
  }, []);

  return {
    quizResults,
    quizTime,
    hasQuizTime,
    checkedCount,
    checkQuizResult,
    setNextQuiz,
  };
};

export default useQuizResult;
