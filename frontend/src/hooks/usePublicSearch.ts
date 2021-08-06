import { useState } from 'react';
import { useRecoilState } from 'recoil';

import {
  PublicWorkbookAsync,
  getPublicWorkbookAsync,
  getTagKeywordAsync,
  getUserKeywordAsync,
} from '../api';
import { searchKeywordState } from '../recoil';
import { PublicWorkbookResponse, SearchKeywordResponse } from '../types';

const usePublicSearch = () => {
  const [keyword, setKeyword] = useRecoilState(searchKeywordState);
  const [inputValue, setInputValue] = useState(keyword);
  const [isLoading, setIsLoading] = useState(false);
  const [searchResponse, setSearchResponse] = useState<
    PublicWorkbookResponse[] | SearchKeywordResponse[]
  >([]);

  const searchPublicWorkbook = async ({
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

  const searchKeyword = async ({
    keyword,
    type = 'name',
  }: Pick<PublicWorkbookAsync, 'keyword' | 'type'>) => {
    try {
      if (type === 'name') return searchPublicWorkbook({ keyword, type });

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
    keyword,
    setKeyword,
    inputValue,
    setInputValue,
    searchResponse,
    resetSearchResponse,
    isLoading,
    setIsLoading,
    searchPublicWorkbook,
    searchKeyword,
  };
};

export default usePublicSearch;
