import {
  SearchKeywordRankingResponse,
  WorkbookRankingResponse,
} from '../types';
import { noAuthorization, request } from './request';

export const getWorkbookRankingsAsync = async () => {
  const { data } = await request.get<WorkbookRankingResponse[]>(
    '/ranks/workbooks',
    {
      headers: noAuthorization,
    }
  );

  return data;
};

export const getSearchKeywordRankingsAsync = async () => {
  const { data } = await request.get<SearchKeywordRankingResponse[]>(
    '/ranks/search',
    {
      headers: noAuthorization,
    }
  );

  return data;
};
