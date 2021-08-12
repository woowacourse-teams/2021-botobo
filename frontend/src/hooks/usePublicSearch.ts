import { useState } from 'react';

import { PublicWorkbookAsync, getPublicWorkbookAsync } from '../api';
import { PublicWorkbookResponse } from '../types';
import useErrorHandler from './useErrorHandler';

const usePublicSearch = () => {
  const [isSearching, setIsSearching] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [startIndex, setStartIndex] = useState(0);
  const [workbookSearchResult, setWorkbookSearchResult] = useState<
    PublicWorkbookResponse[]
  >([]);

  const searchForPublicWorkbook = async ({
    keyword,
    start,
    ...options
  }: PublicWorkbookAsync) => {
    setIsLoading(true);

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
      setIsLoading(false);
    } catch (error) {
      useErrorHandler(error);
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
