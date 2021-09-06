import { useState } from 'react';
import { useRecoilState } from 'recoil';

import { PublicWorkbookAsync, getSearchResultAsync } from '../api';
import { publicSearchResultState } from '../recoil';
import { PublicWorkbookResponse } from '../types';
import { useRouter } from '.';

const usePublicSearchResult = () => {
  const { routePrevPage, routePublicSearchResultQuery } = useRouter();

  const [isLoading, setIsLoading] = useState(false);

  const [{ startIndex }, setPublicWorkbookState] = useRecoilState(
    publicSearchResultState
  );

  const setIsInitialLoading = (isInitialLoading: boolean) => {
    setPublicWorkbookState((prevValue) => ({ ...prevValue, isInitialLoading }));
  };

  const setStartIndex = (type: 'add' | 'reset') => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      startIndex: type === 'reset' ? 0 : prevValue.startIndex + 1,
    }));
  };

  const setWorkbookSearchResult = (
    newWorkbooks: PublicWorkbookResponse[],
    isNew: boolean
  ) => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      publicWorkbookResult: isNew
        ? newWorkbooks
        : [...prevValue.publicWorkbookResult, ...newWorkbooks],
    }));
  };

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
      const newWorkbooks = await getSearchResultAsync({
        keyword,
        start: start ?? startIndex,
        ...options,
      });

      setWorkbookSearchResult(newWorkbooks, isNew);
      setStartIndex('add');
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
    setWorkbookSearchResult([], true);
    setStartIndex('reset');
  };

  return {
    resetSearchResult,
    isLoading,
    setIsLoading,
    searchForPublicWorkbook,
    routePrevPage,
  };
};

export default usePublicSearchResult;
