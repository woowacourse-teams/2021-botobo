import { SEARCH_CRITERIA, SEARCH_ORDER, SEARCH_TYPE } from './../constants';
import {
  PublicWorkbookResponse,
  TagResponse,
  WorkbookResponse,
} from './../types';
import { ValueOf } from './../types/utils';
import { request } from './request';

interface PostWorkbookAsync {
  name: string;
  opened: boolean;
  tags: TagResponse[];
}

export interface PublicWorkbookAsync {
  keyword: string;
  criteria?: ValueOf<typeof SEARCH_CRITERIA>;
  order?: ValueOf<typeof SEARCH_ORDER>;
  type?: ValueOf<typeof SEARCH_TYPE>;
  start?: number;
  size?: number;
}

export const getWorkbooksAsync = async () => {
  const { data } = await request.get<WorkbookResponse[]>('/workbooks');

  return data;
};

export const postWorkbookAsync = async (params: PostWorkbookAsync) => {
  const { data } = await request.post<WorkbookResponse>('/workbooks', params);

  return data;
};

export const putWorkbookAsync = async (workbookInfo: WorkbookResponse) => {
  const { id, ...params } = workbookInfo;

  await request.put(`/workbooks/${id}`, params);
};

export const deleteWorkbookAsync = async (id: number) => {
  await request.delete(`/workbooks/${id}`);
};

export const getPublicWorkbookAsync = async ({
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
