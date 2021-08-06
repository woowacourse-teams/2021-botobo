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

const isPublicWorkbookResponse = (
  data: PublicWorkbookResponse[] | SearchKeywordResponse[]
): data is PublicWorkbookResponse[] => {
  if (data.length === 0) return false;

  const [response] = data;

  return 'cardCount' in response && 'author' in response;
};

const usePublicSearch = () => {
  const [searchKeyword, setSearchKeyword] = useRecoilState(searchKeywordState);
  const [searchType, setSearchType] = useRecoilState(searchTypeState);
  const [inputValue, setInputValue] = useState(searchKeyword);
  const [isLoading, setIsLoading] = useState(false);
  const [searchResponse, setSearchResponse] = useState<
    PublicWorkbookResponse[] | SearchKeywordResponse[]
  >([]);

  const searchForPublicWorkbook = async ({
    keyword,
    ...options
  }: PublicWorkbookAsync) => {
    try {
      const data = await getPublicWorkbookAsync({ keyword, ...options });
      setSearchResponse(data);
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
    try {
      if (type === 'name') return searchForPublicWorkbook({ keyword, type });

      let data: SearchKeywordResponse[] = [];

      if (type === 'tag') {
        data = await getTagKeywordAsync(keyword);
      }

      if (type === 'user') {
        data = await getUserKeywordAsync(keyword);
      }

      setSearchResponse(data);
      setIsLoading(false);
    } catch (error) {
      setIsLoading(false);
      console.error(error);
    }
  };

  const resetSearchResponse = () => setSearchResponse([]);

  return {
    searchKeyword,
    setSearchKeyword,
    searchType,
    setSearchType,
    inputValue,
    setInputValue,
    searchResponse,
    resetSearchResponse,
    isLoading,
    setIsLoading,
    searchForPublicWorkbook,
    searchForKeyword,
    isPublicWorkbookResponse,
  };
};

export default usePublicSearch;
