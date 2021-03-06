import { useState } from 'react';
import { useRecoilState } from 'recoil';

import { PublicWorkbookAsync, getSearchResultAsync } from '../api';
import { SEARCH_CRITERIA } from '../constants';
import { publicSearchResultState } from '../recoil';
import { PublicWorkbookResponse } from '../types';
import { MultiFilterType } from '../types/filter';
import { ValueOf } from '../types/utils';
import { usePublicSearchQuery, useRouter, useSnackbar } from '.';

interface SetSearchResult {
  searchResult: PublicWorkbookResponse[];
  isReset: boolean;
}

const usePublicSearchResult = () => {
  const { routePrevPage, routePublicSearchResultQuery } = useRouter();

  const query = usePublicSearchQuery();
  const { keyword, criteria } = query;

  const [{ startIndex, singleFilters, multiFilters }, setPublicWorkbookState] =
    useRecoilState(publicSearchResultState);

  const [isLoading, setIsLoading] = useState(false);
  const [currentFilterId, setCurrentFilterId] = useState(
    singleFilters.find((filter) => filter.criteria === criteria)?.id ??
      singleFilters[0].id
  );

  const showSnackbar = useSnackbar();

  const setIsInitialLoading = (isInitialLoading: boolean) => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      isInitialLoading,
    }));
  };

  const setSearchResult = ({ searchResult, isReset }: SetSearchResult) => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      isInitialLoading: false,
      startIndex: isReset ? 0 : prevValue.startIndex + 1,
      publicWorkbookResult: isReset
        ? searchResult
        : [...prevValue.publicWorkbookResult, ...searchResult],
    }));
  };

  const setFilteredPublicWorkbook = (newQuery: PublicWorkbookAsync) => {
    setSearchResult({ searchResult: [], isReset: true });
    searchForPublicWorkbook({ ...newQuery, start: 0 });
  };

  const searchForPublicWorkbook = async (
    { keyword, start, criteria, ...options }: PublicWorkbookAsync,
    isReset = true
  ) => {
    setIsLoading(true);
    if (keyword === '') {
      setIsLoading(false);
      setIsInitialLoading(false);

      return;
    }

    try {
      const searchResult = await getSearchResultAsync({
        keyword,
        start: start ?? startIndex + 1,
        criteria,
        ...options,
      });

      if (!searchResult) return;

      setSearchResult({ searchResult, isReset });
      setIsLoading(false);
      routePublicSearchResultQuery({ keyword, criteria, ...options });
    } catch (error) {
      console.error(error);
      setIsLoading(false);
      setIsInitialLoading(false);

      showSnackbar({
        message: '????????? ???????????????. ?????? ??????????????????.',
        type: 'error',
      });
    }
  };

  const getMultiFilterValues = async (type: MultiFilterType) => {
    try {
      const targetFilter = multiFilters.find((item) => item.type === type);

      if (!targetFilter) return [];

      return await targetFilter.getValues(keyword);
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  const setInitialMultiFilterValues = async (type: MultiFilterType) => {
    const selectedIds = query[type]?.split(',');
    const response = await getMultiFilterValues(type);
    const values = response.map((value) => ({
      ...value,
      isSelected: Boolean(selectedIds?.includes(String(value.id))),
    }));

    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      multiFilters: prevValue.multiFilters.map((item) => {
        if (item.type !== type) return item;

        return { ...item, values };
      }),
    }));

    return values;
  };

  const removeMultiFilterItem = (type: MultiFilterType, itemId: number) => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      multiFilters: prevValue.multiFilters.map((item) => {
        if (item.type !== type) return item;

        return {
          ...item,
          values: item.values.map((value) => {
            if (value.id !== itemId) return value;

            return {
              ...value,
              isSelected: !value.isSelected,
            };
          }),
        };
      }),
    }));

    setFilteredPublicWorkbook({
      ...query,
      [type]: query[type]
        ?.split(',')
        .filter((id) => Number(id) !== itemId)
        .join(','),
    });
  };

  const setSingleFilterValues = (
    id: number,
    criteria: ValueOf<typeof SEARCH_CRITERIA>
  ) => {
    setCurrentFilterId(id);
    setFilteredPublicWorkbook({ ...query, criteria });
  };

  const resetFilterValues = () => {
    setCurrentFilterId(singleFilters[0].id);
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      multiFilters: prevValue.multiFilters.map((item) => ({
        ...item,
        values: item.values.map((value) => ({
          ...value,
          isSelected: false,
        })),
      })),
    }));
    setFilteredPublicWorkbook({ keyword });
  };

  return {
    query,
    isLoading,
    currentFilterId,
    singleFilters,
    multiFilters,
    setPublicWorkbookState,
    setIsLoading,
    searchForPublicWorkbook,
    setFilteredPublicWorkbook,
    setSingleFilterValues,
    setInitialMultiFilterValues,
    removeMultiFilterItem,
    resetFilterValues,
    routePrevPage,
  };
};

export default usePublicSearchResult;
