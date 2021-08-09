import { useLocation } from 'react-router-dom';

import { SEARCH_CRITERIA, SEARCH_ORDER, SEARCH_TYPE } from '../constants';
import { ValueOf } from '../types/utils';

const usePublicSearchQuery = () => {
  const query = new URLSearchParams(useLocation().search);

  const criteria = (query.get('criteria') ?? 'date') as ValueOf<
    typeof SEARCH_CRITERIA
  >;
  const type = (query.get('type') ?? 'name') as ValueOf<typeof SEARCH_TYPE>;
  const order = (query.get('order') ?? 'desc') as ValueOf<typeof SEARCH_ORDER>;
  const keyword = query.get('keyword') ?? '';
  const size = query.get('size') ?? 0;

  return {
    criteria,
    type,
    order,
    keyword,
    size: Number(size),
  };
};

export default usePublicSearchQuery;
