import { useState } from 'react';
import { useRecoilState } from 'recoil';

import {
  PublicWorkbookAsync,
  getPublicWorkbookAsync,
  getTagKeywordAsync,
  getUserKeywordAsync,
} from '../api';
import { searchKeywordState, searchTypeState } from '../recoil';
import { PublicWorkbookResponse, SearchKeywordResponse } from '../types';

const usePublicSearch = () => {
  const [searchType, setSearchType] = useRecoilState(searchTypeState);
  const [searchKeyword, setSearchKeyword] = useRecoilState(searchKeywordState);
  const [inputValue, setInputValue] = useState(searchKeyword);
  const [isLoading, setIsLoading] = useState(false);
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
      const data = await getPublicWorkbookAsync({ keyword, ...options });
      setWorkbookSearchResult(data);
      setIsLoading(false);
    } catch (error) {
      setIsLoading(false);
      console.error(error);
    }
  };

  const searchForKeyword = async ({
    keyword,
    type = 'name',
  }: Pick<PublicWorkbookAsync, 'keyword' | 'type'>) => {
    if (keyword === '') {
      setIsLoading(false);

      return;
    }

    try {
      if (type === 'name') return searchForPublicWorkbook({ keyword, type });

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
      setIsLoading(false);
      console.error(error);
    }
  };

  const resetSearchResult = () => {
    setKeywordSearchResult([]);
    setWorkbookSearchResult([]);
  };

  return {
    searchType,
    setSearchType,
    searchKeyword,
    setSearchKeyword,
    inputValue,
    setInputValue,
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
