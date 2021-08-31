import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import { Button, MainHeader, PublicWorkbookList } from '../components';
import { SEARCH_CRITERIA } from '../constants';
import {
  useModal,
  usePublicSearch,
  usePublicSearchQuery,
  useRouter,
} from '../hooks';
import { Flex } from '../styles';
import PageTemplate from './PageTemplate';
import PublicWorkbookLoadable from './PublicSearchResultLoadable';

interface MultiFilters {
  id: number;
  name: string;
  data: string[];
}

const multiFilters: MultiFilters[] = [
  { id: 1, name: '태그', data: [] },
  { id: 2, name: '작성자', data: [] },
];

const singleFilters = [
  { id: 1, name: '최신순', criteria: SEARCH_CRITERIA.DATE },
  { id: 2, name: '좋아요 순', criteria: SEARCH_CRITERIA.HEART },
  { id: 3, name: '이름 순', criteria: SEARCH_CRITERIA.NAME },
  { id: 4, name: '카드 개수 순', criteria: SEARCH_CRITERIA.COUNT },
];

const PublicSearchResultPage = () => {
  const {
    workbookSearchResult,
    isLoading,
    isSearching,
    setIsSearching,
    searchForPublicWorkbook,
    resetSearchResult,
  } = usePublicSearch();
  const { keyword, type, criteria } = usePublicSearchQuery();

  const { openModal } = useModal();

  const { routePrevPage, routePublicSearchResultQuery } = useRouter();

  const [selectedMultiFilters, setSelectedMultiFilters] =
    useState(multiFilters);
  const [currentFilterId, setCurrentFilterId] = useState(
    singleFilters.find((filter) => filter.criteria === criteria)?.id ??
      singleFilters[0].id
  );

  const setMultiFilterData = (filterName: string, data: string) => {
    setSelectedMultiFilters((prevValue) =>
      prevValue.map((value) => {
        if (value.name !== filterName) return value;

        return {
          ...value,
          data: [...value.data, data],
        };
      })
    );
  };

  useEffect(() => {
    setIsSearching(true);
    searchForPublicWorkbook({ keyword, type, criteria });
  }, []);

  if (isSearching) {
    return <PublicWorkbookLoadable />;
  }

  return (
    <>
      <MainHeader />
      <StyledPageTemplate isScroll={true}>
        {workbookSearchResult.length === 0 ? (
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
            <Filter>
              {multiFilters.map(({ id, name }, index) => (
                <Button
                  key={id}
                  shape="round"
                  backgroundColor={
                    selectedMultiFilters[index].data.length > 0
                      ? 'green'
                      : 'gray_5'
                  }
                  inversion={true}
                  onClick={() => {
                    openModal({
                      title: name,
                      content: (
                        <div onClick={() => setMultiFilterData(name, 'hello')}>
                          hello
                        </div>
                      ),
                      closeIcon: 'back',
                    });
                  }}
                >
                  {name}
                </Button>
              ))}
              {singleFilters.map(({ id, name, criteria }) => (
                <Button
                  key={id}
                  shape="round"
                  backgroundColor={currentFilterId === id ? 'green' : 'gray_5'}
                  inversion={true}
                  onClick={() => {
                    if (id === currentFilterId) return;

                    const resetValue = {
                      keyword,
                      type,
                      start: 0,
                      criteria,
                    };

                    setCurrentFilterId(id);
                    resetSearchResult();
                    setIsSearching(true);
                    routePublicSearchResultQuery(resetValue);
                    searchForPublicWorkbook(resetValue);
                  }}
                >
                  {name}
                </Button>
              ))}
            </Filter>
            <SelectedMultiFilterWrapper>
              {selectedMultiFilters.map(
                ({ id, name, data }) =>
                  data.length > 0 && (
                    <SelectedMultiFilter key={id}>
                      선택된 {name}: {data.join(', ')}
                    </SelectedMultiFilter>
                  )
              )}
            </SelectedMultiFilterWrapper>
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
  margin-top: 0.5rem;
  margin-bottom: 1rem;
  text-align: center;
  word-break: break-all;

  ${({ theme }) => css`
    font-size: ${theme.fontSize.medium};
  `}
`;

const Filter = styled.div`
  ${Flex()};
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
`;

const SelectedMultiFilterWrapper = styled.div`
  margin-bottom: 1rem;

  ${({ theme }) =>
    css`
      font-size: ${theme.fontSize.small};
    `}
`;

const SelectedMultiFilter = styled.div`
  margin-bottom: 0.5rem;

  &:last-of-type {
    margin-bottom: 0;
  }
`;

export default PublicSearchResultPage;
