import { useState } from 'react';

import { PublicWorkbookAsync, getPublicWorkbookAsync } from '../api';
import { PublicWorkbookResponse } from '../types';

const usePublicSearch = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [start, setStart] = useState(0);
  const [workbookSearchResult, setWorkbookSearchResult] = useState<
    PublicWorkbookResponse[]
  >([]);

  const searchForPublicWorkbook = async ({
    keyword,
    ...options
  }: PublicWorkbookAsync) => {
    if (keyword === '') {
      setIsLoading(false);

      return;
    }

    try {
      const data = await getPublicWorkbookAsync({
        keyword,
        start,
        ...options,
      });
      setWorkbookSearchResult((prevValue) => [...prevValue, ...data]);
      setStart((prevState) => prevState + 1);
      setIsLoading(false);
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  const resetSearchResult = () => {
    setWorkbookSearchResult([]);
    setStart(0);
  };

  return {
    start,
    setStart,
    workbookSearchResult,
    resetSearchResult,
    isLoading,
    setIsLoading,
    searchForPublicWorkbook,
  };
};

export default usePublicSearch;
