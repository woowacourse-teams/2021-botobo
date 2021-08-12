import { SearchKeywordResponse } from './../types';
import { request } from './request';

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
