import { useEffect } from 'react';
import { useRecoilValue, useResetRecoilState, useSetRecoilState } from 'recoil';

import {
  deleteWorkbookAsync,
  postWorkbookAsync,
  putWorkbookAsync,
} from '../api';
import { editedWorkbookState, workbookIdState, workbookState } from '../recoil';
import { TagResponse, WorkbookResponse } from '../types';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useWorkbook = () => {
  const { data: workbooks, errorMessage } = useRecoilValue(workbookState);
  const editedWorkbook = useRecoilValue(editedWorkbookState);
  const setWorkbookId = useSetRecoilState(workbookIdState);
  const updateWorkbooks = useResetRecoilState(workbookState);

  const { routeMain } = useRouter();
  const showSnackbar = useSnackbar();

  const createWorkbook = async (
    name: string,
    tags: TagResponse[],
    opened: boolean
  ) => {
    try {
      await postWorkbookAsync({ name, tags, opened });
      updateWorkbooks();
      showSnackbar({ message: '문제집이 추가되었어요.' });
      routeMain();
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '문제집을 추가하지 못했어요.', type: 'error' });
    }
  };

  const editWorkbook = async (workbookInfo: WorkbookResponse) => {
    try {
      await putWorkbookAsync(workbookInfo);
      updateWorkbooks();
      showSnackbar({ message: '문제집이 수정되었어요.' });
      routeMain();
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '문제집을 수정하지 못했어요.', type: 'error' });
    }
  };

  const deleteWorkbook = async (id: number) => {
    try {
      await deleteWorkbookAsync(id);
      updateWorkbooks();
      showSnackbar({ message: '문제집이 삭제되었어요.' });
    } catch (error) {
      console.error(error);
      showSnackbar({ message: '문제집을 삭제하지 못했어요.', type: 'error' });
    }
  };

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return {
    workbooks,
    editedWorkbook,
    setWorkbookId,
    createWorkbook,
    editWorkbook,
    deleteWorkbook,
  };
};

export default useWorkbook;
