import { useState } from 'react';

import { PublicWorkbookAsync, getPublicWorkbookAsync } from '../api';
import { PublicWorkbookResponse } from '../types';

const usePublicSearch = () => {
  const [isSearching, setIsSearching] = useState(false);
  const [startIndex, setStartIndex] = useState(0);
  const [workbookSearchResult, setWorkbookSearchResult] = useState<
    PublicWorkbookResponse[]
  >([]);

  const searchForPublicWorkbook = async ({
    keyword,
    start,
    ...options
  }: PublicWorkbookAsync) => {
    if (keyword === '') {
      setIsSearching(false);

      return;
    }

    try {
      const data = await getPublicWorkbookAsync({
        keyword,
        start: start ?? startIndex,
        ...options,
      });
      setWorkbookSearchResult((prevValue) => [...prevValue, ...data]);
      setStartIndex((prevState) => prevState + 1);
      setIsSearching(false);
    } catch (error) {
      console.error(error);
      setIsSearching(false);
    }
  };

  const resetSearchResult = () => {
    setWorkbookSearchResult([]);
    setStartIndex(0);
  };

  return {
    workbookSearchResult,
    resetSearchResult,
    isSearching,
    setIsSearching,
    searchForPublicWorkbook,
  };
};

export default usePublicSearch;
