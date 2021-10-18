import {
  RankingSearchKeywordResponse,
  RankingWorkbookResponse,
} from '../types';
import { noAuthorization, request } from './request';

export const getRankingWorkbooksAsync = async () => {
  const { data } = await request.get<RankingWorkbookResponse[]>(
    '/ranks/workbooks',
    {
      headers: noAuthorization,
    }
  );

  return data;
};

export const getRankingSearchKeywordAsync = async () => {
  const { data } = await request.get<RankingSearchKeywordResponse[]>(
    '/ranks/search',
    {
      headers: noAuthorization,
    }
  );

  return data;
};
