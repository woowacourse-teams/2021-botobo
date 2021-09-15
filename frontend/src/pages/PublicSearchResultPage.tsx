import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect } from 'react';
import { useRecoilValue } from 'recoil';

import DownIcon from '../assets/chevron-down-solid.svg';
import ResetIcon from '../assets/rotate-left-circular-arrow-interface-symbol.svg';
import {
  Button,
  MainHeader,
  MultiFilterSelector,
  PublicWorkbookList,
} from '../components';
import { useModal, usePublicSearchResult } from '../hooks';
import { publicSearchResultState } from '../recoil';
import { Flex } from '../styles';
import { MultiFilterValue } from '../types/filter';
import { isMobile } from '../utils';
import PageTemplate from './PageTemplate';
import PublicSearchResultLoadable from './PublicSearchResultLoadable';

const hasSelectedValueInMultiFilter = (values: MultiFilterValue[]) => {
  return values.some(({ isSelected }) => isSelected);
};

const PublicSearchResultPage = () => {
  const {
    query,
    isLoading,
    currentFilterId,
    singleFilters,
    multiFilters,
    searchForPublicWorkbook,
    setFilteredPublicWorkbook,
    setSingleFilterValues,
    setInitialMultiFilterValues,
    removeMultiFilterItem,
    resetFilterValues,
    routePrevPage,
  } = usePublicSearchResult();

  const { publicWorkbookResult, isInitialLoading } = useRecoilValue(
    publicSearchResultState
  );

  const { keyword } = query;

  const { openModal } = useModal();

  const hasSelectedMultiFilter = multiFilters.find(({ values }) =>
    hasSelectedValueInMultiFilter(values)
  );

  const isFiltered =
    currentFilterId !== singleFilters[0].id || hasSelectedMultiFilter;
  const isNoSearchResult = !isLoading && publicWorkbookResult.length === 0;

  useEffect(() => {
    if (!isInitialLoading) return;

    setInitialMultiFilterValues('tags');
    setInitialMultiFilterValues('users');
    searchForPublicWorkbook({ keyword, start: 0 });
  }, [isInitialLoading]);

  if (isInitialLoading) {
    return <PublicSearchResultLoadable />;
  }

  return (
    <>
      <MainHeader />
      <StyledPageTemplate isScroll={true}>
        <>
          <Title>{keyword} 검색 결과</Title>
          <FilterWrapper>
            <Filter>
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
                  disabled={isNoSearchResult}
                  onClick={({ currentTarget }) => {
                    if (isNoSearchResult) return;

                    currentTarget.scrollIntoView({
                      behavior: 'smooth',
                      inline: 'center',
                      block: 'nearest',
                    });

                    openModal({
                      content: (
                        <MultiFilterSelector
                          type={type}
                          name={name}
                          values={multiFilters[index].values}
                          query={query}
                          setFilteredPublicWorkbook={setFilteredPublicWorkbook}
                          setInitialValues={() =>
                            setInitialMultiFilterValues(type)
                          }
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
                    !isNoSearchResult && currentFilterId === id
                      ? 'green'
                      : 'gray_5'
                  }
                  inversion={true}
                  disabled={isNoSearchResult}
                  onClick={({ currentTarget }) => {
                    if (isNoSearchResult) return;

                    if (id === currentFilterId) return;

                    currentTarget.scrollIntoView({
                      behavior: 'smooth',
                      inline: 'center',
                    });

                    setSingleFilterValues(id, criteria);
                  }}
                >
                  {type}
                </Button>
              ))}
            </Filter>

            {isFiltered && (
              <SelectedMultiFilterWrapper>
                <FilterResetButton onClick={resetFilterValues}>
                  <span>초기화</span>
                  <ResetIcon width="1rem" height="1rem" />
                </FilterResetButton>
                {multiFilters.map(({ type, values }) =>
                  values
                    .filter(({ isSelected }) => isSelected)
                    .map(({ name, id }) => (
                      <SelectedMultiFilterButton
                        key={id}
                        type="button"
                        shape="round"
                        backgroundColor={type === 'users' ? 'gray_2' : 'green'}
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
          {isNoSearchResult ? (
            <NoSearchResult>
              <div>검색 결과가 없어요.</div>
              {!hasSelectedMultiFilter && (
                <Button
                  size="full"
                  backgroundColor="gray_6"
                  onClick={routePrevPage}
                >
                  돌아가기
                </Button>
              )}
            </NoSearchResult>
          ) : (
            <PublicWorkbookList
              query={query}
              isLoading={isLoading}
              publicWorkbooks={publicWorkbookResult}
              searchForPublicWorkbook={searchForPublicWorkbook}
            />
          )}
        </>
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
  ${Flex({ items: 'center' })};
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
  height: 2rem;

  & > svg {
    margin-left: 0.3rem;
    margin-bottom: 0.1rem;
  }
`;

export default PublicSearchResultPage;
