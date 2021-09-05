import { SearchKeywordResponse } from '.';

export type MultiFilterTypes = 'tags' | 'users';
export type MultiFilterNames = '태그' | '작성자';

export type MultiFilterValue = SearchKeywordResponse & {
  isSelected: boolean;
};

export interface MultiFilter {
  id: number;
  type: MultiFilterTypes;
  name: MultiFilterNames;
  values: MultiFilterValue[];
  getValues: (keyword: string) => Promise<SearchKeywordResponse[]>;
}
