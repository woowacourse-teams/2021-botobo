import { useEffect, useState } from 'react';
import { useRecoilState } from 'recoil';

import { PublicWorkbookAsync, getSearchResultAsync } from '../api';
import { SEARCH_CRITERIA } from '../constants';
import { publicSearchResultState } from '../recoil';
import { PublicWorkbookResponse } from '../types';
import { MultiFilterType } from '../types/filter';
import { ValueOf } from '../types/utils';
import { usePublicSearchQuery, useRouter, useSnackbar } from '.';

const usePublicSearchResult = () => {
  const { routePrevPage, routePublicCards, routePublicSearchResultQuery } =
    useRouter();

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
    isReset: boolean
  ) => {
    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      publicWorkbookResult: isReset
        ? newWorkbooks
        : [...prevValue.publicWorkbookResult, ...newWorkbooks],
    }));
  };

  const resetSearchResult = () => {
    setWorkbookSearchResult([], true);
    setStartIndex('reset');
  };

  const setFilteredPublicWorkbook = (newQuery: PublicWorkbookAsync) => {
    setIsLoading(true);
    resetSearchResult();
    searchForPublicWorkbook({ ...newQuery, start: 0 });
  };

  const searchForPublicWorkbook = async (
    { keyword, start, criteria, ...options }: PublicWorkbookAsync,
    isReset = true
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
        criteria,
        ...options,
      });

      setWorkbookSearchResult(newWorkbooks, isReset);
      setStartIndex('add');
      setIsLoading(false);
      setIsInitialLoading(false);

      routePublicSearchResultQuery({ keyword, criteria, ...options });
    } catch (error) {
      console.error(error);
      setIsLoading(false);
      setIsInitialLoading(false);

      showSnackbar({
        message: '검색에 실패했어요. 다시 시도해주세요.',
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
    const response = await getMultiFilterValues(type);
    const values = response.map((value) => ({ ...value, isSelected: false }));

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

  useEffect(() => {
    const [tag, user] = multiFilters;

    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      tags: tag.values,
      users: user.values,
    }));
  }, [multiFilters]);

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
    routePublicCards,
    routePrevPage,
  };
};

export default usePublicSearchResult;
