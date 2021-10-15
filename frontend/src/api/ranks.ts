import {
  RankingSearchKeywordResponse,
  RankingWorkbookResponse,
} from '../types';
import { request } from './request';

export const getRankingWorkbooksAsync = async () => {
  const { data } = await request.get<RankingWorkbookResponse[]>(
    '/ranks/workbooks',
    {
      headers: {},
    }
  );

  return data;
};

export const getRankingSearchKeywordAsync = async () => {
  const { data } = await request.get<RankingSearchKeywordResponse[]>(
    '/ranks/search',
    {
      headers: {},
    }
  );

  return data;
};
