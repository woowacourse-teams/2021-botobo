export const SEARCH_TYPE = {
  NAME: 'name',
  TAG: 'tag',
  USER: 'user',
} as const;

export const SEARCH_CRITERIA = {
  DATE: 'date',
  NAME: 'name',
  COUNT: 'count',
  LIKE: 'like',
} as const;

export const SEARCH_ORDER = {
  ASC: 'asc',
  DESC: 'desc',
};
