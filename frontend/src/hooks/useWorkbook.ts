import { useEffect, useRef } from 'react';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';

import {
  deleteWorkbookAsync,
  getWorkbooksAsync,
  postWorkbookAsync,
  putWorkbookAsync,
} from '../api';
import {
  searchKeywordRankingState,
  shouldWorkbookUpdateState,
  userState,
  workbookRankingState,
  workbookState,
} from '../recoil';
import { TagResponse, WorkbookResponse } from '../types';
import useErrorHandler from './useErrorHandler';
import useRouter from './useRouter';
import useSnackbar from './useSnackbar';

const useWorkbook = () => {
  const userInfo = useRecoilValue(userState);
  const [{ data: workbooks, errorMessage }, setWorkbooks] =
    useRecoilState(workbookState);
  const workbookRankings = useRecoilValue(workbookRankingState);
  const searchKeywordRankings = useRecoilValue(searchKeywordRankingState);
  const setIsWorkbookUpdate = useSetRecoilState(shouldWorkbookUpdateState);

  const { routePrevPage } = useRouter();
  const showSnackbar = useSnackbar();
  const errorHandler = useErrorHandler();

  const deletedWorkbookId = useRef(-1);

  const updateWorkbooks = async () => {
    if (!userInfo) {
      setWorkbooks({ data: [], errorMessage: null });
      setIsWorkbookUpdate(false);

      return;
    }

    try {
      const workbookResponse = await getWorkbooksAsync();
      setWorkbooks({ data: workbookResponse, errorMessage: null });
      setIsWorkbookUpdate(false);
    } catch (error) {
      errorHandler(error, updateWorkbooks);
    }
  };

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
      errorHandler(error, createWorkbook.bind(null, name, tags, opened));
    }
  };

  const editWorkbook = async (workbookInfo: WorkbookResponse) => {
    try {
      await putWorkbookAsync(workbookInfo);
      showSnackbar({ message: '문제집이 수정되었어요.' });
      routePrevPage();
      updateWorkbooks();
    } catch (error) {
      errorHandler(error, editWorkbook.bind(null, workbookInfo));
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
      errorHandler(error, deleteWorkbook.bind(null, id));
    }
  };

  useEffect(() => {
    if (errorMessage) {
      showSnackbar({ message: errorMessage, type: 'error' });
    }
  }, [errorMessage]);

  return {
    workbooks,
    workbookRankings,
    searchKeywordRankings,
    createWorkbook,
    editWorkbook,
    deleteWorkbook,
    updateWorkbooks,
  };
};

export default useWorkbook;
