import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import {
  getTagsWhenWorkbookSearchASync,
  getUsersWhenWorkbookSearchAsync,
} from '../api';
import DownIcon from '../assets/chevron-down-solid.svg';
import ResetIcon from '../assets/rotate-left-circular-arrow-interface-symbol.svg';
import {
  Button,
  MainHeader,
  MultiFilterSelector,
  PublicWorkbookList,
} from '../components';
import { SEARCH_CRITERIA } from '../constants';
import {
  useModal,
  usePublicSearchQuery,
  usePublicSearchResult,
  useRouter,
} from '../hooks';
import { Flex } from '../styles';
import {
  MultiFilter,
  MultiFilterTypes,
  MultiFilterValue,
} from '../types/filter';
import { ValueOf } from '../types/utils';
import { isMobile } from '../utils';
import PageTemplate from './PageTemplate';
import PublicWorkbookLoadable from './PublicSearchResultLoadable';

const dummyList = [
  {
    id: 1,
    name: 'java',
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
    name: 'javascriptjavascriptjavascriptjavascript',
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

const singleFilters = [
  { id: 1, type: '최신순', criteria: SEARCH_CRITERIA.DATE },
  { id: 2, type: '좋아요 순', criteria: SEARCH_CRITERIA.HEART },
  { id: 3, type: '이름 순', criteria: SEARCH_CRITERIA.NAME },
  { id: 4, type: '카드 개수 순', criteria: SEARCH_CRITERIA.COUNT },
];

const hasSelectedValueInMultiFilter = (values: MultiFilterValue[]) => {
  return values.some(({ isSelected }) => isSelected);
};

const PublicSearchResultPage = () => {
  const {
    workbookSearchResult,
    isLoading,
    isSearching,
    setIsSearching,
    searchForPublicWorkbook,
    resetSearchResult,
  } = usePublicSearchResult();

  const query = usePublicSearchQuery();
  const { keyword, tags, users, criteria } = query;

  const { openModal } = useModal();

  const { routePrevPage, routePublicSearchResultQuery } = useRouter();

  const [multiFilters, setMultiFilters] = useState<MultiFilter[]>([
    {
      id: 1,
      type: 'tags',
      name: '태그',
      values: [],
      getValues: getTagsWhenWorkbookSearchASync,
    },
    {
      id: 2,
      type: 'users',
      name: '작성자',
      values: [],
      getValues: getUsersWhenWorkbookSearchAsync,
    },
  ]);

  const [currentFilterId, setCurrentFilterId] = useState(
    singleFilters.find((filter) => filter.criteria === criteria)?.id ??
      singleFilters[0].id
  );

  const hasSelectedMultiFilter = multiFilters.find(({ values }) =>
    hasSelectedValueInMultiFilter(values)
  );

  const isFiltered =
    currentFilterId !== singleFilters[0].id || hasSelectedMultiFilter;

  const setSingleFilterValues = (
    id: number,
    criteria: ValueOf<typeof SEARCH_CRITERIA>
  ) => {
    setCurrentFilterId(id);
    routePublicSearchResultQuery({ keyword, tags, users, criteria, start: 0 });
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

  const setMultiFilterValues = async (type: MultiFilterTypes) => {
    const values = await getMultiFilterValues(type);

    setMultiFilters((prevValue) =>
      prevValue.map((item) => {
        if (item.type !== type) return item;

        return {
          ...item,
          values: dummyList.map((value) => ({ ...value, isSelected: false })),
        };
      })
    );
  };

  const removeMultiFilterItem = (type: MultiFilterTypes, itemId: number) => {
    setMultiFilters((prevValue) =>
      prevValue.map((item) => {
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
      })
    );

    routePublicSearchResultQuery({
      ...query,
      [type]: query[type]
        ?.split(',')
        .filter((id) => Number(id) !== itemId)
        .join(','),
    });
  };

  const resetFilterValues = () => {
    setSingleFilterValues(singleFilters[0].id, singleFilters[0].criteria);
    setMultiFilters((prevValue) =>
      prevValue.map((item) => ({
        ...item,
        values: item.values.map((value) => ({
          ...value,
          isSelected: false,
        })),
      }))
    );
    resetQuery();
  };

  const resetQuery = () => {
    routePublicSearchResultQuery({ keyword, start: 0 });
  };

  useEffect(() => {
    setIsSearching(true);
    setMultiFilterValues('tags');
    setMultiFilterValues('users');
    searchForPublicWorkbook(query);
  }, []);

  useEffect(() => {
    setIsSearching(true);
    resetSearchResult();
    searchForPublicWorkbook({ ...query, start: 0 });
  }, [currentFilterId, tags, users]);

  if (isSearching) {
    return <PublicWorkbookLoadable />;
  }

  return (
    <>
      <MainHeader />
      <StyledPageTemplate isScroll={true}>
        {workbookSearchResult.length !== 0 ? (
          <NoSearchResult>
            <div>검색 결과가 없어요.</div>
            <Button
              size="full"
              backgroundColor="gray_6"
              onClick={routePrevPage}
            >
              돌아가기
            </Button>
          </NoSearchResult>
        ) : (
          <>
            <Title>{keyword} 검색 결과</Title>
            <FilterWrapper>
              <Filter>
                {isFiltered && (
                  <FilterResetButton onClick={resetFilterValues}>
                    <span>초기화</span>
                    <ResetIcon width="1rem" height="1rem" />
                  </FilterResetButton>
                )}
                {multiFilters.map(({ id, type, name }, index) => (
                  <MultiFilterButton
                    key={id}
                    shape="round"
                    backgroundColor={
                      hasSelectedValueInMultiFilter(multiFilters[index].values)
                        ? 'green'
                        : 'gray_5'
                    }
                    inversion={true}
                    onClick={() => {
                      openModal({
                        content: (
                          <MultiFilterSelector
                            type={type}
                            name={name}
                            values={multiFilters[index].values}
                            query={query}
                            setMultiFilters={setMultiFilters}
                          />
                        ),
                      });
                    }}
                  >
                    {name}
                    <DownIcon width="1rem" height="1rem" />
                  </MultiFilterButton>
                ))}
                {singleFilters.map(({ id, type, criteria }) => (
                  <Button
                    key={id}
                    shape="round"
                    backgroundColor={
                      currentFilterId === id ? 'green' : 'gray_5'
                    }
                    inversion={true}
                    onClick={() => {
                      if (id === currentFilterId) return;

                      setSingleFilterValues(id, criteria);
                    }}
                  >
                    {type}
                  </Button>
                ))}
              </Filter>
              {hasSelectedMultiFilter && (
                <SelectedMultiFilterWrapper>
                  {multiFilters.map(({ type, values }) =>
                    values
                      .filter(({ isSelected }) => isSelected)
                      .map(({ name, id }) => (
                        <SelectedMultiFilterButton
                          key={id}
                          type="button"
                          shape="round"
                          backgroundColor={
                            type === 'users' ? 'gray_2' : 'green'
                          }
                          color={type === 'users' ? 'gray_8' : 'white'}
                          onClick={() => removeMultiFilterItem(type, id)}
                        >
                          {name}
                        </SelectedMultiFilterButton>
                      ))
                  )}
                </SelectedMultiFilterWrapper>
              )}
            </FilterWrapper>
            <PublicWorkbookList
              isLoading={isLoading}
              publicWorkbooks={workbookSearchResult}
              searchForPublicWorkbook={searchForPublicWorkbook}
            />
          </>
        )}
      </StyledPageTemplate>
    </>
  );
};

const StyledPageTemplate = styled(PageTemplate)`
  padding-top: 1rem;
`;

const NoSearchResult = styled.div`
  position: absolute;
  width: 80%;
  max-width: 20rem;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0);
  text-align: center;

  & > div {
    margin-bottom: 1rem;
  }
`;

const Title = styled.h2`
  word-break: break-all;
  text-align: center;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

const FilterWrapper = styled.div`
  position: sticky;
  top: 3.75rem;
  padding: 0.1rem 1.25rem 0.5rem 1.25rem;
  z-index: 1;
  transform: translateX(-1.25rem);
  width: calc(100% + 2.5rem);
  margin-bottom: 0.5rem;

  ${({ theme }) => css`
    background-color: ${theme.color.gray_0};
  `}
`;

const MultiFilterButton = styled(Button)`
  ${Flex({ items: 'center' })};
  padding: 0.5rem 0.8rem;

  & > svg {
    margin-left: 0.3rem;
  }
`;

const Filter = styled.div`
  ${Flex()};
  gap: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
  overflow-x: auto;

  & > button {
    flex-shrink: 0;
  }

  ${({ theme }) => css`
    ${isMobile
      ? css`
          ::-webkit-scrollbar {
            display: none;
          }
        `
      : css`
          padding-bottom: 0.5rem;
          ::-webkit-scrollbar {
            height: 4px;
          }
          ::-webkit-scrollbar-thumb {
            background-color: ${theme.color.gray_4};
          }
        `}
  `}
`;

const SelectedMultiFilterWrapper = styled.div`
  margin-top: 0.7rem;
  margin-bottom: 0.3rem;
  white-space: nowrap;
  overflow-x: auto;

  ${({ theme }) => css`
    ${isMobile
      ? css`
          ::-webkit-scrollbar {
            display: none;
          }
        `
      : css`
          padding-bottom: 0.5rem;
          ::-webkit-scrollbar {
            height: 4px;
          }
          ::-webkit-scrollbar-thumb {
            background-color: ${theme.color.gray_4};
          }
        `}
  `}
`;

const SelectedMultiFilterButton = styled(Button)`
  flex-shrink: 0;
  padding: 0.5rem 1rem;

  &:not(:first-of-type) {
    margin-left: 0.5rem;
  }
`;

const FilterResetButton = styled.button`
  ${Flex({ items: 'center' })};

  & > svg {
    margin-left: 0.3rem;
  }
`;

export default PublicSearchResultPage;
