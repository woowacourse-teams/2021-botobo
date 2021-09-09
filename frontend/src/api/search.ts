import { PublicWorkbookResponse, SearchKeywordResponse } from './../types';
import { request } from './request';
import { PublicWorkbookAsync } from './workbook';

export const getTagsFromWorkbookAsync = async (keyword: string) => {
  const { data } = await request.get<SearchKeywordResponse[]>(
    `/tags?workbook=${keyword}`
  );

  return data;
};

export const getUsersFromWorkbookAsync = async (keyword: string) => {
  const { data } = await request.get<SearchKeywordResponse[]>(
    `/users?workbook=${keyword}`
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
  tags,
  users,
  criteria = 'date',
  order = 'desc',
  start = 0,
  size = 20,
}: PublicWorkbookAsync) => {
  const { data } = await request.get<PublicWorkbookResponse[]>(
    `/search/workbooks?keyword=${keyword}&criteria=${criteria}&order=${order}&start=${start}&size=${size}&type=user`
  );

  return data;
};
