import { useState } from 'react';

import { PublicWorkbookAsync, getSearchResultAsync } from '../api';
import { PublicWorkbookResponse } from '../types';

const usePublicSearch = () => {
  const [isSearching, setIsSearching] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
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
      const data = await getSearchResultAsync({
        keyword,
        start: start ?? startIndex,
        ...options,
      });
      setWorkbookSearchResult((prevValue) => [...prevValue, ...data]);
      setStartIndex((prevState) => prevState + 1);
      setIsSearching(false);
      setIsLoading(false);
    } catch (error) {
      console.error(error);
      setIsSearching(false);
      setIsLoading(false);
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
    isLoading,
    setIsSearching,
    searchForPublicWorkbook,
  };
};

export default usePublicSearch;
