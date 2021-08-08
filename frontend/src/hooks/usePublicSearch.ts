import { useState } from 'react';

import {
  PublicWorkbookAsync,
  getPublicWorkbookAsync,
  getTagKeywordAsync,
  getUserKeywordAsync,
} from '../api';
import { SEARCH_TYPE } from '../constants';
import { PublicWorkbookResponse, SearchKeywordResponse } from '../types';
import { ValueOf } from '../types/utils';

const usePublicSearch = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [start, setStart] = useState(0);
  const [keywordSearchResult, setKeywordSearchResult] = useState<
    SearchKeywordResponse[]
  >([]);
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

  const searchForKeyword = async (
    keyword: string,
    type: ValueOf<typeof SEARCH_TYPE>
  ) => {
    if (keyword === '') {
      setIsLoading(false);

      return;
    }

    try {
      if (type === 'name') return searchForPublicWorkbook({ keyword });

      let data: SearchKeywordResponse[] = [];

      if (type === 'tag') {
        data = await getTagKeywordAsync(keyword);
      }

      if (type === 'user') {
        data = await getUserKeywordAsync(keyword);
      }

      setKeywordSearchResult(data);
      setIsLoading(false);
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  const resetSearchResult = () => {
    setKeywordSearchResult([]);
    setWorkbookSearchResult([]);
    setStart(0);
  };

  return {
    start,
    setStart,
    keywordSearchResult,
    workbookSearchResult,
    resetSearchResult,
    isLoading,
    setIsLoading,
    searchForPublicWorkbook,
    searchForKeyword,
  };
};

export default usePublicSearch;
