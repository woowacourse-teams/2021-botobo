import { useState } from 'react';
import { useRecoilState } from 'recoil';

import { PublicWorkbookAsync, getSearchResultAsync } from '../api';
import { SEARCH_CRITERIA } from '../constants';
import { publicSearchResultState } from '../recoil';
import { PublicWorkbookResponse } from '../types';
import { MultiFilterTypes } from '../types/filter';
import { ValueOf } from '../types/utils';
import { usePublicSearchQuery, useRouter } from '.';

const dummyList = [
  {
    id: 1,
    name: 'java  dd',
  },
  {
    id: 2,
    name: 'javascript',
  },
  {
    id: 3,
    name: '자바',
  },
  {
    id: 4,
    name: '자스',
  },
  {
    id: 5,
    name: '자바스크립트',
  },
  {
    id: 6,
    name: `javascriptjavascriptjavascriptja
    vascript`,
  },
  {
    id: 7,
    name: '자스스스',
  },
  {
    id: 8,
    name: 'ㅁㄴㅇㅁㄴㅇ',
  },
];

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

  const getMultiFilterValues = async (type: MultiFilterTypes) => {
    try {
      const targetFilter = multiFilters.find((item) => item.type === type);

      if (!targetFilter) return [];

      return await targetFilter.getValues(keyword);
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  const setInitialMultiFilterValues = async (type: MultiFilterTypes) => {
    const values = await getMultiFilterValues(type);

    setPublicWorkbookState((prevValue) => ({
      ...prevValue,
      multiFilters: prevValue.multiFilters.map((item) => {
        if (item.type !== type) return item;

        return {
          ...item,
          values: dummyList.map((value) => ({ ...value, isSelected: false })),
        };
      }),
    }));
  };

  const removeMultiFilterItem = (type: MultiFilterTypes, itemId: number) => {
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
