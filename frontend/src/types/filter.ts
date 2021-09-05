import { SearchKeywordResponse } from '.';

export type MultiFilterTypes = '태그' | '작성자';

export type MultiFilterValue = SearchKeywordResponse & {
  isSelected: boolean;
};

export interface MultiFilter {
  id: number;
  type: MultiFilterTypes;
  values: MultiFilterValue[];
  getValues: (keyword: string) => Promise<SearchKeywordResponse[]>;
}
