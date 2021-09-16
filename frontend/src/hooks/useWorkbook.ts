import { useEffect, useRef } from 'react';
import { useRecoilState, useSetRecoilState } from 'recoil';

import {
  deleteWorkbookAsync,
  getWorkbooksAsync,
  postWorkbookAsync,
  putWorkbookAsync,
} from '../api';
import { shouldWorkbookUpdateState, workbookState } from '../recoil';
import { TagResponse, WorkbookResponse } from '../types';
import useErrorHandler from './useErrorHandler';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useWorkbook = () => {
  const [{ data: workbooks, errorMessage }, setWorkbooks] =
    useRecoilState(workbookState);
  const setIsWorkbookUpdate = useSetRecoilState(shouldWorkbookUpdateState);

  const deletedWorkbookId = useRef(-1);

  const updateWorkbooks = async () => {
    try {
      const workbookResponse = await getWorkbooksAsync();
      setWorkbooks({ data: workbookResponse, errorMessage: null });
      setIsWorkbookUpdate(false);
    } catch (error) {
      setWorkbooks({
        data: [],
        errorMessage: '문제집을 불러오지 못했어요. 새로고침을 해보세요.',
      });
    }
  };

  const { routePrevPage } = useRouter();
  const showSnackbar = useSnackbar();
  const errorHandler = useErrorHandler();

  const createWorkbook = async (
    name: string,
    tags: TagResponse[],
    opened: boolean
  ) => {
    try {
      await postWorkbookAsync({ name, tags, opened });
      showSnackbar({ message: '문제집이 추가되었어요.' });
      routePrevPage();
      updateWorkbooks();
    } catch (error) {
      errorHandler(error);
    }
  };

  const editWorkbook = async (workbookInfo: WorkbookResponse) => {
    try {
      await putWorkbookAsync(workbookInfo);
      showSnackbar({ message: '문제집이 수정되었어요.' });
      routePrevPage();
      updateWorkbooks();
    } catch (error) {
      errorHandler(error);
    }
  };

  const deleteWorkbook = async (id: number) => {
    if (deletedWorkbookId.current === id) return;

    try {
      await deleteWorkbookAsync(id);
      deletedWorkbookId.current = id;
      showSnackbar({ message: '문제집이 삭제되었어요.' });
      updateWorkbooks();
    } catch (error) {
      errorHandler(error);
    }
  };

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return {
    workbooks,
    createWorkbook,
    editWorkbook,
    deleteWorkbook,
    updateWorkbooks,
  };
};

export default useWorkbook;
