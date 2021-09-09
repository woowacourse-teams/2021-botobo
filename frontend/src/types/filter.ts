import { SEARCH_CRITERIA } from '../constants';
import { ValueOf } from './utils';
import { SearchKeywordResponse } from '.';

export type MultiFilterType = 'tags' | 'users';
export type MultiFilterName = '태그' | '작성자';

export type MultiFilterValue = SearchKeywordResponse & {
  isSelected: boolean;
};

export interface SingleFilter {
  id: number;
  type: string;
  criteria: ValueOf<typeof SEARCH_CRITERIA>;
}

export interface MultiFilter {
  id: number;
  type: MultiFilterType;
  name: MultiFilterName;
  values: MultiFilterValue[];
  getValues: (keyword: string) => Promise<SearchKeywordResponse[]>;
}
