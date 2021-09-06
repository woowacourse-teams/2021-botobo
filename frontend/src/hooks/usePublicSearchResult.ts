import { useState } from 'react';
import { useRecoilState } from 'recoil';

import { PublicWorkbookAsync, getSearchResultAsync } from '../api';
import {
  publicSearchResultState,
  publicSearchStartIndexState,
} from '../recoil';
import { publicSearchInitialLoadState } from '../recoil/searchState';
import { useRouter } from '.';

const usePublicSearchResult = () => {
  const { routePrevPage, routePublicSearchResultQuery } = useRouter();

  const [isLoading, setIsLoading] = useState(false);

  const [startIndex, setStartIndex] = useRecoilState(
    publicSearchStartIndexState
  );
  const [isInitialLoading, setIsInitialLoading] = useRecoilState(
    publicSearchInitialLoadState
  );
  const [workbookSearchResult, setWorkbookSearchResult] = useRecoilState(
    publicSearchResultState
  );

  const searchForPublicWorkbook = async (
    { keyword, start, ...options }: PublicWorkbookAsync,
    isNew = true
  ) => {
    if (keyword === '') {
      setIsLoading(false);
      setIsInitialLoading(false);

      return;
    }

    try {
      const newValues = await getSearchResultAsync({
        keyword,
        start: start ?? startIndex,
        ...options,
      });

      setWorkbookSearchResult(
        isNew ? newValues : (prevValues) => [...prevValues, ...newValues]
      );
      setStartIndex((prevValue) => prevValue + 1);
      setIsLoading(false);
      setIsInitialLoading(false);

      routePublicSearchResultQuery({ keyword, start, ...options });
    } catch (error) {
      console.error(error);
      setIsLoading(false);
      setIsInitialLoading(false);
    }
  };

  const resetSearchResult = () => {
    setWorkbookSearchResult([]);
    setStartIndex(0);
  };

  return {
    workbookSearchResult,
    resetSearchResult,
    isInitialLoading,
    isLoading,
    setIsLoading,
    searchForPublicWorkbook,
    routePrevPage,
  };
};

export default usePublicSearchResult;
