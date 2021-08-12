import { css } from '@emotion/react';
import styled from '@emotion/styled';
import React, { useEffect, useState } from 'react';

import { Button, MainHeader, PublicWorkbookList } from '../components';
import { SEARCH_CRITERIA } from '../constants';
import { usePublicSearch, usePublicSearchQuery, useRouter } from '../hooks';
import { Flex } from '../styles';
import PublicWorkbookLoadable from './PublicSearchResultLoadable';

const filters = [
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

  const { routePrevPage, routePublicSearchResultQuery } = useRouter();

  const [currentFilterId, setCurrentFilterId] = useState(
    filters.find((filter) => filter.criteria === criteria)?.id ?? filters[0].id
  );

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
      <Container>
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
              {filters.map(({ id, name, criteria }) => (
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
            <PublicWorkbookList
              isLoading={isLoading}
              publicWorkbooks={workbookSearchResult}
              searchForPublicWorkbook={searchForPublicWorkbook}
            />
          </>
        )}
      </Container>
    </>
  );
};

const Container = styled.div`
  ${({ theme }) =>
    css`
      padding: ${theme.pageSize.padding};
      padding-top: 1rem;
    `}
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
  margin-bottom: 1.5rem;
`;

export default PublicSearchResultPage;
