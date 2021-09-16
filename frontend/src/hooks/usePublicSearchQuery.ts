import { useLocation } from 'react-router-dom';

import { SEARCH_CRITERIA } from '../constants';
import { ValueOf } from '../types/utils';

export type PublicSearchQueryReturnType = ReturnType<
  typeof usePublicSearchQuery
>;

const usePublicSearchQuery = () => {
  const query = new URLSearchParams(useLocation().search);

  const criteria = (query.get('criteria') ?? 'date') as ValueOf<
    typeof SEARCH_CRITERIA
  >;
  const tags = query.get('tags');
  const users = query.get('users');
  const keyword = query.get('keyword') ?? '';
  const size = query.get('size') ?? 0;

  return {
    tags,
    users,
    criteria,
    keyword,
    size: Number(size),
  };
};

export default usePublicSearchQuery;
