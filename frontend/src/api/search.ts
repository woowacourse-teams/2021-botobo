import { PublicWorkbookResponse, SearchKeywordResponse } from './../types';
import { request } from './request';
import { PublicWorkbookAsync } from './workbook';

export const getTagsFromWorkbookAsync = async (keyword: string) => {
  const params = new URLSearchParams();
  params.append('workbook', keyword);

  const { data } = await request.get<SearchKeywordResponse[]>('/tags', {
    params,
  });

  return data;
};

export const getUsersFromWorkbookAsync = async (keyword: string) => {
  const params = new URLSearchParams();
  params.append('workbook', keyword);

  const { data } = await request.get<SearchKeywordResponse[]>('/users', {
    params,
  });

  return data;
};

export const getTagKeywordAsync = async (keyword: string) => {
  const params = new URLSearchParams();
  params.append('keyword', keyword);

  const { data } = await request.get<SearchKeywordResponse[]>('/search/tags', {
    params,
  });

  return data;
};

export const getSearchResultAsync = async ({
  keyword,
  tags,
  users,
  criteria = 'date',
  start = 0,
  size = 20,
}: PublicWorkbookAsync) => {
  const params = new URLSearchParams();
  params.append('keyword', keyword);
  params.append('criteria', criteria);
  params.append('start', String(start));
  params.append('size', String(size));
  tags && params.append('tags', tags);
  users && params.append('users', users);

  const { data } = await request.get<PublicWorkbookResponse[]>(
    '/search/workbooks',
    { params }
  );

  return data;
};
