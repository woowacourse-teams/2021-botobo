export type MultiFilterTypes = '태그' | '작성자';

export interface MultiFilterValue {
  id: number;
  name: string;
  isSelected: boolean;
}

export interface MultiFilter {
  id: number;
  type: MultiFilterTypes;
  values: MultiFilterValue[];
}
