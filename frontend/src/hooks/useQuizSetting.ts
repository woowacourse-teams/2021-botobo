import { useEffect, useState } from 'react';
import { useRecoilValue, useSetRecoilState } from 'recoil';

import { QUIZ_MODE } from './../constants';
import { quizState, workbookState } from './../recoil';
import { quizModeState } from './../recoil/quizState';
import { postQuizzesAsync } from '../api';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

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

  const showSnackbar = useSnackbar();
  const { routeQuiz } = useRouter();

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

  const startQuiz = async () => {
    const workbookIds = workbooks
      .filter(({ isChecked }) => isChecked)
      .map((workbook) => workbook.id);

    if (workbookIds.length === 0) {
      alert('카테고리를 선택해주세요!');

      return;
    }

    try {
      const quizzes = await postQuizzesAsync(workbookIds);

      setQuizzes(quizzes);
      setQuizMode(QUIZ_MODE.DEFAULT);
      routeQuiz();
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '퀴즈 생성에 실패했어요.', type: 'error' });
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
