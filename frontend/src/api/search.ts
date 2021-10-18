import axios from 'axios';

import { PublicWorkbookResponse, SearchKeywordResponse } from './../types';
import { cancelController, noAuthorization, request } from './request';
import { PublicWorkbookAsync } from './workbook';

export const getTagsFromWorkbookAsync = async (keyword: string) => {
  const params = new URLSearchParams();
  params.append('workbook', keyword);

  const { data } = await request.get<SearchKeywordResponse[]>('/tags', {
    headers: noAuthorization,
    params,
  });

  return data;
};

export const getUsersFromWorkbookAsync = async (keyword: string) => {
  const params = new URLSearchParams();
  params.append('workbook', keyword);

  const { data } = await request.get<SearchKeywordResponse[]>('/users', {
    headers: noAuthorization,
    params,
  });

  return data;
};

export const getTagKeywordAsync = async (keyword: string) => {
  try {
    cancelController.searchCancel?.();

    const params = new URLSearchParams();
    params.append('keyword', keyword);

    const { data } = await request.get<SearchKeywordResponse[]>(
      '/search/tags',
      {
        headers: noAuthorization,
        params,
        cancelToken: new axios.CancelToken((token) => {
          cancelController.searchCancel = token;
        }),
      }
    );

    return data;
  } catch (error) {
    if (axios.isCancel(error)) return [];

    throw error;
  }
};

export const getSearchResultAsync = async ({
  keyword,
  tags,
  users,
  criteria = 'date',
  start = 0,
  size = 20,
}: PublicWorkbookAsync) => {
  try {
    cancelController.tagCancel?.();

    const params = new URLSearchParams();
    params.append('keyword', keyword);
    params.append('criteria', criteria);
    params.append('start', String(start));
    params.append('size', String(size));
    tags && params.append('tags', tags);
    users && params.append('users', users);

    const { data } = await request.get<PublicWorkbookResponse[]>(
      '/search/workbooks',
      {
        headers: noAuthorization,
        params,
        cancelToken: new axios.CancelToken((token) => {
          cancelController.tagCancel = token;
        }),
      }
    );

    return data;
  } catch (error) {
    if (axios.isCancel(error)) return null;

    throw error;
  }
};
