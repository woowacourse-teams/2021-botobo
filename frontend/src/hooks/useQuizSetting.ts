import { useEffect, useState } from 'react';
import { useRecoilValue, useResetRecoilState, useSetRecoilState } from 'recoil';

import { QUIZ_MODE } from './../constants';
import {
  hasQuizTimeState,
  quizModeState,
  quizState,
  quizTimeState,
  workbookState,
} from './../recoil';
import { postQuizzesAsync } from '../api';
import useErrorHandler from './useErrorHandler';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

interface StartQuiz {
  count: number;
  isTimeChecked: boolean;
}

const useQuizSetting = () => {
  const { data, errorMessage } = useRecoilValue(workbookState);
  const [workbooks, setWorkbooks] = useState(
    data
      .filter(({ cardCount }) => cardCount > 0)
      .map((workbook) => ({
        ...workbook,
        isChecked: false,
      }))
  );
  const setQuizzes = useSetRecoilState(quizState);
  const setQuizMode = useSetRecoilState(quizModeState);
  const setHasQuizTime = useSetRecoilState(hasQuizTimeState);
  const resetQuizTime = useResetRecoilState(quizTimeState);

  const showSnackbar = useSnackbar();
  const { routeQuiz } = useRouter();
  const errorHandler = useErrorHandler();

  const checkWorkbook = (id: number) => {
    const newWorkbooks = workbooks.map((workbook) => {
      if (workbook.id !== id) return workbook;

      return {
        ...workbook,
        isChecked: !workbook.isChecked,
      };
    });

    setWorkbooks(newWorkbooks);
  };

  const startQuiz = async ({ count, isTimeChecked }: StartQuiz) => {
    const workbookIds = workbooks
      .filter(({ isChecked }) => isChecked)
      .map((workbook) => workbook.id);

    if (workbookIds.length === 0) {
      showSnackbar({ message: '카테고리를 선택해주세요.' });

      return;
    }

    try {
      const quizzes = await postQuizzesAsync(workbookIds, count);

      setQuizzes(quizzes);
      setQuizMode(QUIZ_MODE.DEFAULT);
      setHasQuizTime(isTimeChecked);
      resetQuizTime();
      routeQuiz();
    } catch (error) {
      errorHandler(error);
    }
  };

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return { workbooks, checkWorkbook, startQuiz };
};

export default useQuizSetting;
