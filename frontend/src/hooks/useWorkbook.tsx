import { useResetRecoilState } from 'recoil';

import { postWorkbookAsync } from '../api';
import { workbookState } from '../recoil';
import useSnackbar from './useSnackbar';

const useWorkbook = () => {
  const updateWorkbooks = useResetRecoilState(workbookState);
  const showSnackbar = useSnackbar();

  const createWorkbook = async (
    name: string,
    tags: string[],
    opened: boolean
  ) => {
    try {
      await postWorkbookAsync({ name, tags, opened });
      updateWorkbooks();
      showSnackbar({ message: '문제집이 추가되었어요.' });
    } catch (error) {
      console.error();
      showSnackbar({ message: '문제집을 추가하지 못했어요.', type: 'error' });
    }
  };

  return { createWorkbook };
};

export default useWorkbook;
