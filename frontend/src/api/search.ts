import { PublicWorkbookResponse, SearchKeywordResponse } from './../types';
import { request } from './request';
import { PublicWorkbookAsync } from './workbook';

export const getUserKeywordAsync = async (keyword: string) => {
  const { data } = await request.get<SearchKeywordResponse[]>(
    `/search/users?keyword=${keyword}`
  );

  return data;
};

export const getTagKeywordAsync = async (keyword: string) => {
  const { data } = await request.get<SearchKeywordResponse[]>(
    `/search/tags?keyword=${keyword}`
  );

  return data;
};

export const getSearchResultAsync = async ({
  keyword,
  criteria = 'date',
  order = 'desc',
  type = 'name',
  start = 0,
  size = 20,
}: PublicWorkbookAsync) => {
  const { data } = await request.get<PublicWorkbookResponse[]>(
    `/search/workbooks?type=${type}&criteria=${criteria}&order=${order}&keyword=${keyword}&start=${start}&size=${size}`
  );

  return data;
};
