export { default as theme } from './theme';
export { default as ROUTE } from './route';
export { default as STORAGE_KEY } from './storage';
export { CLOUD_FRONT_DOMAIN } from './path';
export { SEARCH_TYPE, SEARCH_CRITERIA, SEARCH_ORDER } from './search';

export const WORKBOOK_NAME_MAXIMUM_LENGTH = 20;
export const CARD_TEXT_MAX_LENGTH = 2000;

export const QUIZ_MODE = {
  DEFAULT: 'DEFAULT',
  GUEST: 'GUEST',
  OTHERS: 'OTHERS',
} as const;
